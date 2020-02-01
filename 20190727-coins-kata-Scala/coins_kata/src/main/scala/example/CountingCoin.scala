package example


/**
  * Coins in the US
  * fifty  (50 cents)
  * quarters (25 cents)
  * dimes (10 cents)
  * nickels (5 cents)
  * pennies (1 cent)
  *
  *
  *
  */



object CountingCoin {
  type Count = Int
  type Denomination = Int
  type Coin = (Denomination, Count)
  type Coins = Map[Denomination, Count]

  val denominations = List(50, 25, 20, 10, 5, 1)

  //amount => number of cents
  def count(amount: Int): List[Int] =  {
    val x = denominations.foldLeft((amount, Map.empty[Int, Int])){
      case ((a, c),d) =>
        val times = a / d
        val rem = a % d
        (rem, c + (d -> times))
    }
    x._2.toList.sortBy(_._1 * -1).flatMap {
      case (coin, count) => (1 to count).map(_ => coin)
    }
  }

  def reduce(coins: Coins): Coins = {
    val denominationProgression = denominations
      .sorted.sliding(2, 1).map(l => l(0) -> l(1)).toMap

    val smallCoins = coins.filter(_._1 < 50).toList.sortBy(_._1)
    val x = smallCoins.map {
      case coins@(d, _) => a2b(coins, denominationProgression(d))
    }

    x.reduce(concatMaps)

  }

  def concatMaps(m1: Map[Int, Int], m2: Map[Int, Int]) = {
    m1.map {
      case (k, v) => (k, m2.get(k).map(_ + v).getOrElse(v))
    } ++ (m2 -- m1.keys)
  }

  def ones2Fives(ones: Count): Coins = {
    val fives = ones / 5
    val remainingOnes = ones % 5
    Map(1 -> remainingOnes, 5 -> fives)
  }

  def a2b(as: Coin, d: Denomination): Coins = {
    val (a, c) = as
    val ds = (c * a) / d
    val remainingAs = (c * a) % d
    Map(a -> remainingAs, d -> ds)
  }


}

