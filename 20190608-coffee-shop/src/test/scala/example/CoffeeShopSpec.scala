package example

import example.CoffeeShopSpec.tester
import org.scalatest._
import org.scalatest.prop.TableDrivenPropertyChecks

class CoffeeShopSpec extends FlatSpec with Matchers with TestData with TableDrivenPropertyChecks{

  import tester._

  "The coffee shop" should "handle number of events" ignore{
    given(orderPlacedReactions)
      .when(acceptPayment)
      .`then`(paymentAcceptedReactions)
      .when(collectOrder)
      .`then`(orderCollectedReactions)

    given(orderPlacedReactions ++ paymentAcceptedReactions)
      .when(collectOrder)
      .`then`(orderCollectedReactions)
  }

  "A new Coffeeshop" should "reject an order" in {
    Table(
      ("when", "then")
      , (placeOrder, orderPlacementRejected)
      , (acceptPayment, orderPaymentRejected)
      , (collectOrder, orderCollectRejected)
    )forEvery {
      (w, t) => givenNew when w  `then` t
    }
  }

}

object CoffeeShopSpec {

  import State._

  val tester: CoffeeShopTester[New] = CoffeeShopTester[New]()

  sealed trait State

  object State {

    sealed trait New extends State

    sealed trait Given extends State

    sealed trait When extends State

  }

  case class CoffeeShopTester[S <: State] private(
                                                   cs: CoffeeShop = CoffeeShop.empty,
                                                   reactions: Seq[Event] = Seq.empty
                                                 ) extends Matchers {
    def given[T >: S <: New](events: Seq[Event]): CoffeeShopTester[Given] = copy(cs.updateAll(events))

    def givenNew[T >: S <: New]: CoffeeShopTester[Given] = copy(cs.updateAll(Nil))

    def when[T >: S <: Given](c: Command): CoffeeShopTester[When] = {
      val es = cs.receive(c)
      copy(cs.updateAll(es), es)
    }

    def `then`[T >: S <: When](expectedEvens: Seq[Event]): CoffeeShopTester[Given] = {
      reactions shouldBe expectedEvens
      CoffeeShopTester[Given](cs)
    }
  }

}
