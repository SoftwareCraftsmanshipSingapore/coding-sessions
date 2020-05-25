package enchanted

import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers

import scala.util.Random

class MagicBookTest extends AnyFreeSpec with Matchers {
  "MagicBook" - {
    "should not contain duplicate enchantments" in {
      Iterator.iterate(
        Weapon(3)
      ) (
        MagicBook.enchant
      ).take(
        1000
      ).foreach {
        w => w.enchantments shouldBe w.enchantments.distinct
      }
    }
    "should not enchant a weapon with the same enchantment twice in a row" in {
      Iterator.iterate(
        Weapon(Random.nextInt(2) + 1)
      ) (
        MagicBook.enchant
      ).take(
        1000000
      ).sliding(
        2, 1
      ).foreach {
        case w1 +: w2 +: Nil => w1.lastEnchantment should not be w2.lastEnchantment
      }
    }
  }
}
