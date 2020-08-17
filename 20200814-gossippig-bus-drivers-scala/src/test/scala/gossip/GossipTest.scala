package gossip

import org.scalatest.EitherValues
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class GossipTest extends AnyFunSuite with Matchers with EitherValues {
  import GossipTest._

  test("two drivers with non-overlapping routes should not gossip"){
    val b1 = bus1("1")
    val b2 = bus2("2")
    val transport = Transport(b1, b2)
    transport.gossip() shouldBe Left("never")
  }

  test("two drivers with a 1 stop route that fully overlaps should gossip on 1st stop"){
    val b1 = bus1("1")
    val b2 = bus2("1")
    val transport = Transport(b1, b2)
    transport.gossip() shouldBe Right(1)
  }

  test("two drivers with the same route but different starting stops will never meet"){
    val b1 = bus1("12")
    val b2 = bus2("21")
    val transport = Transport(b1, b2)
    transport.gossip() shouldBe Left("never")
  }
  test("three drivers with the same route but different starting stops will never meet"){
    val b1 = bus1("123")
    val b2 = bus2("231")
    val b3 = bus3("312")
    val transport = Transport(b1, b2, b3)
    transport.gossip() shouldBe Left("never")
  }
  test("two drivers with the same route and starting stop and a third driver with a non overlapping route will never meet"){
    val b1 = bus1("123")
    val b2 = bus2("123")
    val b3 = bus3("456")
    val transport = Transport(b1, b2, b3)
    transport.gossip() shouldBe Left("never")
  }
  test("two drivers with the same route and starting stop one apart and a third driver with the same route but travelling in the opposite direction will exchange all gossip in 5"){
    val b1 = bus1("123")
    val b2 = bus2("231")
    val b3 = bus3("321")
    val transport = Transport(b1, b2, b3)
    transport.gossip() shouldBe Right(5)
  }
  test("three drivers on routes overlapping at two exchanges will share all the gossip in 16 minutes"){
    val b1 = bus1("123")
    val b2 = bus2("3456")
    val b3 = bus3("678")
    val transport = Transport(b1, b2, b3)
    transport.gossip() shouldBe Right(16)
  }
  test("four drivers on routes overlapping at three exchanges will share all the gossip in 25 minutes"){
    val b1 = bus1("abcde")
    val b2 = bus2("efgh")
    val b3 = bus3("hijkl")
    val b4 = bus4("lmno")
    val transport = Transport(b1, b2, b3, b4)
    transport.gossip() shouldBe Right(25)
  }
}

object GossipTest {
  private def bus1(route: String) = Bus(Gossip("1"), route)
  private def bus2(route: String) = Bus(Gossip("2"), route)
  private def bus3(route: String) = Bus(Gossip("3"), route)
  private def bus4(route: String) = Bus(Gossip("4"), route)
}
