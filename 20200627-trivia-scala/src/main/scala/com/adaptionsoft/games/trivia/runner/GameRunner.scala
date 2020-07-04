package com.adaptionsoft.games.trivia.runner

import com.adaptionsoft.games.uglytrivia.Game

object GameRunner {
  def run(dice: Iterator[Int], answers: Iterator[Int]): Result = {
    var aAWinner = false

    var rolls = List.empty[Int]
    var stops = List.empty[Int]
    val aGame = new Game(dice, answers, "Chet", "Pat", "Sue")
    do {
      rolls = rolls ::: List(aGame.roll())
      val stopValue = answers.next()
      stops = stops ::: List(stopValue)
      aAWinner = aGame.answer(stopValue != 7)
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