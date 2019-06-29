package shop

import org.scalatest._
import org.scalatest.prop.TableDrivenPropertyChecks
import commands._, events._

class CoffeeShopSpec extends FlatSpec with Matchers with TestData with TableDrivenPropertyChecks{

  import CoffeeShopSpec.CoffeeShopTester._

  "The coffee shop" should "handle number of events" ignore {
    given(orderPlacedReactions)
      .when(acceptPayment)
      .dzen(paymentAcceptedReactions)
      .when(collectOrder)
      .dzen(orderCollectedReactions)

    given(orderPlacedReactions ++ paymentAcceptedReactions)
      .when(collectOrder)
      .dzen(orderCollectedReactions)
  }

  "A new CoffeeShop" should "reject an order" in {
    Table(
      ("when", "then")
      , (placeOrder, orderPlacementRejected)
      , (acceptPayment, orderPaymentRejected)
      , (collectOrder, orderCollectRejected)
    )forEvery {
      (w, t) => givenNew when w dzen t
    }
  }

}

object CoffeeShopSpec {

  import State._

  val tester: CoffeeShopTester[New] = CoffeeShopTester[New]()

  sealed trait State
  object State {
    sealed trait New   extends State
    sealed trait Given extends State
    sealed trait When  extends State
  }

  case class CoffeeShopTester[S <: State] private(
    cs: CoffeeShop = CoffeeShop.empty,
    reactions: Events = Seq.empty
  ) extends Matchers {
    import CoffeeShop._

    def when[T >: S <: Given](c: Command): CoffeeShopTester[When] = {
      val es = cs.receive(c)
      CoffeeShopTester[When](updateAll(cs, es), es)
    }

    def dzen[T >: S <: When](expectedEvens: Events): CoffeeShopTester[Given] = {
      reactions shouldBe expectedEvens
      CoffeeShopTester[Given](cs)
    }
  }

  object CoffeeShopTester {
    import CoffeeShop._

    def given(events: Events): CoffeeShopTester[Given] = CoffeeShopTester[Given](initialise(events), events)
    def givenNew: CoffeeShopTester[Given] = CoffeeShopTester[Given](initialise())
  }

}
