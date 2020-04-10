package marsrover.parser.fast

import fastparse._
import marsrover.{Direction, Location, Plateau, Position, Rover}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class Parser extends AnyWordSpec with Matchers {
  "1 1" should {
    "parser" in {
      val Parsed.Success(Plateau(1, 1), _) = plateau("1 1")
    }
  }
  "rover: 1 1 N" should {
    "parse" in {
      val Parsed.Success(parsedPosition, _) = position("1 1 N")
      parsedPosition shouldBe Position(Location(1, 1), Direction.N)
    }
  }
  "full instruction for one rover position and commands" should {
    "be parsed" in {
      val plateau = Plateau(1, 1)
      val rover = Rover(1, 1, Direction.N, plateau)
      val commands = "FFLFRLRL"

      val inputInstructions =
        """
          |1 1
          |
          |1 1 N
          |
          |FFLFRLRL
          |""".stripMargin

      instruction(inputInstructions) match {
        case Parsed.Success(parsedInstructions, _) =>
          println(parsedInstructions)
        case f@Parsed.Failure(str, i, extra) =>
          val t = f.trace(true)
          println(t.aggregateMsg)
      }

    }
  }
}
