package gossip

import gossip.Bus.Buses

import scala.collection.mutable

class Bus (routes: List[Int], val gossip: Gossip) {
  private val all = Iterator.continually(routes.iterator).flatten//.buffered
  def move(): Int = all.next()
  def addGossip(bus: Bus): Unit = gossip.add(bus.gossip.gossip)
}

class Gossip(val gossip: mutable.Set[String]) {
  def add(gossip: mutable.Set[String]): Unit = this.gossip ++ gossip
  def size: Int = gossip.size
}
object Gossip {
  def apply(gossip: String) = new Gossip(mutable.Set(gossip))
}

class Transport(buses: Buses) {
  private val busCount = buses.size
  def gossip(): Either[String, Int] = {
    @scala.annotation.tailrec
    def loop(minute: Int = 1): Either[String, Int] = {
      buses.groupBy(_.move()).values.foreach(gossip)
      if (buses.forall(b => b.gossip.size == busCount))
        Right(minute)
      else {
        if (minute == 480)
          Left("never")
        else
          loop(minute + 1)
      }
    }
    loop()
  }

  private def gossip(buses:Buses):Unit = {
    for {
      b1 <- buses
      b2 <- buses
    } b1.addGossip(b2)
  }
}

object Bus {
  type Buses = List[Bus]
}