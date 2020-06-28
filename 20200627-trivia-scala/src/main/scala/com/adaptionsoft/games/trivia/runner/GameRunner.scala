package com.adaptionsoft.games.trivia.runner

import com.adaptionsoft.games.uglytrivia.Game

object GameRunner {
  def run(rollValues: Iterator[Int], stopValues: Iterator[Int]): Result = {
    var aAWinner = false

    var rolls = List.empty[Int]
    var stops = List.empty[Int]
    val aGame = new Game("Chet", "Pat", "Sue")
    do {
      val rollValue = rollValues.next()
      rolls = rolls ::: List(rollValue)
      aGame.roll(rollValue)              //1 ... 5
      val stopValue = stopValues.next()
      stops = stops ::: List(stopValue)
      if (stopValue == 7) {      //0 ... 8
        aAWinner = aGame.wrongAnswer
      }
      else {
        aAWinner = aGame.wasCorrectlyAnswered
      }
    } while (aAWinner)
    Result(rolls, stops, aGame.log.mkString("\n"))
  }
}

case class Result(rolls: List[Int], stops: List[Int], out: String) {
  override def toString: String =
    s"""${rolls.mkString(",")}
      |${stops.mkString(",")}
      |$out""".stripMargin
}