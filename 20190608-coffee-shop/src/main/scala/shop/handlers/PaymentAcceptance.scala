package shop.handlers

import shop._
import commands._
import events._
import implicits._
import model._

trait PaymentAcceptance extends CS {
  self: CoffeeShop =>

  override def receive: Receive = self.receive orElse {
    case AcceptPayment(orderNumber, money) => acceptPayment(orderNumber, money)
  }

  private def acceptPayment(orderNumber: OrderNumber, money: Money): Events =
    if (orders.exists(_.number == orderNumber))
      PaymentAccepted(Receipt(orderNumber, money))
    else
      OrderPaymentRejected(s"Order $orderNumber not found!")

  override def update: Update = self.update orElse {
    case PaymentAccepted(receipt) => copy(receipts = receipts :+ receipt)
    case _: OrderPaymentRejected  => self
  }
}
