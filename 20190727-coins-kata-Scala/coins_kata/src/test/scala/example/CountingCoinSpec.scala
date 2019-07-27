package example

import org.scalatest._
import example.CountingCoin._


class CountingCoinSpec extends FlatSpec with Matchers {
  "given 45 cents" should "give: 25, 20" in {
    val amoutnToTest = 45
    count(amoutnToTest) shouldEqual List(25, 20)
  }
  "given 40 cents" should "give: 20, 20" in {
    val amoutnToTest = 40
    reduce(Map(1 -> 40)) shouldEqual Map(20 -> 1)
  }
  "smallest coins" should "???" in {
    reduce(Map(1 -> 1, 5 -> 1)) shouldBe 1 -> 1
  }
  "1c into 5s" should "give 1c back" in {
    ones2Fives(1) shouldBe Map(1 -> 1, 5 -> 0)
  }
  "5 x 1c into 5s" should "5c" in {
    ones2Fives(5) shouldBe Map(1 -> 0, 5 -> 1)
  }
  "6 x 1c int 5s" should "1c, 5c" in {
    ones2Fives(6) shouldBe Map(1 -> 1, 5 -> 1)
  }

  "6 x 1c into 5s" should "1c, 5c" in {
    a2b(1 -> 6, 5) shouldBe Map(1 -> 1, 5 -> 1)
  }
  "5 x 20 into 25" should "4 x 25" in {
    a2b(20 -> 5, 25) shouldBe Map(20 -> 0, 25 -> 4)
  }
//  "40 1c coins" should "reduce to 8 5c pieces" in {
//    reduce(Map(1 -> 40)) shouldBe Map(5, 8)
//  }
}
