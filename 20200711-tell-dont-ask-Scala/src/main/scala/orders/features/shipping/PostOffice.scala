package orders.features.shipping

import orders.repository.OrderRepository
import orders.services.ShipmentService

class PostOffice(orderRepository: OrderRepository, shipmentService: ShipmentService) {
  def ship(shippingRequest: ShippingRequest): Unit = {
    import shippingRequest.orderId
    orderRepository
      .updateWith(orderId)(_.ship())
      .map(_ => shipmentService.ship(orderId))
      .left.map(e => Left(s"Order [id=$orderId] cannot be shipped because: $e"))
  }
}
