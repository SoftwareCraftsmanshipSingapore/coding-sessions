package enchanted

import org.scalatest.matchers.should.Matchers
import org.scalatest.freespec.AnyFreeSpec

class DuranceTest extends AnyFreeSpec with Matchers {
  "Durance" - {
    "not enchant the weapon about 10%" in {
      val numberOfRuns = 1000000
      val durance = new Durance
      val notEnchantedCount = Iterator.continually {
        durance.enchant()
        durance.describeWeapon()
      }.take(
        numberOfRuns
      ).count(
        _.startsWith("\nDagger")
      )

      (notEnchantedCount.toDouble / numberOfRuns.toDouble) shouldEqual 0.1 +- 0.002
    }
    "enchant the weapon equally with all available enchantments" - {
      val numberOfRuns = 1000000
      val durance = new Durance
      val enchantmentCounts = Iterator.continually {
        durance.enchant()
        durance.describeWeapon()
      }.take(
        numberOfRuns
      ).foldLeft(MagicBook.enchantments.map(_.prefix -> 0).toMap) {
        case (a, s) =>
          val key = a.keys.find(k => s.startsWith(s"\n$k"))
          key.map(k => a + (k -> (a(k) + 1))).getOrElse(a)
      }
      val total = enchantmentCounts.values.sum
      enchantmentCounts.map {
        case (name, count) =>
          s"$name" in {
            (count.toDouble / total.toDouble) shouldEqual 0.2 +- 0.001
          }
      }
    }
  }
}
