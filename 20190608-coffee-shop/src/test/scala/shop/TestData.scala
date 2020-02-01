package shop

import events._, commands._, model._, implicits._

trait TestData {
  protected val orderNumber = 1
  protected val coffeePrice: Money = 1
  protected val coffee = Coffee("Latte", "small", coffeePrice)
  protected val receipt = Receipt(orderNumber, coffeePrice)
  protected val orderRejectionMsg = "No Stock!"
  protected val paymentRejectionMsg = s"Order $orderNumber not found!"
  protected val collectRejectionMsg = s"Order $orderNumber not found!"

  protected val placeOrder = PlaceOrder(Seq(coffee))
  protected val acceptPayment = AcceptPayment(orderNumber, coffeePrice)
  protected val collectOrder = CollectOrder(orderNumber)

  protected val orderPlacedReactions    : Events = OrderPlaced(Order(orderNumber, Seq(coffee)))
  protected val paymentAcceptedReactions: Events = PaymentAccepted(receipt)
  protected val orderCollectedReactions : Events = OrderCollected(List(coffee))
  protected val orderPlacementRejected  : Events = OrderPlacementRejected(orderRejectionMsg)
  protected val orderPaymentRejected    : Events = OrderPaymentRejected(paymentRejectionMsg)
  protected val orderCollectRejected    : Events = OrderCollectionRejected(collectRejectionMsg)
}
