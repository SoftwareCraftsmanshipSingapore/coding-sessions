package enchanted

trait Weapon {
  def stats: String =
    s"""
       |$name
       |${attributes.mkString("\n")}""".stripMargin
  def name: String
  def attributes: List[Attribute]
}

object Weapon {
  case object PlainWeapon extends Weapon {
    override val name: String = "Dagger of the Nooblet"
    override val attributes: List[Attribute] = List(
      Attribute("5 - 10", "attach damage"),
      Attribute("1.2", "attach speed")
    )
  }
  case class EnchantedWeapon(enchantment: Enchantment) extends Weapon {
    override def name: String = s"${enchantment.prefix} ${PlainWeapon.name}"
    override def attributes: List[Attribute] = PlainWeapon.attributes ++ enchantment.attributes
  }
}

