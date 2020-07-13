package orders.features.creation

import orders.domain.Order
import orders.domain.Order.OrderItems
import orders.features.creation.PurchaseRequest.PurchaseRequestItems
import orders.repository.ProductCatalogue

class OrderFactory(productCatalogue: ProductCatalogue) {
  def make(purchaseRequest: PurchaseRequest): Either[String, Order] = {
    @scala.annotation.tailrec
    def getOrderItems(items: PurchaseRequestItems, orderItems: OrderItems = Nil): Either[String, OrderItems] = items match {
      case Nil => if (orderItems.isEmpty) Left("No items requested") else Right(orderItems)
      case i+:is => productCatalogue.getByName(i.productName) match {
        case Some(product) => getOrderItems(is, orderItems :+ Order.Item(product, i.quantity))
        case None          => Left(s"""product error: "${i.productName}" not in catalogue""")
      }
    }

    getOrderItems(purchaseRequest.items) map Order(Order.Status.Created, purchaseRequest.currency)
  }
}
