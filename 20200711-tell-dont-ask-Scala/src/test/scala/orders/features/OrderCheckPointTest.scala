package orders.features

import orders.data.TestData
import orders.domain.Order
import orders.doubles.InMemoryOrderRepository
import orders.features.checkpoint.{CheckPointRequest, OrderCheckPoint}
import org.scalatest.{EitherValues, OptionValues}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks

class OrderCheckPointTest
  extends AnyFunSuite with Matchers with TestData with EitherValues with TableDrivenPropertyChecks with OptionValues {
  private val orderRepository = new InMemoryOrderRepository
  private val orderCheckPoint = new OrderCheckPoint(orderRepository)

  private val newOrder = Order(Order.Status.Created, "AUD")(Order.Item(salad, 2))

  test("new order should pass the checkpoint when approved") {
    val orderId = orderRepository.addOrder(newOrder)
    val approvalRequest = CheckPointRequest(orderId, CheckPointRequest.Decision.Approve)
    orderCheckPoint.applyDecision(approvalRequest) shouldBe Right(())
    orderRepository.getById(orderId).value.status shouldBe Order.Status.Approved
  }

  test("a request to approve a non-existent order should report that the order is missing") {
    val unknownOrderId = -1
    val approvalRequest = CheckPointRequest(unknownOrderId, CheckPointRequest.Decision.Approve)
    orderCheckPoint.applyDecision(approvalRequest).left.value shouldBe "Order [id=-1] cannot be approved because: not available in repository"
  }

  import Order.Status._
  Table("Invalid order status for approval", Approved, Rejected, Shipped).forEvery {
    status =>
      test(s"a request to approve an order that is $status should fail and the order should remain unchanged") {
        val orderId = orderRepository.addOrder(newOrder.copy(status = status))
        val approvalRequest = CheckPointRequest(orderId, CheckPointRequest.Decision.Approve)
        orderCheckPoint.applyDecision(approvalRequest).left.value shouldBe s"Order [id=$orderId] cannot be approved because: it is already $status"
        orderRepository.getById(orderId).value.status shouldBe status
    }
  }
}
