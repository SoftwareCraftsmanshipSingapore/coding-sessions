package orders.features.shipping

import orders.repository.OrderRepository
import orders.services.ShipmentService

class PostOffice(orderRepository: OrderRepository, shipmentService: ShipmentService) {
  def ship(shippingRequest: ShippingRequest): Either[String, Unit] = {
    import shippingRequest.orderId
    val result = for {
      order        <- orderRepository.getById(orderId)
      shippedOrder <- order.ship()
      _            <- shipmentService.ship(orderId)
    } yield orderRepository.update(orderId)(shippedOrder)
    result.left.map(e => s"Order [id=$orderId] cannot be shipped because: $e")
  }
}
