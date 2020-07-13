package orders.features.checkpoint

import orders.features.checkpoint.CheckPointRequest.Decision
import orders.repository.OrderRepository.OrderId

case class CheckPointRequest(orderId: OrderId, decision: Decision)

object CheckPointRequest {
  sealed trait Decision
  object Decision {
    case object Approve extends Decision
    case object Reject  extends Decision
  }
}
