package orders.features.creation

import orders.features.creation.PurchaseRequest.Item

case class PurchaseRequest(currency: String, items: Item*)

object PurchaseRequest {
  type PurchaseRequestItems = Seq[Item]
  case class Item(productName: String, quantity: Int)
}
