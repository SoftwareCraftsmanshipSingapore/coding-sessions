package marsrover.parser

import marsrover.Move.Moves
import marsrover.{Move, Position}

package object fast {
  import fastparse._
  import marsrover.{Direction, Plateau}

  def px(s: String): Parsed[String] = parse(s, x(_))
  def x[_:P]:P[String] = {
    import NoWhitespace._
    P(CharsWhile(_ != '\n') ~ "\n" ~ CharsWhile(_ != '\n').! ~ AnyChar.rep().!)
  }.map {
    case (a, b) => s"$a$b"
  }

  object instruction {
    import SingleLineWhitespace._
    private type Instruction = (Plateau, Seq[(Position, Moves)])

    def apply(s: String): Parsed[Instruction] = parse(s, instruction(_))

    private def instruction[_:P]: P[Instruction] = {
      P ( plateau.withGarbage ~ (position.position ~ commands).rep(1))
    }
  }

  object plateau {
    def apply(s: String): Parsed[Plateau] = parse(s, plateau(_))

    def plateau[_:P]:P[Plateau] = {
      import SingleLineWhitespace._
      P(number ~ number ~ newline0).map {
        case (x, y) => Plateau(x,y)
      }.opaque("plateau")
    }

    def withGarbage[_:P]:P[Plateau] = {
      import NoWhitespace._
      P(/*(newline0 ~ CharsWhile(_ != '\n') ~ newline0 ~ plateau) |*/ (AnyChar ~ plateau))
    }

  }

  object position {
    import SingleLineWhitespace._

    def apply(s: String): Parsed[Position] = parse(s, position(_))

    def position[_:P]: P[Position] = P( number ~ number ~ direction ~ newline0).map {
      case (x, y, d) => Position(x, y, d)
    }
  }
  private def number[_:P]:P[Int] = P( CharsWhileIn("0-9").!).map(_.toInt)
  private def direction[_:P]:P[Direction] = P (CharIn("NSWE").!).map(Direction(_)).opaque("direction")
  private def commands[_:P]:P[Moves] = {
    import SingleLineWhitespace._
    P (CharsWhileIn("FLR").! ~ newline0).map(_.toList.map(Move(_))).opaque("commands")
  }

  private def newline0[_:P]: P[Unit] = {
    import NoWhitespace._
     P("\n").rep()
  }
  private def newline[_:P]: P[Unit] = {
    import NoWhitespace._
    val min = 1
     P("\n").rep(min)
  }
}
