package kata

import org.scalatest._

class KataSolutionSpec  extends FlatSpec with Matchers {

  val mySolutionSpec = new MyKataSolution()

  lazy val zero: String =
    """._.
      ||.|
      ||_|""".stripMargin

  lazy val one: String =
    """...
      |..|
      |..|""".stripMargin.trim

  val zeroOne : String =
    """._. ...
      ||.| ..|
      ||_| ..|""".stripMargin.trim



  "The MyKataSolution" should "return a 0 correctly" in {
     val res1 = mySolutionSpec.getSolution("0")
     val res2 = mySolutionSpec.getSolution("1")
     res1 shouldEqual zero
     res2 shouldEqual one
  }

  "The solution" should "be able to print 01" in {
    val res1 = mySolutionSpec.getSolution("01")
    println(res1)
    res1 shouldEqual zeroOne
  }


  "The solution" should "provide the roman number" in {
    for(i <- Range(1, 10)){
      mySolutionSpec.romanToDigital(mySolutionSpec.digitalToRoman(i)) shouldEqual i
    }

    "it" should "work" in {
      mySolutionSpec.toRoman(1) shouldBe "I"
      mySolutionSpec.toRoman(10) shouldBe "X"
      mySolutionSpec.toRoman(101) shouldBe "CI"
      mySolutionSpec.toRoman(11) shouldBe "XI"
      mySolutionSpec.toRoman(87) shouldBe "LXXXVII"
      mySolutionSpec.toRoman(49) shouldBe "XLIX"
      mySolutionSpec.toRoman(999) shouldBe "CMXCIX"
      mySolutionSpec.toRoman(1000) shouldBe "M"
      mySolutionSpec.toRoman(1010) shouldBe "MX"
      mySolutionSpec.toRoman(2020) shouldBe "MMXX"

    }
//
//    val res = mySolutionSpec.digitalToRoman("1")
//    val res2 = mySolutionSpec.digitalToRoman("2")
//    val res3 = mySolutionSpec.digitalToRoman("10")
//    res shouldEqual "I"
//    res2 shouldEqual "II"
//    res3 shouldEqual "X"


  }

}
