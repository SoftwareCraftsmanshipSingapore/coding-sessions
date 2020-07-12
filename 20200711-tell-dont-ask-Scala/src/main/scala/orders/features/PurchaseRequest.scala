package orders.features

import orders.features.PurchaseRequest.Item

case class PurchaseRequest(currency: String, items: Item*)

object PurchaseRequest {
  type PurchaseRequestItems = Seq[Item]
  case class Item(productName: String, quantity: Int)
}
