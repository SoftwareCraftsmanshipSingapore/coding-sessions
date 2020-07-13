package orders.features.checkpoint

import orders.features.checkpoint.CheckPointRequest.Decision

case class CheckPointRequest(orderId: Int, decision: Decision)

object CheckPointRequest {
  sealed trait Decision
  object Decision {
    case object Approve extends Decision
    case object Reject  extends Decision
  }
}
