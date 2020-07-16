package orders.repository

import orders.domain.Produkt

trait ProductCatalogue {
  def getByName(productName: String): Either[String, Produkt]
}
