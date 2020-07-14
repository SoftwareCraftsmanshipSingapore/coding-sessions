package orders.doubles

import orders.repository.OrderRepository.OrderId
import orders.services.ShipmentService

import scala.collection.mutable

class TestShipmentService extends ShipmentService {
  private val shippedOrders = mutable.ArrayBuffer.empty[OrderId]
  override def ship(orderId: OrderId): Unit = shippedOrders.addOne(orderId)
  def isShipped(orderId: OrderId): Boolean = shippedOrders.contains(orderId)
}
