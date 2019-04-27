package auction

import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.{FlatSpec, Matchers}

class AuctionTest extends FlatSpec with Matchers with TableDrivenPropertyChecks{


  "auction with two bidders" should "return winner" in {

    val (alice, bobby) = ("Alice", "Bob")

    Table(
       ("reserve price", "bids"                            , "winning bid")
      ,(1              ,  Seq(Bid(alice, 2), Bid(bobby, 1)), Bid(alice, 2))
      ,(1              ,  Seq(Bid(alice, 1), Bid(bobby, 2)), Bid(bobby, 2))
      ,(1              ,  Seq(Bid(alice, 3), Bid(alice, 2)), Bid(alice, 2))
      ,(5              ,  Seq(Bid(alice, 3), Bid(alice, 2)), NoBid        )
    )forEvery{
      case (reservePrice, bids , winningBid) =>
        val auction = new Auction(reserve = reservePrice)
        bids.foreach(auction.receivesBid)
        auction.winner() shouldBe winningBid
    }
  }

}

class Auction(reserve: Int) {

  var bids: List[ABid] = List(NoBid)

  def winner(): ABid = bids.maxBy(_.amount)

  def receivesBid(bid: ABid): Unit = {
    bids = bids.filterNot(b => b.bidder == bid.bidder) ++ Option(bid).filter(_.amount > reserve)
  }

}

sealed trait ABid {
  def amount: Int = 0
  def bidder: String = "not bob"
}

case class Bid(override val bidder: String, override val amount: Int) extends ABid
case object NoBid extends ABid