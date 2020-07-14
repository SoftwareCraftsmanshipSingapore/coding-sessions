package orders.services

import orders.repository.OrderRepository.OrderId

trait ShipmentService {
  def ship(orderId: OrderId): Unit
}
