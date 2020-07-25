package orders.features.creation

import orders.repository.OrderRepository.OrderId
import orders.repository.{OrderRepository, ProductCatalogue}

class OrderFactory(productCatalogue: ProductCatalogue, orderRepository: OrderRepository) {
  def make(purchaseRequest: PurchaseRequest): Either[String, OrderId] =
    purchaseRequest.createOrder(productCatalogue) map orderRepository.addOrder
}
