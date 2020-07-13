package orders.data

import orders.domain.Produkt
import orders.domain.Produkt.{Price, TaxPercentage}
import orders.doubles.InMemoryProductCatalogue

trait TestData {
  protected lazy val food: Produkt.Category = Produkt.Category("food", TaxPercentage("10"))
  protected lazy val salad: Produkt = Produkt("salad", Price("3.56"), food)
  protected lazy val tomato: Produkt = Produkt("tomato", Price("4.65"), food)
  protected lazy val productCatalogue = new InMemoryProductCatalogue(salad, tomato)

}
