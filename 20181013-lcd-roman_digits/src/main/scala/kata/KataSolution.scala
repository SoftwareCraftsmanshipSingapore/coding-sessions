package kata

import scala.collection.immutable.ListMap
import scala.collection.mutable


class MyKataSolution {

  def getSolution(number: String) : String = {

   val zero = Array(
    "._.",
     "|.|",
     "|_|")

    val one = Array(
      "...",
      "..|",
      "..|"
    )
    val digits = Map('0' -> zero, '1' -> one)

    val selectedNumbers = number.map(digits)

    (0 to 2)
      .map(r => selectedNumbers.map(_(r)))
      .map(_.mkString(" ")).mkString("\n")

  }

  val romans = ListMap(10 -> "X", 5 -> "V", 1 -> "I")

  case class R(rs: String*)
  val ps = Map(
    0 -> R(""    , ""    , ""    , ""),
    1 -> R("I"   , "X"   , "C"   , "M"),
    2 -> R("II"  , "XX"  , "CC"  , "MM"),
    3 -> R("III" , "XXX" , "CCC" , "MMM"),
    4 -> R("IV"  , "XL"  , "CD"     ),
    5 -> R("V"   , "L"   , "D"    ),
    6 -> R("VI"  , "LX"  , "DC"   ),
    7 -> R("VII" , "LXX" , "DCC"  ),
    8 -> R("VIII", "LXXX", "DCCC" ),
    9 -> R("IX"  , "XC"  , "CM")
  )

  def toRoman(d: Int): String = {
    val x = d.toString.reverse.zipWithIndex.map {
      case (n, p) => ps(n.toInt).rs(p)
    }
    x.mkString
  }
  val digits = romans.map { case (k, v) => v -> k}

  def digitalToRoman (number: Int ): String = {
/*
    number.toString.zipWithIndex.map{
      case (n, p) =>  n match{
        case d@(1 | 2 | 3) => "I" * d
        case 4 => "IV"
        case 5 => "V"
        case 6 | 7 | 8 => ???
        case 9  => "IX"
      }
    }
*/
    ???
  }

  def romanToDigital(romanDigits: String): Int = {
    digits(romanDigits)
  }

}
