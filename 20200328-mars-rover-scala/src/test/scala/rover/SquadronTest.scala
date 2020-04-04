package rover

import org.scalatest.EitherValues
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

//TODO: 1. Invalid instructions
//      1.1  bad direction: X instead of N
//      1.2  line delimiters assumed to be \n\n
//      1.3  rover position outside of plateau
//TODO: 2. Collision detection
//      2.1  result communication
//TODO: 3. Boundary detection
//TODO: 4. One rover succeeds one fails
//TODO: 5. World exploration
//      5.1 world setup
//      5.2 rover cell content detection
class SquadronTest  extends AnyWordSpec with Matchers with EitherValues {

  "a squadron" should {
    "parse the instructions" that {
      "are well formed" in {
        val instructions =
          """
            |5 5
            |d
            |1 2 N
            |
            |LFLFLFLFF
            |
            |3 3 E
            |
            |FFRFFRFRRF
            |""".stripMargin

        val expectedOutput =
          """
            |1 3 N
            |
            |5 1 E
            |""".stripMargin
        Squadron(instructions) match {
          case Right(s) =>
            s.execute()

            s.plateau shouldBe Plateau(5, 5)
            s.result shouldBe expectedOutput
          case Left(error) =>
            fail(s"failed to parse instructions: $error")
        }
      }
    }
    "fail to parse the instructions" that {
      "are empty" in {
        Squadron("") shouldBe Left("empty set of instructions")
      }
      "only contain the plateau size" in {
        Squadron("5 5") shouldBe Left("only plateau")
      }
      "only contain the plateau size and garbage" in {
        Squadron("5 5\ngarbage") shouldBe Left("only plateau")
      }
    }
  }
}
