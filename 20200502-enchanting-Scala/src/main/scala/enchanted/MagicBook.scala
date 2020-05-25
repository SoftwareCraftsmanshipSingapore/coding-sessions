package enchanted

import enchanted.Weapon.{Basic, Enchanted}

import scala.util.Random

case object MagicBook {
  type Enchantments = List[Enchantment]
  val ice      : Enchantment = Enchantment("Ice"      , "Icy"    , Attribute.Value.DamageBoost(BigDecimal("+5"), "ice"))
  val fire     : Enchantment = Enchantment("Fire"     , "Inferno", Attribute.Value.DamageBoost(BigDecimal("+5"), "fire"))
  val lifesteal: Enchantment = Enchantment("Lifesteal", "Vampire", Attribute.Value.LifeSteam  (BigDecimal("+5")))
  val agility  : Enchantment = Enchantment("Agility"  , "Quick"  , Attribute.Value.Agility    (BigDecimal("+5")))
  val strength : Enchantment = Enchantment("Strength" , "Angry"  , Attribute.Value.Strength   (BigDecimal("+5")))

  val enchantments: Enchantments = List(ice, fire, lifesteal, agility, strength)

  def enchant(weapon: Weapon): Weapon = {
    def pickOne(es: Enchantments) = Random.shuffle(es).headOption
    def doEnchant(w: Weapon, e: Option[Enchantment]) = e.map(w.enchant).getOrElse(w)
    weapon match {
      case w: Enchanted if Random.nextInt(99) < 10 => w.remove()
      case w: Enchanted                            => doEnchant(w,pickOne(enchantments.filterNot(w.contains)))
      case w: Basic                                => doEnchant(w,pickOne(enchantments))
      case w                                             => w
    }
  }

}
