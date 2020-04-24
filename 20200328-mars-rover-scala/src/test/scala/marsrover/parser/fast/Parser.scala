package marsrover.parser.fast

import fastparse._
import marsrover.Direction.N
import marsrover.Move.{F, L, R}
import marsrover.{Direction, Location, Plateau, Position}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class Parser extends AnyWordSpec with Matchers {
  "1 x 1 plateau" should {
    "be parsed" when {
      "it is well formed" in {
        val Parsed.Success(Plateau(1, 1), _) = plateau("1 1")
      }
      "and has some garbage" ignore {
        val Parsed.Success(Plateau(1, 1), _) = plateau("\n\n1 1")
      }
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
      val inputInstructions =
        """
          |1 1
          |
          |1 1 N
          |
          |FFLFRLRL
          |""".stripMargin

      val result = instruction(inputInstructions)
      val Parsed.Success(parsed, _) = result
      val (plateau, positionsAndCommands) = parsed
      val (positions, commands) = positionsAndCommands.unzip
      plateau shouldBe Plateau(1, 1)
      positions shouldBe List(Position(1, 1, N))
      commands shouldBe List(List(F, F, L, F, R, L, R, L))
    }
  }
  "full instructions for one rover positions and commands with interspersed garbage" should {
    "be parsed" ignore {
      val inputInstructions =
        """
          |garbage
          |1 1
          |1 1 N
          |FFLRRFLF
          |""".stripMargin

      val result = instruction(inputInstructions)
//      val f@Parsed.Failure(_, _, _) = result
//      println(f.trace(true).msg)
      val Parsed.Success(parsed, _) = result
      val (plateau, positionsAndCommands) = parsed
      val (positions, commands) = positionsAndCommands.unzip
      plateau shouldBe Plateau(1, 1)
      positions shouldBe List(Position(1, 1, N))
      commands shouldBe List(List(F,F,L,R,R,F,L,F))
    }
  }
  "fff" in {
    val Parsed.Success(parsed, _) = px("aa\nsss\n\ndd")
    parsed shouldBe "sss\n\ndd"
  }
}
