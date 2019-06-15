package example

import example.CoffeeShop.{Money, OrderNumber, Products}
import example.Command.{AcceptPayment, CollectOrder, PlaceOrder}
import example.Event.{OrderCollectionRejected, OrderCollected, OrderPlaced, OrderPlacementRejected, PaymentAccepted, OrderPaymentRejected}

sealed trait Command

object Command {
  case class PlaceOrder(products: Seq[Product])extends Command
  case class AcceptPayment(orderNumber: OrderNumber, money: Money)extends Command
  case class CollectOrder(orderNumber: OrderNumber)extends Command
}

sealed trait Event
object Event {
  //Order placed
  case class OrderPlaced(order: Order) extends Event
  case class PaymentAccepted(receipt: Receipt) extends Event
  case class OrderCollected(products: Seq[Product]) extends Event
  case class OrderPlacementRejected(msg: String) extends Event
  case class OrderPaymentRejected(msg: String) extends Event
  case class OrderCollectionRejected(msg: String) extends Event
}


trait Product {
  def price : Money
}

case class Coffee(`type`: String, size: String, override val price: Money) extends Product
case class Order(number: OrderNumber, product: Product*) {
  def totalPrice : Money = product.foldLeft(BigDecimal(0))(_ + _.price)
}

case class Receipt(orderNumber: OrderNumber)

case class CoffeeShop(orders: Seq[Order], receipts: Seq[Receipt], inventory: Map[Product, Int]){

  //Seq cause could create multiple changes
  def receive(command: Command): Seq[Event] = command match{
    case PlaceOrder(products) =>
      if(inventoryAvailable(products)) {
        val order = Order(1, products: _*)
        Seq(OrderPlaced(order))
      }else{
        Seq(OrderPlacementRejected("No Stock!"))
      }

    case AcceptPayment(orderNumber, money) =>
     Seq(OrderPaymentRejected(s"Order $orderNumber not found!"))

    case CollectOrder(orderNumber) =>
      val collectedProducts: Seq[Product] = orders.filter(_.number == orderNumber).flatMap(_.product)
      Seq(collectedProducts).map {
        case Nil => OrderCollectionRejected(s"Order $orderNumber not found!")
        case ps  => OrderCollected(ps)
      }
  }

  def updateAll(events: Seq[Event]): CoffeeShop =
    events.foldLeft(this)(update)

  private def update(cs:CoffeeShop, e : Event): CoffeeShop = e match {
    case OrderPlaced(order)                                                               => cs.copy(orders = orders :+ order)
    case PaymentAccepted(receipt)                                                         => cs.copy(receipts = receipts :+ receipt)
    case OrderCollected(products)                                                         => cs.copy(inventory = inventory -- products)
    case _: OrderPlacementRejected | _: OrderPaymentRejected | _: OrderCollectionRejected => this
  }

  private def inventoryAvailable(products: Seq[Product]): Boolean =
    products.toSet.subsetOf(inventory.keySet)

}
object CoffeeShop{
  type OrderNumber = Int
  type Money = BigDecimal
  type Products = Seq[Product]

  def empty = CoffeeShop(orders = Seq.empty, receipts = Seq.empty, inventory = Map.empty)

  def initialise (events: Seq[Event]): CoffeeShop = CoffeeShop.empty.updateAll(events)
}
