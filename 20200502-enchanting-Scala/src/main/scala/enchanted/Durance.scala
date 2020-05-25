package enchanted

class Durance {
  private var weapon = Weapon()

  def enchant(): Unit = weapon = MagicBook.enchant(weapon)

  def describeWeapon(): String = weapon.stats
}


