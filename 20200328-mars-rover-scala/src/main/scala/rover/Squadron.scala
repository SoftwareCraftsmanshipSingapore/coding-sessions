package rover

import rover.Squadron.{Commands, Rovers}

import scala.util.matching.Regex

class Squadron(val plateau: Plateau, rovers: Rovers, commands: Commands) {
  def execute() : Unit =
    rovers.zip(commands).foreach {
      case (r, cs) => r.move(cs)
    }

  def result: String = rovers.map(_.positionString).mkString("\n", "\n\n", "\n")
}

object Squadron {
  type Rovers = Seq[Rover]
  type Command = String
  type Commands = Seq[Command]
  private trait Parser {
    protected val locationR: Regex = """([0-9]+)\s+([0-9]+)""".r
    protected val roverR   : Regex = """([0-9]+)\s+([0-9]+)\s+([NSWE])""".r
    protected val commandsR: Regex = """([LRF]+)""".r

    def parse(line: String): Either[String, Parser]
  }
  private object Parser {
    case object New extends Parser {
      override def parse(line: String): Either[String, Parser] = line match {
        case locationR(x, y) => Right(JustPlateau()(Plateau(x, y)))
        case roverR(_, _, _) => Left("Expected Plateau size but got first Rover")
        case commandsR(_)    => Left("Expected Plateau but got first rover commands")
        case _               => Right(this) //skip and keep parsing
      }
    }

    case class JustPlateau()(implicit val plateau: Plateau) extends Parser {
      override def parse(line: String): Either[String, Parser] = line match {
        case roverR(x, y, d) => Right(FirstRover(Rover(x, y, d)))
        case commandsR(_)    => Left("Expected first rover but got first commands")
        case _               => Right(this) //skip and keep parsing
      }
    }

    case class FirstRover(rover: Rover)(implicit plateau: Plateau) extends Parser {
      override def parse(line: String): Either[String, Parser] = line match {
        case commandsR(cs)   => Right(FirstCommand(rover, cs))
        case roverR(_, _, _) => Left("Expected commands for first rover but got another rover")
        case _               => Right(this) //skip and keep parsing
      }
    }

    case class FirstCommand(rover: Rover, command: Command)(implicit val plateau: Plateau) extends Parser {
      override def parse(line: String): Either[String, Parser] = line match {
        case roverR(x, y, d) => Right(SecondRover(Seq(rover, Rover(x, y, d)), command))
        case commandsR(_)    => Left("Expected second rover but got another set of commands for first rover")
        case _               => Right(this) //skip and keep parsing
      }
    }

    case class SecondRover(rovers: Rovers, command: Command)(implicit val plateau: Plateau) extends Parser {
      override def parse(line: String): Either[String, Parser] = line match {
        case commandsR(cs)   => Right(TwoOrMoreCommands(rovers, Seq(command, cs)))
        case roverR(_, _, _) => Left("Expected commands for second rover but got another rover")
        case _               => Right(this) //skip and keep parsing
      }
    }

    case class TwoOrMoreCommands(rovers: Rovers, commands: Commands)(implicit val plateau: Plateau) extends Parser {
      override def parse(line: String): Either[String, Parser] = line match {
        case roverR(x, y, d) => Right(TwoOrMoreRovers(rovers :+ Rover(x, y, d), commands))
        case commandsR(_)    => Left(s"Expected rover but got another set of commands for rover no ${rovers.length}")
        case _               => Right(this) //skip and keep parsing
      }
    }

    case class TwoOrMoreRovers(rovers: Rovers, commands: Commands)(implicit val plateau: Plateau) extends Parser {
      override def parse(line: String): Either[String, Parser] = line match {
        case commandsR(cs)   => Right(TwoOrMoreCommands(rovers, commands :+ cs))
        case roverR(_, _, _) => Left(s"Expected commands for rover no ${rovers.length} but got another rover")
        case _               => Right(this) //skip and keep parsing
      }
    }
  }

  def apply(instructions: String): Either[String, Squadron] = {
    import Parser._
    @scala.annotation.tailrec
    def parse(parser: Parser, lines: List[String]): Either[String, Parser] = lines match {
      case Nil         => Right(parser)
      case last::Nil   => parser.parse(last)
      case first::rest => parser.parse(first) match {
        case Right(p) => parse(p, rest)
        case left     => left
      }
    }
    val lines = instructions.trim.split('\n').filter(_.nonEmpty).toList
    parse(New, lines) match {
      case Right(parser) => parser match {
        case New                 => Left("empty set of instructions")
        case _:JustPlateau       => Left("only plateau")
        case _:FirstRover        => Left("only one rover")
        case p:FirstCommand      => Right(new Squadron(p.plateau, Seq(p.rover), Seq(p.command)))
        case _:SecondRover       => Left("two rovers and missing 2nd rover commands")
        case p:TwoOrMoreCommands => Right(new Squadron(p.plateau, p.rovers, p.commands))
        case p:TwoOrMoreRovers   => Left(s"${p.rovers.length} rovers and missing last rover commands")
      }
      case Left(errors) => Left(errors)
    }
  }
}
