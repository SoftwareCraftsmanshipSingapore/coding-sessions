package marsrover.parser

import marsrover.Position

package object fast {
  import fastparse._
  import marsrover.{Direction, Plateau}

  object instruction {
    import MultiLineWhitespace._
    private type Instruction = (Plateau, Seq[(Position, String)])

    def apply(s: String): Parsed[Instruction] = parse(s, instruction(_))

    private def instruction[_:P]: P[Instruction] =
      P (CharsWhile(_ != '\n').? ~ plateau.plateau ~ (position.position ~ commands).rep(1))
  }

  object plateau {
    def apply(s: String): Parsed[Plateau] = parse(s, plateau(_))

    import SingleLineWhitespace._
    def plateau[_:P]:P[Plateau] = P(number ~ number).map {
      case (x, y) => Plateau(x,y)
    }
  }

  object position {
    import SingleLineWhitespace._

    def apply(s: String): Parsed[Position] = parse(s, position(_))

    def position[_:P]: P[Position] = P( number ~ number ~ direction ).map {
      case (x, y, d) => Position(x, y, d)
    }
  }
  private def number[_:P]:P[Int] = P( CharsWhileIn("0-9").!).map(_.toInt)
  private def direction[_:P]:P[Direction] = P (CharIn("NSWE").!).map(Direction.from)
  private def commands[_:P]:P[String] = P (CharsWhileIn("FLR").!).map(_.toString)
}
