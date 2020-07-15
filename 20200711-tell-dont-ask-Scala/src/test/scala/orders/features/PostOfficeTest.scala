package orders.features

import orders.data.TestData
import orders.domain.Order
import orders.features.shipping.{PostOffice, ShippingRequest}
import org.scalatest.{EitherValues, OptionValues}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks

class PostOfficeTest
  extends AnyFunSuite with Matchers with TestData with EitherValues with OptionValues with TableDrivenPropertyChecks {
  private val postOffice = new PostOffice(orderRepository, shipmentService)

  private val approvedOrder = Order(Order.Status.Approved, "AUD")(Order.Item(salad, 2))
  test("approved order should be shipped") {
    val orderId = orderRepository.addOrder(approvedOrder)
    val shippingRequest = ShippingRequest(orderId)
    postOffice.process(shippingRequest).toOption.value shouldBe ()
    orderRepository.getById(orderId).toOption.value.status shouldBe Order.Status.Shipped
    shipmentService.isShipped(orderId) shouldBe true
  }

  import Order.Status._
  Table(
    ("invalid order states","reason"),
    (Created               ,"it is not Approved"),
    (Rejected              ,"it is has been Rejected"),
    (Shipped               ,"it is has already been Shipped")
  ).forEvery{
    (status, reason) =>
      test(s"when order is $status it cannot be shipped") {
        val order = approvedOrder.copy(status = status)
        val orderId = orderRepository.addOrder(order)
        val shippingRequest = ShippingRequest(orderId)
        postOffice.process(shippingRequest).left.value shouldBe s"Order [id=$orderId] cannot be shipped because: $reason"
        orderRepository.getById(orderId).toOption.value.status shouldBe status
        shipmentService.isShipped(orderId) shouldBe false
      }
  }
}
