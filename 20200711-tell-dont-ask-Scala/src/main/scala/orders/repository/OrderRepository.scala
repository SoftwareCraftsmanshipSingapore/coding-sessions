package orders.repository

import orders.domain.Order
import orders.repository.OrderRepository.OrderId

trait OrderRepository {
  def addOrder(order: Order): OrderId
  def getById(orderId: OrderId): Either[String, Order]
  def update(orderId: OrderId)(order: Order): Unit
  def updateWith(orderId: OrderId)(update: Order => Either[String, Order]): Either[String, Unit]
}

object OrderRepository {
  case class OrderId(underlying: Int) extends AnyVal {
    def inc: OrderId = copy(underlying + 1)

    override def toString: String = s"$underlying"
  }
}
