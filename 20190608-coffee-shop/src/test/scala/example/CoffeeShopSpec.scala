package example

import example.CoffeeShop.{Money, OrderNumber}
import example.CoffeeShopSpec.tester
import example.Command.{AcceptPayment, CollectOrder, PlaceOrder}
import example.Event.{OrderCollected, OrderPlaced, OrderRejected, PaymentAccepted}
import org.scalatest._

class CoffeeShopSpec extends FlatSpec with Matchers {
  val orderNumber = 1
  val coffeePrice: Money = 1
  val coffee = Coffee("Latte", "small", coffeePrice)
  val order = Order(orderNumber, coffee)
  val receipt = Receipt(orderNumber)
  val orderRejectionMsg = "No Stock!"

  val placeOrder = PlaceOrder(Seq(coffee))
  val acceptPayment = AcceptPayment(orderNumber, coffeePrice)
  val collectOrder = CollectOrder(orderNumber)

  private val orderPlacedReactions = Seq(OrderPlaced(Order(orderNumber, coffee)))
  private val paymentAcceptedReactions = Seq(PaymentAccepted(receipt))
  private val orderCollectedReactions = Seq(OrderCollected(List(coffee)))
  private val orderRejected = Seq(OrderRejected(orderRejectionMsg))


  "Placing and order" should "result an order" in {
     val coffeeShop = new CoffeeShop(Nil, Nil, Map(coffee -> 1))
     val afterOrder = coffeeShop.order(coffee)
     afterOrder shouldBe CoffeeShop(Seq(order), Nil, Map(coffee -> 1))
     afterOrder.orders shouldBe Seq(order)

     val afterPayment = afterOrder.pay(orderNumber, coffeePrice)
     val receipt = Receipt(orderNumber)
     afterPayment shouldBe CoffeeShop(Seq(order), Seq(receipt), Map(coffee -> 1))
     afterPayment.receipts shouldBe Seq(receipt)

     val afterCollection = afterPayment.collect(orderNumber)
     afterCollection shouldBe CoffeeShop(Seq(order), Seq(receipt), Map.empty)
     afterCollection.inventory shouldBe 'empty


  }
  "Placing and order again" should "result an order" in {
     val coffeeShop = new CoffeeShop(Nil, Nil, Map(coffee -> 1))
     coffeeShop
       .order(coffee)
       .pay(orderNumber, coffeePrice)
       .collect(orderNumber) shouldBe CoffeeShop(Seq(order), Seq(receipt), Map.empty)
  }

  "placing and order through command" should "be possible" in {
     val coffeeShop = new CoffeeShop(Nil, Nil, Map(coffee -> 1))
     coffeeShop.receive(placeOrder) shouldBe orderPlacedReactions
  }
  "payment through command" should "be possible" in {
     val coffeeShop = new CoffeeShop(Seq(order), Nil, Map(coffee -> 1))
     coffeeShop.receive(acceptPayment) shouldBe paymentAcceptedReactions
  }

  "ordering through command" should "be possible" in {
    val coffeeShop = new CoffeeShop(Seq(order), Seq(receipt), Map(coffee -> 1))
    coffeeShop.receive(collectOrder) shouldBe orderCollectedReactions
  }
  "stream of commands" should "be processed" in {
    val coffeeShop = new CoffeeShop(Nil, Nil, Map(coffee -> 1))

    val commands = Seq(placeOrder, acceptPayment, collectOrder)

    val (ncs , nes) = commands.foldLeft(coffeeShop -> Seq.empty[Event]) {
      case((cs, es), c) =>
        val (ncs, nes) = cs.processCommand(c)
        ncs -> (es ++ nes)
    }

    val expectedEvents =
      orderPlacedReactions ++ paymentAcceptedReactions ++ orderCollectedReactions
    nes shouldBe expectedEvents
  }

  "The coffee shop" should "handle number of events" in {
    tester
      .given(orderPlacedReactions)
      .when(acceptPayment)
      .`then`(paymentAcceptedReactions)
      .when(collectOrder)
      .`then`(orderCollectedReactions)
    tester
      .given(orderPlacedReactions ++ paymentAcceptedReactions)
      .when(collectOrder)
      .`then`(orderCollectedReactions)
    tester
      .given(Nil)
      .when(placeOrder)
      .`then`(orderRejected)
  }

}

object CoffeeShopSpec {
  import State._

  val tester = CoffeeShopTester[New]()

  sealed trait State
  object State {
    sealed trait New extends State
    sealed trait Given extends State
    sealed trait When extends State
  }
  case class CoffeeShopTester[S <: State] private (
    cs: CoffeeShop = CoffeeShop.empty,
    reactions: Seq[Event] = Seq.empty
  ) extends Matchers {
    def given[T >: S <: New](events: Seq[Event]): CoffeeShopTester[Given] = copy(cs.update(events))

    def when[T >: S <: Given](c: Command): CoffeeShopTester[When] = {
      val es = cs.receive(c)
      copy(cs.update(es), es)
    }

    def `then`[T >: S <: When](expectedEvens: Seq[Event]): CoffeeShopTester[Given] = {
      reactions shouldBe expectedEvens
      CoffeeShopTester[Given](cs)
    }
  }
}
