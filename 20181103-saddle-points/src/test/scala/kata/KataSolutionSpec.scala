package kata

import org.scalatest._

class KataSolutionSpec  extends FlatSpec with Matchers {


  "The empty matrix" should "contain no saddle points" in {
     val solution = MyKataSolution.saddlePoints(Array.empty)
     solution shouldBe Nil
  }

  "A first matrix column in first column" should "return the whole column of points" in {
    val matrix = Array.ofDim[Int](5, 5)
    for(i <- 0 until 5) matrix(i)(0) = 1
    matrix.foreach(r => println(r.mkString(",")))
    val solution = MyKataSolution.saddlePoints(matrix)
    solution shouldBe List(P(0,0), P(1,0), P(2,0), P(3,0), P(4,0))
  }

  "Given a row of all 0s the max method" should "return all the cells in the array" in {
    val res = MyKataSolution.rowMax(0, Array(0,0,0,0,0))
    res shouldBe List(P(0,0), P(0,1), P(0,2), P(0,3), P(0,4))
  }

  "Given a row of one max number to max method" should "return the only cell" in {
    val res = MyKataSolution.rowMax(0, Array(0,1,0,0,0))
    res shouldBe List(P(0,1))
  }

  "Given a row of two max number to max method" should "return the cell of the maximum numbers" in {
    val res = MyKataSolution.rowMax(0, Array(0,1,0,1,0))
    res shouldBe List(P(0,1), P(0,3))
  }

  "Given a column of all 0s to the min method" should "return all the cells in the column" in {
    val res = MyKataSolution.colMin(0, Array(0,0,0,0,0))
    res shouldBe List(P(0,0), P(1,0), P(2,0), P(3,0), P(4,0))
  }

  "Given the column of one min number to min method" should "return the only min cell" in {
    val res = MyKataSolution.colMin(0, Array(0,1,1,1,1))
    res shouldBe List(P(0,0))
  }

  "Given the column of two min numbers to a min method" should "return the two min cells" in {
    val res = MyKataSolution.colMin(0, Array(0,0,1,1,1))
    res shouldBe List(P(0,0), P(1, 0))
  }

}
