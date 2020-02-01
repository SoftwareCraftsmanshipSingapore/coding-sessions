package shop

import events._
import commands._
import model._
import handlers._

trait CS { //TODO: better name
  type Receive = PartialFunction[Command, Events]
  type Update = PartialFunction[Event, CoffeeShop]

  def receive: Receive = {
    case c if false => sys.error(s"unexpected command: $c")
  }

  def update: Update = {
    case e if false => sys.error(s"unexpected event: $e")
  }
}
case class CoffeeShop(
  orders   : Seq[Order],
  receipts : Seq[Receipt],
  inventory: Map[Product, Int]
) extends CS with
  OrderPlacement with
  PaymentAcceptance with
  OrderCollection

object CoffeeShop {

  def empty = CoffeeShop(orders = Seq.empty, receipts = Seq.empty, inventory = Map.empty)

  def initialise (events: Seq[Event] = Nil): CoffeeShop =
    updateAll(empty, events)

  def updateAll(coffeeShop: CoffeeShop, events: Events): CoffeeShop =
    events.foldLeft(coffeeShop) {
      case (cs, e) => cs.update(e) //TODO: update is partial function
    }
}
