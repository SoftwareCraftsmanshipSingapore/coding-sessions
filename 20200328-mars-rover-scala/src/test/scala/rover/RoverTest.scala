package rover

import org.scalatest.matchers.should.Matchers

class RoverTest extends org.scalatest.wordspec.AnyWordSpec with Matchers {
  "rover at (1,2,N)" when {
    "instructed to Move" should {
      "end up at (1,3,N)" in {
        val rover = new Rover(Position(1, 2), 'N')
        rover.move()
        rover.current shouldBe (Position(1, 3), 'N')
      }
    }
  }
  "rover at (1, 2, S)" when {
    "instructed to Move" should {
      "end up at (1,1,S)" in {

      }
    }
  }
}
