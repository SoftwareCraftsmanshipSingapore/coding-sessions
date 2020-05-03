package enchanted

case class Enchantment(name: String, prefix: String, attributes: Attribute*)

case class Attribute(value: String, description: String) {
  override def toString: String = s"$value $description"
}
