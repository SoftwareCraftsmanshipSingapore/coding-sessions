package marsrover.parser.fsm

import marsrover.Squadron.{Command, Commands, Positions}
import marsrover.{Plateau, Position}

import scala.util.matching.Regex

sealed trait Parser {
  protected val locationR: Regex = """([0-9]+)\s+([0-9]+)""".r
  protected val roverR   : Regex = """([0-9]+)\s+([0-9]+)\s+([NSWE])""".r
  protected val commandsR: Regex = """([LRF]+)""".r

  def parse(line: String): Either[String, Parser]
}

object Parser {
  case object New extends Parser {
    override def parse(line: String): Either[String, Parser] = line match {
      case locationR(x, y) => Right(JustPlateau(Plateau(x, y)))
      case roverR(_, _, _) => Left("Expected Plateau size but got first Rover")
      case commandsR(_)    => Left("Expected Plateau but got first rover commands")
      case _               => Right(this) //skip and keep parsing
    }
  }

  case class JustPlateau(plateau: Plateau) extends Parser {
    override def parse(line: String): Either[String, Parser] = line match {
      case roverR(x, y, d) => Right(FirstRover(plateau, Position(x, y, d)))
      case commandsR(_)    => Left("Expected first rover position but got first commands")
      case _               => Right(this) //skip and keep parsing
    }
  }

  case class FirstRover(plateau: Plateau, position: Position) extends Parser {
    override def parse(line: String): Either[String, Parser] = line match {
      case commandsR(cs)   => Right(FirstCommand(plateau, position, cs))
      case roverR(_, _, _) => Left("Expected first rover commands but got another rover")
      case _               => Right(this) //skip and keep parsing
    }
  }

  case class FirstCommand(plateau: Plateau, position: Position, command: Command) extends Parser {
    override def parse(line: String): Either[String, Parser] = line match {
      case roverR(x, y, d) => Right(SecondRover(plateau, Seq(position, Position(x, y, d)), command))
      case commandsR(_)    => Left("Expected second rover position but got another set of commands for first rover")
      case _               => Right(this) //skip and keep parsing
    }
  }

  case class SecondRover(plateau: Plateau, positions: Positions, command: Command) extends Parser {
    override def parse(line: String): Either[String, Parser] = line match {
      case commandsR(cs)   => Right(TwoOrMoreCommands(plateau, positions, Seq(command, cs)))
      case roverR(_, _, _) => Left("Expected commands for second rover but got another rover")
      case _               => Right(this) //skip and keep parsing
    }
  }

  case class TwoOrMoreCommands(plateau: Plateau, positions: Positions, commands: Commands) extends Parser {
    override def parse(line: String): Either[String, Parser] = line match {
      case roverR(x, y, d) => Right(TwoOrMoreRovers(plateau, positions :+ Position(x, y, d), commands))
      case commandsR(_)    => Left(s"Expected rover but got another set of commands for rover no ${positions.length}")
      case _               => Right(this) //skip and keep parsing
    }
  }

  case class TwoOrMoreRovers(plateau: Plateau, positions: Positions, commands: Commands) extends Parser {
    override def parse(line: String): Either[String, Parser] = line match {
      case commandsR(cs)   => Right(TwoOrMoreCommands(plateau, positions, commands :+ cs))
      case roverR(_, _, _) => Left(s"Expected commands for rover no ${positions.length} but got another rover")
      case _               => Right(this) //skip and keep parsing
    }
  }
}
