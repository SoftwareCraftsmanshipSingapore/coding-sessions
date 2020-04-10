package marsrover

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
  private val right = Symbol("right")
  private val p   = "5 5"
  private val r1  = "1 2 N"
  private val r2c = "LFLFLFLFF"
  private val r2  = "3 3 E"
  private val r1c = "FFRFFRFRRF"
  private val r3  = "0 0 N"
  private val r3c = "LRLRLRLRLRFFF"

  "a squadron" should {
    "parse the instructions" that {
      "are well formed" in {
        val instructions =
         s"""
            |$p
            |
            |$r1
            |
            |$r2c
            |
            |$r2
            |
            |$r1c
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

            //s.plateau shouldBe Plateau(5, 5)
            s.result shouldBe expectedOutput
          case Left(error) =>
            fail(s"failed to parse instructions: $error")
        }
      }
      "contain plateau, first rover and first command" in {
        Squadron(s"$p\n$r1\n$r1c") shouldBe right
        Squadron(s"$p\ngarbage\n$r1\n\n$r1c\ngarbage\\\nmore garbage") shouldBe right
      }
      "contain plateau two rovers and their commands" in {
        Squadron(s"$p\n$r1\n$r1c\n$r2\n$r2c") shouldBe right
      }
      "contain plateau three rovers and their commands" in {
        Squadron(s"$p\n$r1\n$r1c\n$r2\n$r2c\n$r3\n$r3c") shouldBe right
      }
    }
    "fail to parse the instructions" that {
      "are empty" in {
        Squadron("") shouldBe Left("empty set of instructions")
      }
      "contain the plateau size" in {
        Squadron(s"$p") shouldBe Left("only plateau")
        Squadron(s"$p\n\ngarbage") shouldBe Left("only plateau")
      }
      "and first rover" in {
        Squadron(s"$p\ngarbage\n$r1\ngarbage") shouldBe Left("only one rover position")
      }
      "and first rover's commands and second rover" in {
        Squadron(s"$p\ngarbage\n$r1\ngarbage\n$r1c\n$r2") shouldBe Left("two rover positions and missing 2nd rover commands")
      }
      "and third rover" in {
        Squadron(s"$p\n$r1\n$r1c\n$r2\n$r2c\n$r3\ngarbage\n") shouldBe Left("3 rover positions and missing last rover commands")
      }
    }
  }
}
