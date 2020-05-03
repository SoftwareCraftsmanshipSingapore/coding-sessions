package enchanted

import enchanted.Weapon.EnchantedWeapon
import org.scalatest.matchers.should.Matchers
import org.scalatest.freespec.AnyFreeSpec

class DuranceTest extends AnyFreeSpec with Matchers {
  "Durance" - {
    "should not enchant the weapon about 10%" in {
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
    "should enchant the weapon equally with all available enchantments" - {
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
    "should correctly enchant the weapon" in {
      val plain = """Dagger of the Nooblet
                    |5 - 10 attach damage
                    |1.2 attach speed""".stripMargin
      val icy = """Icy Dagger of the Nooblet
                  |5 - 10 attach damage
                  |1.2 attach speed
                  |+5 ice damage""".stripMargin
      val inferno = """Inferno Dagger of the Nooblet
                      |5 - 10 attach damage
                      |1.2 attach speed
                      |+5 fire damage""".stripMargin
      val vampire = """Vampire Dagger of the Nooblet
                      |5 - 10 attach damage
                      |1.2 attach speed
                      |+5 lifesteal""".stripMargin
      val quick = """Quick Dagger of the Nooblet
                    |5 - 10 attach damage
                    |1.2 attach speed
                    |+5 agility""".stripMargin
      val angry = """Angry Dagger of the Nooblet
                    |5 - 10 attach damage
                    |1.2 attach speed
                    |+5 strength""".stripMargin
      val expectedEnchantedWeaponStats = Set(plain, icy, inferno, vampire, quick, angry)
      val durance = new Durance
      val uniqueWeaponStats = Iterator.continually{
        durance.enchant()
        durance.describeWeapon().strip()
      }.take{
        100
      }.foldLeft(Set.empty[String])(_ + _)
      expectedEnchantedWeaponStats shouldBe uniqueWeaponStats
    }
  }
}
