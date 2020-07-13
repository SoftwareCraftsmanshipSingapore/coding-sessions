package orders.domain

import orders.domain.Order.{GrossAmount, OrderItems, Status, Tax}

import scala.math.BigDecimal.RoundingMode

case class Order(status: Status, currency: String, items: OrderItems, total: GrossAmount, totalTax: Tax) {
  def approve(): Either[String, Order] = status match {
    case Order.Status.Created => Right(copy(status = Order.Status.Approved))
    case invalid              => Left (s"it is already $invalid")
  }
}

object Order {
  type OrderItems = Seq[Item]
  def apply(status: Status, currency: String)(items: OrderItems): Order = {
    val totalAmount = items.foldLeft(GrossAmount.ZERO)(_ + _.grossAmount)
    val totalTax = items.foldLeft(Tax.ZERO)(_ + _.tax)
    Order(status, currency, items, totalAmount, totalTax)
  }

  case class Item(product: Produkt, quantity: Quantity, grossAmount: GrossAmount, tax: Tax)
  object Item {
    def apply(product: Produkt, quantity: Int): Item = {
      val q = Quantity(quantity)
      Item(product, q, product.grossAmount * q, product.taxAmount * q)
    }
  }
  case class Quantity(underlying: BigDecimal) extends AnyVal

  case class GrossAmount private (underlying: BigDecimal) extends AnyVal {
    def + (other:GrossAmount): GrossAmount = GrossAmount(underlying + other.underlying)
    def * (quantity: Quantity): GrossAmount = GrossAmount(underlying * quantity.underlying)
  }
  object GrossAmount {
    val ZERO = new GrossAmount(0)
    def apply(underlying: BigDecimal): GrossAmount = new GrossAmount(underlying.setScale(2, RoundingMode.HALF_UP))
    def apply(underlying: String): GrossAmount = apply(BigDecimal(underlying))
  }

  case class Tax private (underlying: BigDecimal) extends AnyVal {
    def + (other: Tax): Tax = new Tax(underlying + other.underlying)
    def * (quantity: Quantity): Tax = Tax(underlying * quantity.underlying)
  }
  object Tax {
    val ZERO = new Tax(0)
    def apply(underlying: BigDecimal): Tax = new Tax(underlying.setScale(2, RoundingMode.HALF_UP))
    def apply(underlying: String): Tax = apply(BigDecimal(underlying))
  }

  sealed trait Status
  object Status {
    case object Created  extends Status
    case object Approved extends Status
    case object Rejected extends Status
    case object Shipped  extends Status
  }
}
