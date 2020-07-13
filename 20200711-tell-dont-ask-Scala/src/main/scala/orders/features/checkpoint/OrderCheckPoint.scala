package orders.features.checkpoint

import orders.repository.OrderRepository

class OrderCheckPoint(orderRepository: OrderRepository) {
  def applyDecision(request: CheckPointRequest): Either[String, Unit] = {
    import request._
    def error(e: String): String = s"Order [id=$orderId] cannot be approved because: $e"
    orderRepository.getById(orderId) match {
      case Some(order) => order.approve().map(orderRepository.updateOrder(orderId)).left.map(error)
      case None        => Left(error("not available in repository"))
    }
  }
}
