package rover

import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks

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
class SquadronTest  extends org.scalatest.wordspec.AnyWordSpec with Matchers with TableDrivenPropertyChecks {

  "a squadron" when {
    "receiving instructions" should {
      "parse them" in {
        val squadron = new Squadron
        val instructions =
          """
            |5 5
            |
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
        squadron.receive(instructions)
        squadron.execute()

        squadron.plateau shouldBe Plateau(5, 5)
        squadron.result shouldBe expectedOutput
      }
    }
  }
}
