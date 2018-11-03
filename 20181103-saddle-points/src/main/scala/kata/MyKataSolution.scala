package kata

case class P(y: Int, x: Int)

object MyKataSolution {

  def saddlePoints(matrix: Array[Array[Int]]): List[P]  = {
    if(matrix.isEmpty) Nil
    else {
      val allMaxPoints: Array[P] = matrix.zipWithIndex.flatMap{case (r, i) => rowMax(i, r)}
      val allMinPoints: Seq[P] = (0 until 5).flatMap(c =>  colMin(c, matrix.map(_(c))))

      allMaxPoints.filter(p => allMinPoints.contains(p)).toList

//      List(P(0,0), P(1,0), P(2,0), P(3,0), P(4,0))
    }

  }

  def rowMax(rowNum: Int, row: Array[Int]): List[P] = {
    val max= row.max
    row.zipWithIndex.filter(e => e._1 == max).map(e => P(rowNum, e._2)).toList
  }

  def colMin(colNum: Int, col: Array[Int]): List[P] = {
    val min = col.min
    col.zipWithIndex.filter(e => e._1 == min).map(e => P(e._2, colNum)).toList
  }

}
