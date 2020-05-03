package enchanted

import enchanted.Weapon.{EnchantedWeapon, PlainWeapon}

import scala.util.Random

class Durance {
  private var weapon:Weapon = PlainWeapon

  def enchant(): Unit = {
    if (Random.nextInt(99) < 10) {
      weapon = PlainWeapon
    } else {
      val enchantment = weapon match {
        case PlainWeapon                  => MagicBook.next()
        case EnchantedWeapon(enchantment) => MagicBook.nextExcept(enchantment)
      }
      weapon = EnchantedWeapon(enchantment)
    }
  }

  def describeWeapon(): String = weapon.stats
}


