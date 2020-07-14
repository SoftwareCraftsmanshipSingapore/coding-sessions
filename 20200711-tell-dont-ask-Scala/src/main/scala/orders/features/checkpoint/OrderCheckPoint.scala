package orders.features.checkpoint

import orders.repository.OrderRepository

class OrderCheckPoint(orderRepository: OrderRepository) {
  def applyDecision(request: CheckPointRequest): Either[String, Unit] = {
    import request._
    orderRepository
      .updateWith(orderId)(_.approve())
      .left.map(e => s"Order [id=$orderId] cannot be approved because: $e")
  }
}
