package enchanted

import enchanted.Attribute.Damage
import enchanted.Attribute.Value.{Agility, DamageBoost}

trait Weapon {
  def stats: String =
    s"""
       |$name
       |${attributes.mkString("\n")}""".stripMargin
  def name: String = "Dagger of the Nooblet"
  def attributes: List[Attribute] = List(damage,speed)
  def dps: BigDecimal = {
    val averageDamage = damage.average + enchantments.collect {
      case Enchantment(_, _, d:DamageBoost) => d.value / 2
    }.sum
    val _speed = {
      speed.value + enchantments.find {
        case Enchantment(_, _, _: Agility) => true
        case _                             => false
      }.map(_.attribute.value).getOrElse(BigDecimal(0)) / BigDecimal(10)
    }
    averageDamage * _speed
  }

  def enchantments: Seq[Enchantment] = Seq.empty
  protected def damage: Attribute.Damage = Attribute.Damage(5, 10, "attach damage")
  protected def speed : Attribute.Speed  = Attribute.Speed (BigDecimal("1.2"), "attach speed")

  def enchant(enchantment: Enchantment): Weapon

  def lastEnchantment: Option[Enchantment] = None
}

object Weapon {
  def apply(max: Int = 1):Weapon = Basic(max)
  case class Basic(max: Int) extends Weapon {
    def enchant(enchantment: Enchantment): Weapon = Enchanted(Seq(enchantment), max)
  }
  sealed trait Enchanted extends Weapon {
    def max: Int
    override def name: String = s"${enchantments.lastOption.map(_.prefix).map(_ + " ").getOrElse("")}${super.name}"
    override def attributes: List[Attribute] = super.attributes ++ enchantments.map(_.attribute)
    def remove(): Weapon = if (enchantments.size > 1) Enchanted.Partially(enchantments.init, max) else Basic(max)
    def contains(e: Enchantment): Boolean = enchantments.contains(e)
    override def lastEnchantment: Option[Enchantment] = enchantments.lastOption
  }
  object Enchanted {
    def apply(enchantments: Seq[Enchantment], max: Int = 3): Enchanted = {
      require(enchantments.nonEmpty, "empty enchantments")
      require(enchantments.size <= max, s"max is $max but got ${enchantments.size}")
      (if (enchantments.size == max) Fully.apply _ else Partially.apply _)(enchantments, max)
    }
    case class Partially(override val enchantments: Seq[Enchantment], max: Int) extends Enchanted {
      def enchant(enchantment: Enchantment): Weapon = {
        if (enchantments.size + 1 == max) {
          Fully(enchantments :+ enchantment, max)
        } else {
          copy(enchantments = enchantments :+ enchantment)
        }
      }

    }
    case class Fully(override val enchantments: Seq[Enchantment], max: Int) extends Enchanted {
      def enchant(enchantment: Enchantment): Weapon = copy(enchantments = enchantments.tail :+ enchantment)
    }
  }
}

