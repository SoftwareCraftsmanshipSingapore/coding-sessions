package rover

import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks
import rover.Direction._

class RoverTest extends org.scalatest.wordspec.AnyWordSpec with Matchers with TableDrivenPropertyChecks{

  private val plateau = Plateau(5, 5)

  "starting location (1, 2)" when {
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
            val rover = new Rover(Location(1, 2), direction, plateau)
            rover.forward()
            rover.position shouldBe (expectedLocation, direction)
            rover.lastMoveSuccess shouldBe true
          }
        }
    }
  }

  "starting at (1,2)" when {
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
            val rover = new Rover(location, direction, plateau)
            rover.left()
            rover.position shouldBe (location, newDirection)
          }
        }
    }
  }

  "a rover" when {
    "receiving a sequence of command" should {
      "moves to a new position" in {
        val rover = new Rover(Location(1, 1), N, plateau)
        rover.move("FLFR")
        rover.position shouldBe (Location(0,2), N)
      }
    }
  }

  "a rover" when {
    val directions = Table("direction", N, W, S, E)
    directions.forEvery{
      d =>
        s"facing $d and instructed to move outside of the plateau" should {
          "refuse the command and remain in the original position" in {
            val rover = new Rover(Location(0, 0), d, Plateau(0, 0))
            rover.forward()
            rover.position shouldBe (Location(0, 0), d)
            rover.lastMoveSuccess shouldBe false
          }
        }
    }
  }

}
