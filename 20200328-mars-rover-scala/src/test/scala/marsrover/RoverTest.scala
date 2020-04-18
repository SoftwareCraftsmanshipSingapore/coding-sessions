package marsrover

import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks
import marsrover.Direction._
import marsrover.Move._

class RoverTest extends org.scalatest.wordspec.AnyWordSpec with Matchers with TableDrivenPropertyChecks{

  private val plateau = Plateau(5, 5)

  "move from starting location (1, 2)" when {
    val testData = Table(
      ("direction", "expected")
      ,(N,  Location (1, 3))
      ,(S,  Location (1, 1))
      ,(E,  Location (2, 2))
      ,(W,  Location (0, 2))
    )
    testData forEvery {
      (direction, expectedLocation) =>
        s"direction $direction and instructed to go forward" should {
          s"end up at $expectedLocation" in {
            val rover = Rover(1, 2, direction, plateau)
            rover.move(F)
            rover.position shouldBe Position(expectedLocation, direction)
          }
        }
    }
  }

  "turn left starting at (1,2)" when {
    val testData = Table(
      ("direction", "new direction")
      ,(N, W)
      ,(S, E)
      ,(E, N)
      ,(W, S)
    )
    testData.forEvery{
      (direction, newDirection) =>
        s"pointing $direction instructed to turn Left" should {
          s"point $newDirection" in {
            val location = Location(1, 2)
            val rover = Rover(1, 2, direction, plateau)
            rover.move(L)
            rover.position shouldBe Position(location, newDirection)
          }
        }
    }
  }

  "a rover" when {
    "receiving a sequence of commands" should {
      "moves to a new position" in {
        val rover = Rover(1, 1, N, plateau)
        rover.move(F, L, F, R)
        rover.position shouldBe Position(Location(0, 2), N)
      }
    }

    "receiving a sequence of commands" that {
      "take it outside the boundary" should {
        "stop at the command that takes it outside the plateau boundary" in {
          val rover = Rover(1, 1, N, plateau)

          rover.move(L, F, R, L, F, R, L, L)

          rover.position shouldBe Position(Location(0, 1), W)
          rover.moves.mkString shouldBe "LFRL"
          rover.failureReason shouldBe Option("encountered edge of plateau")
        }
      }
    }
  }


  "a rover" when {
    val directions = Table("direction", N, W, S, E)
    directions.forEvery{
      d =>
        s"facing $d and instructed to move outside of the plateau" should {
          "refuse the command and remain in the original position" in {
            val rover = Rover(0, 0, d, Plateau(0, 0))
            rover.move(F)
            rover.position shouldBe Position(Location(0, 0), d)
          }
        }
    }
  }

}
