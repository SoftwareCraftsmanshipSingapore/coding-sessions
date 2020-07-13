package orders.features

import orders.data.TestData
import orders.domain.Order
import orders.domain.Order.Status.Created
import orders.domain.Order.{GrossAmount, Quantity, Tax}
import orders.features.creation.PurchaseRequest.Item
import orders.features.creation.{OrderFactory, PurchaseRequest}
import org.scalatest.EitherValues
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class OrderCreationTests extends AnyFunSuite with Matchers with EitherValues with TestData {

  private val orderFactory = new OrderFactory(productCatalogue)

  test("purchase multiple items") {
    val purchaseRequest = PurchaseRequest("EUR", Item("salad", 2), Item("tomato", 3))
    orderFactory.make(purchaseRequest).right.value shouldBe Order(Created, "EUR")(
        Order.Item(salad , Quantity(2), GrossAmount("7.84") , Tax("0.72")),
        Order.Item(tomato, Quantity(3), GrossAmount("15.36"), Tax("1.41"))
      )
  }

  test("purchase an unknown product") {
    val purchaseRequest = PurchaseRequest("EUR", Item("unknown product", 2))
    orderFactory.make(purchaseRequest).left.value shouldBe """product error: "unknown product" not in catalogue"""
  }
}
