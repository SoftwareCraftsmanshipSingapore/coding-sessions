package shop

import model._

package object events {
  type Events = Seq[Event]

  sealed trait Event

  case class OrderPlaced            (order: Order          ) extends Event
  case class PaymentAccepted        (receipt: Receipt      ) extends Event
  case class OrderCollected         (products: Seq[Product]) extends Event
  case class OrderPlacementRejected (msg: String           ) extends Event
  case class OrderPaymentRejected   (msg: String           ) extends Event
  case class OrderCollectionRejected(msg: String           ) extends Event
}
