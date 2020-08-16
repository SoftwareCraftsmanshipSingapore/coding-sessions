package orders.features.creation

import orders.domain.Order
import orders.domain.Order.OrderItems
import orders.features.creation.PurchaseRequest.{Item, PurchaseRequestItems}
import orders.repository.ProductCatalogue

case class PurchaseRequest(currency: String, items: Item*) {
  def createOrder(productCatalogue: ProductCatalogue): Either[String, Order] = {
    @scala.annotation.tailrec
    def getOrderItems(items: PurchaseRequestItems, orderItems: OrderItems = Nil): Either[String, OrderItems] =
      items match {
        case Nil     => if (orderItems.isEmpty) Left("No items requested") else Right(orderItems)
        case i +: is => productCatalogue.getByName(i.productName) match {
          case Right(product) => getOrderItems(is, orderItems :+ Order.Item(product, i.quantity))
          case Left(left)     => Left(left)
        }
      }
    getOrderItems(items) map Order.mkNew(currency)
  }
}

object PurchaseRequest {
  type PurchaseRequestItems = Seq[Item]
  case class Item(productName: String, quantity: Int)
}
