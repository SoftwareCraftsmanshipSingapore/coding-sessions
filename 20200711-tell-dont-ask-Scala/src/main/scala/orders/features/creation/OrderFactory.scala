package orders.features.creation

import orders.domain.Order
import orders.domain.Order.OrderItems
import orders.features.creation.PurchaseRequest.PurchaseRequestItems
import orders.repository.OrderRepository.OrderId
import orders.repository.{OrderRepository, ProductCatalogue}

class OrderFactory(productCatalogue: ProductCatalogue, orderRepository: OrderRepository) {
  def make(purchaseRequest: PurchaseRequest): Either[String, OrderId] = {
    @scala.annotation.tailrec
    def getOrderItems(items: PurchaseRequestItems, orderItems: OrderItems = Nil): Either[String, OrderItems] =
      items match {
        case Nil     => if (orderItems.isEmpty) Left("No items requested") else Right(orderItems)
        case i +: is => productCatalogue.getByName(i.productName) match {
          case Right(product) => getOrderItems(is, orderItems :+ Order.Item(product, i.quantity))
          case Left(left) => Left(left)
        }
      }
    import purchaseRequest._
    getOrderItems(items) map Order.mkNew(currency) map orderRepository.addOrder
  }
}
