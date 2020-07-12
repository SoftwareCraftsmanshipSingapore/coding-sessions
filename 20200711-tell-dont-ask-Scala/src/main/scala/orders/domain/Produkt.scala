package orders.domain

import orders.domain.Order.{GrossAmount, Tax}
import orders.domain.Produkt.{Category, Price}

import scala.math.BigDecimal.RoundingMode

case class Produkt(name: String, price: Price, category: Category) {
  def taxAmount: Tax = price * category.taxPercentage
  def grossAmount: GrossAmount = price + taxAmount
}

object Produkt {
  case class Price private (underlying: BigDecimal) extends AnyVal {
    def + (tax: Tax): GrossAmount = GrossAmount(underlying + tax.underlying)
    def * (taxPercentage: TaxPercentage): Tax = Tax(underlying / BigDecimal(100) * taxPercentage.underlying)
  }
  object Price {
    def apply(underlying: BigDecimal): Price = new Price(underlying.setScale(2, RoundingMode.HALF_UP))
    def apply(underlying: String): Price = apply(BigDecimal(underlying))
  }

  case class Category(name: String, taxPercentage: TaxPercentage)

  case class TaxPercentage private (underlying: BigDecimal) extends AnyVal
  object TaxPercentage {
    def apply(underlying: BigDecimal): TaxPercentage = new TaxPercentage(underlying.setScale(2, RoundingMode.HALF_UP))
    def apply(underlying: String): TaxPercentage = apply(BigDecimal(underlying))
  }
}
