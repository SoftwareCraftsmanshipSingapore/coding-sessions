package shop

import model._

package object commands {
  sealed trait Command

  case class PlaceOrder   (products: Seq[Product]                )extends Command
  case class AcceptPayment(orderNumber: OrderNumber, money: Money)extends Command
  case class CollectOrder (orderNumber: OrderNumber              )extends Command
}
