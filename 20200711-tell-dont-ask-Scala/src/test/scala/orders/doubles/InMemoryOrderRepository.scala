package orders.doubles

import orders.domain.Order
import orders.repository.OrderRepository

import scala.collection.mutable

class InMemoryOrderRepository extends OrderRepository {
  private var orderId = 0
  private val orders = mutable.Map.empty[Int, Order]

  override def getById(orderId: Int): Option[Order] = orders.get(orderId)
  def addOrder(order: Order): Int = {
    orders.update(orderId, order)
    orderId += 1
    orderId
  }

  override def updateOrder(orderId: Int)(order: Order): Unit = orders.update(orderId, order)
}
