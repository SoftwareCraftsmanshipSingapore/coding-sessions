package example

import example.CoffeeShop.Money
import example.Command.{AcceptPayment, CollectOrder, PlaceOrder}
import example.Event.{OrderCollected, OrderCollectionRejected, OrderPaymentRejected, OrderPlaced, OrderPlacementRejected, PaymentAccepted}

trait TestData {
  type Events = Seq[Event]
  protected val orderNumber = 1
  protected val coffeePrice: Money = 1
  protected val coffee = Coffee("Latte", "small", coffeePrice)
  protected val receipt = Receipt(orderNumber)
  protected val orderRejectionMsg = "No Stock!"
  protected val paymentRejectionMsg = s"Order $orderNumber not found!"
  protected val collectRejectionMsg = s"Order $orderNumber not found!"

  protected val placeOrder = PlaceOrder(Seq(coffee))
  protected val acceptPayment = AcceptPayment(orderNumber, coffeePrice)
  protected val collectOrder = CollectOrder(orderNumber)

  protected val orderPlacedReactions: Events = Seq(OrderPlaced(Order(orderNumber, coffee)))
  protected val paymentAcceptedReactions: Events = Seq(PaymentAccepted(receipt))
  protected val orderCollectedReactions: Events = Seq(OrderCollected(List(coffee)))
  protected val orderPlacementRejected: Events = Seq(OrderPlacementRejected(orderRejectionMsg))
  protected val orderPaymentRejected: Events = Seq(OrderPaymentRejected(paymentRejectionMsg))
  protected val orderCollectRejected: Events = Seq(OrderCollectionRejected(collectRejectionMsg))
}
