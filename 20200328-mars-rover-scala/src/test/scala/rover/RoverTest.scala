package rover

import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks

class RoverTest extends org.scalatest.wordspec.AnyWordSpec with Matchers with TableDrivenPropertyChecks{

  "starting position of 1, 2" in {
    val testData = Table(
      ("direction", "expected"),
      ('N',  Position (1, 3)),
      //('W',  Position (1, 3)),
      ('S',  Position (1, 1))
      //('E',  Position (1, 3)),
    )
    testData forEvery {
      (direction, expectedPostition) =>
        val rover = new Rover(Position(1, 2), direction)
        rover.move()
        rover.current shouldBe (expectedPostition, direction)
    }
  }
}
