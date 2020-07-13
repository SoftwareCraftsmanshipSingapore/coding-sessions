package orders.features

import orders.domain.Order.Status.Created
import orders.domain.Order.{GrossAmount, Quantity, Tax}
import orders.domain.Produkt.{Price, TaxPercentage}
import orders.domain.{Order, Produkt}
import orders.doubles.InMemoryProductCatalogue
import orders.features.PurchaseRequest.Item
import orders.features.creation.{OrderFactory, PurchaseRequest}
import org.scalatest.{EitherValues, OptionValues}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class OrderCreationTests extends AnyFunSuite with Matchers with EitherValues {
  private val food = Produkt.Category("food", TaxPercentage("10"))
  private val salad = Produkt("salad", Price("3.56"), food)
  private val tomato = Produkt("tomato", Price("4.65"), food)
  private val productCatalogue = new InMemoryProductCatalogue(salad, tomato)

  private val orderFactory = new OrderFactory(productCatalogue)

  test("purchase multiple items") {
    val purchaseRequest = PurchaseRequest("EUR", Item("salad", 2), Item("tomato", 3))
    orderFactory.make(purchaseRequest).right.value shouldBe Order(
      Created, "EUR", Seq(
        Order.Item(salad , Quantity(2), GrossAmount("7.84") , Tax("0.72")),
        Order.Item(tomato, Quantity(3), GrossAmount("15.36"), Tax("1.41"))
      )
    )
  }

  test("purchase an unknown product") {
    val purchaseRequest = PurchaseRequest("EUR", Item("unknown product", 2))
    orderFactory.make(purchaseRequest).left.value shouldBe """product error: "unknown product" not in catalogue"""
  }
}
