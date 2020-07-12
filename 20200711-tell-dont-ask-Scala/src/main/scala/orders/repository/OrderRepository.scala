package orders.repository

import orders.domain.Order

trait OrderRepository {
  def addOrder(order: Order): Int
  def getById(orderId: Int): Option[Order]
}
