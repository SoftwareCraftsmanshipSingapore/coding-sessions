package orders.doubles

import orders.domain.Produkt
import orders.repository.ProductCatalogue

class InMemoryProductCatalogue(products: Produkt*) extends  ProductCatalogue {
  def getByName(productName: String): Either[String, Produkt] =
    products.find(_.name == productName).toRight(s"""product error: "$productName" not in catalogue""")
}
