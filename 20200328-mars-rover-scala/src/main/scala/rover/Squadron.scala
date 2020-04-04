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
    protected val roverR   : Regex = """([0-9]+)\s+([0-9]+)\s([NSWE])""".r
    protected val commandsR: Regex = """([LRF]+)""".r

    def parse(line: String): Option[Parser]
  }
  private object Parser {
    case object New extends Parser {
      override def parse(line: String): Option[Parser] = Option(line) collect {
        case locationR(x, y) => PlateauP()(Plateau(x, y))
      }
    }

    case class PlateauP()(implicit val plateau: Plateau) extends Parser {
      override def parse(line: String): Option[Parser] = Option(line) collect {
        case roverR(x, y, d) => FirstRover(Rover(x, y, d))
      }
    }

    case class FirstRover(rover: Rover)(implicit plateau: Plateau) extends Parser {
      override def parse(line: String): Option[Parser] = Option(line) collect {
        case commandsR(cs) => FirstCommand(rover, cs)
      }
    }

    case class FirstCommand(rover: Rover, command: Command)(implicit val plateau: Plateau) extends Parser {
      override def parse(line: String): Option[Parser] = Option(line) collect {
        case roverR(x, y, d) => SecondRover(Seq(rover, Rover(x, y, d)), command)
      }
    }

    case class SecondRover(rovers: Rovers, command: Command)(implicit val plateau: Plateau) extends Parser {
      override def parse(line: String): Option[Parser] = Option(line) collect {
        case commandsR(cs) => TwoOrMoreCommands(rovers, Seq(command, cs))
      }
    }

    case class TwoOrMoreCommands(rovers: Rovers, commands: Commands)(implicit val plateau: Plateau) extends Parser {
      override def parse(line: String): Option[Parser] = Option(line) collect {
        case roverR(x, y, d) => TwoOrMoreRovers(rovers :+ Rover(x, y, d), commands)
      }
    }

    case class TwoOrMoreRovers(rovers: Rovers, commands: Commands)(implicit val plateau: Plateau) extends Parser {
      override def parse(line: String): Option[Parser] = Option(line) collect {
        case commandsR(cs) => TwoOrMoreCommands(rovers, commands :+ cs)
      }
    }
  }

  def apply(instructions: String): Squadron = {
    import Parser._
    @scala.annotation.tailrec
    def parse(parser: Parser, lines: List[String]): Parser = lines match {
      case Nil         => parser
      case last::Nil   => parser.parse(last).getOrElse(parser)
      case first::rest => parser.parse(first) match {
        case Some(p) => parse(p, rest)
        case None    => parser
      }
    }
    val lines = instructions.trim.split('\n').filter(_.nonEmpty).toList
    parse(New, lines) match {
      case New                 => sys.error("empty set of instructions")
      case _:PlateauP          => sys.error("only plateau")
      case _:FirstRover        => sys.error("only one rover")
      case p:FirstCommand      => new Squadron(p.plateau, Seq(p.rover), Seq(p.command))
      case _:SecondRover       => sys.error("two rovers and missing 2nd rover commands")
      case p:TwoOrMoreCommands => new Squadron(p.plateau, p.rovers, p.commands)
      case p:TwoOrMoreRovers   => sys.error(s"${p.rovers.length} rovers and missing last rover commands")
    }
  }
}
