package shop.handlers

import shop._
import implicits._
import model._
import events._
import commands._

trait OrderCollection extends CS {
  self: CoffeeShop =>

  override def receive: Receive = super.receive orElse {
    case CollectOrder(orderNumber) =>  collectOrder(orderNumber)
  }

  private def collectOrder(orderNumber: OrderNumber): Events =
    orders.find(_.number == orderNumber) match {
      case Some(order) => OrderCollected(order.products)
      case None        => OrderCollectionRejected(s"Order $orderNumber not found!")
    }

  override def update: Update = super.update orElse {
    case OrderCollected(products)   => copy(inventory = inventory -- products)
    case _: OrderCollectionRejected => this
  }

}
