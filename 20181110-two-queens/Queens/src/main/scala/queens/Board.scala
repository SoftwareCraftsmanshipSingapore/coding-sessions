package queens

import queens.Board.Position

class Board (){

  val board = Array.ofDim[Int](8,8)

  def collision :Boolean = {
    val q1::q2::Nil = getQueens
    q1.isAttacking(q2)
  }

  def place(location: String): Board = {
    put (Board.translate(location))
    this
  }

  def atLocation(location: String): Int = {
    get(Board.translate(location))
  }

  private def getQueens : List[Queen] = for {
    (row, ri) <- board.toList.zipWithIndex
    (value, ci) <- row.zipWithIndex if value == 1
  } yield Queen(ri, ci)

  private def put(t: Position): Unit = t match {
    case (x,y) => board(x)(y) = 1
  }

  private def get(t: Position): Int = t match {
    case (x,y) => board(x)(y)
  }

}

case class Queen(x: Int, y: Int) {
  private def sameColumn(o: Queen): Boolean = y == o.y
  private def sameRow(o: Queen): Boolean = x == o.x
  private def sameDiagonal(o: Queen): Boolean = math.abs(x - o.x) == math.abs(y - o.y)
  def isAttacking(o: Queen): Boolean = sameRow(o) || sameColumn(o) || sameDiagonal(o)
}

object Board{
  type Position = (Int, Int)
  def translate(loc: String) : Position = {
    val x = loc(0).toInt - 'a'.toInt
    val y = loc(1).asDigit - 1
    x -> y
  }

}
