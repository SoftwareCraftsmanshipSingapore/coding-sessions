package orders.doubles

import orders.domain.Order
import orders.repository.OrderRepository
import orders.repository.OrderRepository.OrderId

import scala.collection.mutable

class InMemoryOrderRepository extends OrderRepository {
  private var orderId = OrderId(-1)
  private val orders = mutable.Map.empty[OrderId, Order]

  override def getById(orderId: OrderId): Option[Order] = orders.get(orderId)
  def addOrder(order: Order): OrderId = {
    orderId = orderId.inc
    orders.update(orderId, order)
    orderId
  }

  override def updateWith(orderId: OrderId)(update: Order => Either[String, Order]): Either[String, Unit] =
    orders.get(orderId).map(update(_).map(orders.update(orderId, _))).getOrElse(Left("not available in repository"))
}
