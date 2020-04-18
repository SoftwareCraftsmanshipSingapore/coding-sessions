package marsrover

import marsrover.Move._
import marsrover.Squadron.Rovers
import marsrover.parser.fsm.Parser

class Squadron(rovers: Rovers, commands: List[Moves]) {
  def execute() : Unit =
    rovers.zip(commands).foreach {
      case (r, cs) => r.move(cs: _*)
    }

  def result: String = rovers.map(_.positionString).mkString("\n", "\n\n", "\n")
}

object Squadron {
  type Positions = Seq[Position]
  type Rovers = Seq[Rover]
  type Command = String

  def apply(instructions: String): Either[String, Squadron] = {
/*
    parser.fast.instruction(instructions) match {
      case Parsed.Success((plateau, roverPositionsAndCommands), _) =>
        val (positions, moves) = roverPositionsAndCommands.unzip
        Right(new Squadron(mkRovers(plateau, positions), moves.toList))
      case f@Parsed.Failure(_, index, _) =>
        val t = f.trace(true)
        Left(s"index: $index expected: ${t.label}")
    }
*/

    import marsrover.parser.fsm.Parser._
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
        case _:FirstRover        => Left("only one rover position")
        case p:FirstCommand      => Right(new Squadron(mkRovers(p.plateau, Seq(p.position)), List(p.move)))
        case _:SecondRover       => Left("two rover positions and missing 2nd rover commands")
        case p:TwoOrMoreCommands => Right(new Squadron(mkRovers(p.plateau, p.positions), p.moves))
        case p:TwoOrMoreRovers   => Left(s"${p.positions.length} rover positions and missing last rover commands")
      }
      case Left(errors) => Left(errors)
    }
  }



  private def mkRovers(plateau: Plateau, positions: Positions) = positions.map(p => new Rover(p, plateau))
}
