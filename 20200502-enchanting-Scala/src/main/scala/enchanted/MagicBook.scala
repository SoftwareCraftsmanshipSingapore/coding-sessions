package enchanted

import scala.util.Random

case object MagicBook {
  private type Enchantments = List[Enchantment]
  val enchantments: Enchantments = List(
     Enchantment("Ice"      , "Icy"    , Attribute("+5", "ice damage"))
    ,Enchantment("Fire"     , "Inferno", Attribute("+5", "fire damage"))
    ,Enchantment("Lifesteal", "Vampire", Attribute("+5", "lifesteal"))
    ,Enchantment("Agility"  , "Quick"  , Attribute("+5", "agility"))
    ,Enchantment("Strength" , "Angry"  , Attribute("+5", "strength"))
  )

  def nextExcept(existing: Enchantment): Enchantment = pickOne(enchantments.filterNot(_ == existing))
  def next(): Enchantment = pickOne(enchantments)

  private def pickOne(enchantments: Enchantments): Enchantment = Random.shuffle(enchantments).head
}
