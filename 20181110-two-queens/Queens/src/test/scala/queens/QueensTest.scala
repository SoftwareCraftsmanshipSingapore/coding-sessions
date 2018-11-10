package queens

import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.{FlatSpec, Matchers}

class QueensTest extends FlatSpec with Matchers with TableDrivenPropertyChecks{

  "it" should "determine if two queens are attacking each other" in {
    Table(
      ("queen1", "queen2", "is attacking?")
      ,("a1"   , "c2"    , false)
      ,("a1"   , "a2"    , true)
      ,("a1"   , "h1"    , true)
      ,("a1"   , "b2"    , true)
      ,("e4"   , "g6"    , true)
    ) forEvery {
      case (queen1, queen2, isAttacking) =>
        mkBoard(queen1, queen2).collision shouldBe isAttacking
    }
  }

  it should "translate coordinate" in {
    Table(
      ("Chess coordinate", "Matrix Index")
        ,("a1", 0 -> 0)
        ,("h1", 7 -> 0)
        ,("h8", 7 -> 7)
        ,("a8", 0 -> 7)
    ).forEvery{
      case (cc, mi) =>
        Board.translate(cc) shouldBe mi
    }
  }

  "Placing the queen on the cell" should "return it from the cell" in  {
    val board = new Board
    board.place("a1")
    board.atLocation("a1") shouldBe 1
    board.atLocation("a2") shouldBe 0
  }

  def mkBoard(qs: String*): Board  = qs.foldLeft(new Board)(_.place(_))

}