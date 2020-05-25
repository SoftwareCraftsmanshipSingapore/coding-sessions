package enchanted

import enchanted.MagicBook._
import enchanted.Weapon.{Basic, Enchanted}
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class WeaponTest extends AnyFreeSpec with ScalaCheckPropertyChecks with Matchers {

  "DPS of a basic weapon" - {
    Table(
       ("enchantments",                           "dps")
      ,(Seq.empty     ,                           "09.00")
      ,(Seq(ice)      ,                           "12.00")
      ,(Seq(fire)     ,                           "12.00")
      ,(Seq(agility)  ,                           "12.75")
      ,(Seq(ice       , agility)   ,              "17.00")
      ,(Seq(fire      , agility)   ,              "17.00")
      ,(Seq(ice       , fire       , agility  ) , "21.25")
    ).forEvery{
      (enchantments, dps) =>
        val enchantmentsCombinations = Set(lifesteal, strength)
          .subsets()
          .filter(_.size <= 3 - enchantments.size)
          .map(es => enchantments ++ es)
        enchantmentsCombinations.foreach {
          es =>
            s"${es.map(_.name).mkString("with ",", ", "")} should be $dps" in {
              val weapon = if(es.isEmpty) Basic(0) else Enchanted(es)
              weapon.dps shouldBe BigDecimal(dps)
            }
        }
    }
  }
}
