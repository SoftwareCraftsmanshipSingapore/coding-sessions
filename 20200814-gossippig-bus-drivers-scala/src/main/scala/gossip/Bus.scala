package gossip

import gossip.Bus.{Buses, Stop}

import scala.collection.mutable

class Bus (val gossip: Gossip, routes: Seq[Stop]) {
  private val all = Iterator.continually(routes.iterator).flatten
  def move(): Stop = all.next()
  def addGossip(bus: Bus): Unit = gossip.add(bus.gossip)
}
object Bus {
  type Stop = Char
  type Buses = List[Bus]
  def apply(gossip: Gossip, routes: String): Bus = new Bus(gossip, routes)
}

class Gossip(private[Gossip] val underlying: mutable.Set[String]) {
  def add(gossip: Gossip): Unit = underlying.addAll(gossip.underlying)
  def size: Int = underlying.size
}
object Gossip {
  def apply(gossip: String) = new Gossip(mutable.Set(gossip))
}

class Transport(buses: Buses) {
  private val busCount = buses.size
  def gossip(): Either[String, Int] = {
    @scala.annotation.tailrec
    def loop(minute: Int = 1): Either[String, Int] = {
      buses.groupBy(_.move()).values.filter(_.size > 1).foreach(gossip)
      if (buses.forall(b => b.gossip.size == busCount))
        Right(minute)
      else
        if (minute == 480)
          Left("never")
        else
          loop(minute + 1)
    }
    loop()
  }

  private def gossip(buses:Buses):Unit =
    (buses ++ buses).sliding(2, 1).foreach {
      case b::bs => bs.foreach(b.addGossip)
    }
}

object Transport {
  def apply(buses: Bus*): Transport = new Transport(buses.toList)
}