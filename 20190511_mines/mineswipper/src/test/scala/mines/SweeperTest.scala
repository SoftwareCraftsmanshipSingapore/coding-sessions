package mines

import mines.SquareBoard.{Cells, RevealedCells}
import org.scalatest.{FlatSpec, Matchers}

import scala.language.implicitConversions

class SweeperTest extends FlatSpec with Matchers {
  import SweeperTest._

  "zero case" should "reveal the whole board with every cell showing 0 mines around it" in {
    val board = List(
       ".."
      ,".."
    )
    val revealed = List(
       "00"
      ,"00"
    )
    implicit val size: Int = 2
    new SquareBoard(board).reveal(Cell(2, 2)).asText shouldBe revealed
  }

  "single mine" should "reveal only the clicked cell" in {
    val board = List(
       "X."
      ,".."
    )
    val revealed = List(
       "??"
      ,"?1"
    )
    implicit val size: Int = 2
    new SquareBoard(board).reveal(Cell(2, 2)).asText shouldBe revealed
  }
  "3x3 with single mine clicked away from mine" should "reveal ..." in {
    val board = List(
       "X.."
      ,"..."
      ,"..."
    )
    val revealed = List(
       "?10"
      ,"110"
      ,"000"
    )
    implicit val size: Int = 3
    new SquareBoard(board).reveal(Cell(3, 3)).asText shouldBe revealed
  }
  "3x3 with single mine clicked next to a mine" should "reveal ..." in {
    val board = List(
       "X.."
      ,"..."
      ,"..."
    )
    val revealed = List(
       "???"
      ,"?1?"
      ,"???"
    )
    implicit val s`ize: Int = 3
    new SquareBoard(board).reveal(Cell(2, 2)).asText shouldBe revealed
  }

  "3x3 with two mines clicked away from mine" should "reveal ..." in {
    val board = List(
       "XX."
      ,"..."
      ,"..."
    )
    val revealed = List(
       "??1"
      ,"221"
      ,"000"
    )
    implicit val size: Int = 3
    new SquareBoard(board).reveal(Cell(3, 3)).asText shouldBe revealed
  }
  "3x3 with two non-adjacent mines clicked away from mine" should "reveal ..." in {
    val board = List(
       "X.."
      ,"..."
      ,"..X"
    )
    val revealed = List(
       "?10"
      ,"121"
      ,"01?"
    )
    implicit val size: Int = 3
    new SquareBoard(board).reveal(Cell(1, 3)).asText shouldBe revealed
  }
  "4x4 with two non-adjacent mines clicked away from mine" should "reveal ..." in {
    val board = List(
       "X..."
      ,"...."
      ,"...."
      ,"...X"
    )
    val revealed = List(
       "?100"
      ,"1100"
      ,"0011"
      ,"001?"
    )
    implicit val size: Int = 4
    new SquareBoard(board).reveal(Cell(1, 3)).asText shouldBe revealed
  }

}

object SweeperTest {
  implicit def ListOfString2SetOfCells(ss: List[String]): Cells = {
    val cells = for {
      (r, y) <- ss.zipWithIndex
      (c, x) <- r.zipWithIndex
      if c == 'X'
    } yield Cell(x + 1, y + 1)

    cells.toSet
  }
  case class Dimensions(x: Int, y: Int)

  implicit class CellsOps(val cs: RevealedCells) extends AnyVal {
    def asText(implicit size: Int): List[String] = {
      val a = Array.fill(size, size)('?')
      cs.foreach {
        case (Cell(x, y), mineCount) => a(y-1)(x-1) = s"$mineCount".head
      }
      a.map(_.mkString).toList
    }
  }
}







