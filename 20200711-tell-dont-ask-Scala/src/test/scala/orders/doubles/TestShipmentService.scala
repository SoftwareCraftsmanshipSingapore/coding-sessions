package orders.doubles

import orders.services.ShipmentService

import scala.collection.mutable

class TestShipmentService() extends ShipmentService {
  private val shippedOrders = mutable.ArrayBuffer.empty[Int]
  override def ship(orderId: Int): Unit = shippedOrders.addOne(orderId)
  def isShipped(orderId: Int): Boolean = shippedOrders.contains(orderId)
}
