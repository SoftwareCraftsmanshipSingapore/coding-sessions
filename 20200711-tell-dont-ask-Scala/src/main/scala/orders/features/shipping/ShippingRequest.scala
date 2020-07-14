package orders.features.shipping

import orders.repository.OrderRepository.OrderId

case class ShippingRequest(orderId: OrderId)
