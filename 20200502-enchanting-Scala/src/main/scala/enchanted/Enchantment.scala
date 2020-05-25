package enchanted

case class Enchantment(name: String, prefix: String, attribute: Attribute.Value)

sealed trait Attribute

object Attribute {
  case class Damage(min: BigDecimal, max: BigDecimal, description: String) extends Attribute {
    override def toString: String = s"$min - $max $description"
    def average:BigDecimal = (min + max) / 2
  }
  case class Speed(value: BigDecimal, description: String) extends Attribute {
    override def toString: String = s"$value $description"
  }
  sealed trait Value extends Attribute {
    def value: BigDecimal
  }
  object Value {
    case class DamageBoost(value: BigDecimal, description: String) extends Value {
      override def toString: String = s"+$value $description damage"
    }
    case class LifeSteam(value: BigDecimal) extends Value {
      override def toString: String = s"+$value lifesteal"
    }

    case class Agility  (value: BigDecimal) extends Value {
      override def toString: String = s"+$value agility"
    }
    case class Strength (value: BigDecimal) extends Value {
      override def toString: String = s"+$value strength"
    }
  }
}
