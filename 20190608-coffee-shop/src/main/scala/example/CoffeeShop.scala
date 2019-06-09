package example

import example.CoffeeShop.{Money, OrderNumber, Products}
import example.Command.{AcceptPayment, CollectOrder, PlaceOrder}
import example.Event.{OrderCollected, OrderPlaced, OrderRejected, PaymentAccepted}

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
  case class OrderRejected(msg: String) extends Event
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

  def processCommand(c:Command): (CoffeeShop, Seq[Event]) = {
    val events = receive(c)
    (update(events), events)
  }
  //Seq cause could create multiple changes
  def receive(command: Command): Seq[Event] = command match{
    case PlaceOrder(products) =>
      if(inventoryAvailable(products)) {
        val order = Order(1, products: _*)
        Seq(OrderPlaced(order))
      }else{
        Seq(OrderRejected("No Stock!"))
      }

    case AcceptPayment(orderNumber, money) =>
     Seq(PaymentAccepted(Receipt(orderNumber)))

    case CollectOrder(orderNumber) =>
      val collectedProducts: Seq[Product] = orders.filter(_.number == orderNumber).flatMap(_.product)
      Seq(OrderCollected(collectedProducts))
  }

  def update(events: Seq[Event]): CoffeeShop =
    events.foldLeft(this) {
      case (cs, OrderPlaced(order)) =>
        cs.copy(orders = orders :+ order)
      case (cs, PaymentAccepted(receipt)) =>
        cs.copy(receipts = receipts :+ receipt)
      case (cs, OrderCollected(products)) =>
        cs.copy(inventory = inventory -- products)
      case (cs, OrderRejected(msg)) =>
        return this
    }

  def order(product: Product*): CoffeeShop = {
    val order = Order(1, product:_*)
    copy(orders = orders :+ order)
  }

  def pay(orderNumber: OrderNumber, money: Money): CoffeeShop =
    copy(receipts = receipts :+ Receipt(orderNumber))

  def collect(orderNumber: OrderNumber): CoffeeShop = {
    val collectedProducts = orders.filter(_.number == orderNumber).flatMap(_.product)
    copy(inventory = inventory -- collectedProducts)
  }

  private def inventoryAvailable(products: Seq[Product]): Boolean =
    products.toSet.subsetOf(inventory.keySet)

}
object CoffeeShop{
  type OrderNumber = Int
  type Money = BigDecimal
  type Products = Seq[Product]

  def empty = CoffeeShop(orders = Seq.empty, receipts = Seq.empty, inventory = Map.empty)

  def initialise (events: Seq[Event]): CoffeeShop = CoffeeShop.empty.update(events)
}
