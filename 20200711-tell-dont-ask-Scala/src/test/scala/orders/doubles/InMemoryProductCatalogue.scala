package orders.doubles

import orders.domain.Produkt
import orders.repository.ProductCatalogue

class InMemoryProductCatalogue(products: Produkt*) extends  ProductCatalogue {
  def getByName(productName: String): Option[Produkt] = products.find(_.name == productName)
}
