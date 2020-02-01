package shop.handlers

import shop._
import events._
import model._
import implicits._
import commands._

trait OrderPlacement extends CS {
  self: CoffeeShop =>

  override def receive: Receive = super.receive orElse {
    case PlaceOrder(products) => placeOrder(products)
  }

  override def update: Update = super.update orElse {
    case OrderPlaced(order)        => copy(orders = orders :+ order)
    case OrderPlacementRejected(_) => self
  }

  protected def placeOrder(products: Products): Events = {
    if(inventoryAvailable(products))
      OrderPlaced(Order(1, products)) //TODO: need to generate order number
    else
      OrderPlacementRejected("No Stock!")
  }

  private def inventoryAvailable(products: Seq[Product]): Boolean =
    products.toSet.subsetOf(inventory.keySet)

}
