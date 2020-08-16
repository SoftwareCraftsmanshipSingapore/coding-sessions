package gossip

import org.scalatest.EitherValues
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class GossipTest extends AnyFunSuite with Matchers with EitherValues {
  test("two drivers with non-overlapping routes should not gossip"){
    val b1 = new Bus(List(1), Gossip("b1"))
    val b2 = new Bus(List(2), Gossip("b2"))
    val transport = new Transport(List(b1, b2))
    transport.gossip().left.value shouldBe "never"
  }
}
