package marsrover.parser.fsm

import marsrover.Move.Moves
import marsrover.Squadron.Positions
import marsrover.{Move, Plateau, Position}

import scala.util.matching.Regex

sealed trait Parser {
  protected val plateauR: Regex = """([0-9]+)\s+([0-9]+)""".r
  protected val roverR: Regex = """([0-9]+)\s+([0-9]+)\s+([NSWE])""".r
  protected val movesR: Regex = """([LRF]+)""".r

  def parse(line: String): Either[String, Parser]

  protected def mkMoves(s: String): Moves = s.toList.map(Move(_))
}

object Parser {
  case object New extends Parser {
    override def parse(line: String): Either[String, Parser] = line match {
      case plateauR(x, y)  => Right(JustPlateau(Plateau(x, y)))
      case roverR(_, _, _) => Left("Expected Plateau size but got first Rover")
      case movesR(_)       => Left("Expected Plateau but got first rover commands")
      case _               => Right(this) //skip and keep parsing
    }
  }

  case class JustPlateau(plateau: Plateau) extends Parser {
    override def parse(line: String): Either[String, Parser] = line match {
      case roverR(x, y, d) => Right(FirstRover(plateau, Position(x, y, d)))
      case movesR(_)       => Left("Expected first rover position but got first commands")
      case _               => Right(this) //skip and keep parsing
    }
  }

  case class FirstRover(plateau: Plateau, position: Position) extends Parser {
    override def parse(line: String): Either[String, Parser] = line match {
      case movesR(ms)      => Right(FirstCommand(plateau, position, mkMoves(ms)))
      case roverR(_, _, _) => Left("Expected first rover commands but got another rover")
      case _               => Right(this) //skip and keep parsing
    }
  }

  case class FirstCommand(plateau: Plateau, position: Position, move: Moves) extends Parser {
    override def parse(line: String): Either[String, Parser] = line match {
      case roverR(x, y, d) => Right(SecondRover(plateau, Seq(position, Position(x, y, d)), move))
      case movesR(_)       => Left("Expected second rover position but got another set of commands for first rover")
      case _               => Right(this) //skip and keep parsing
    }
  }

  case class SecondRover(plateau: Plateau, positions: Positions, moves: Moves) extends Parser {
    override def parse(line: String): Either[String, Parser] = line match {
      case movesR(ms)      => Right(TwoOrMoreCommands(plateau, positions, List(moves, mkMoves(ms))))
      case roverR(_, _, _) => Left("Expected commands for second rover but got another rover")
      case _               => Right(this) //skip and keep parsing
    }
  }

  case class TwoOrMoreCommands(plateau: Plateau, positions: Positions, moves: List[Moves]) extends Parser {
    override def parse(line: String): Either[String, Parser] = line match {
      case roverR(x, y, d) => Right(TwoOrMoreRovers(plateau, positions :+ Position(x, y, d), moves))
      case movesR(_)       => Left(s"Expected rover but got another set of commands for rover no ${positions.length}")
      case _               => Right(this) //skip and keep parsing
    }
  }

  case class TwoOrMoreRovers(plateau: Plateau, positions: Positions, commands: List[Moves]) extends Parser {
    override def parse(line: String): Either[String, Parser] = line match {
      case movesR(ms)      => Right(TwoOrMoreCommands(plateau, positions, commands :+ mkMoves(ms)))
      case roverR(_, _, _) => Left(s"Expected commands for rover no ${positions.length} but got another rover")
      case _               => Right(this) //skip and keep parsing
    }
  }
}
