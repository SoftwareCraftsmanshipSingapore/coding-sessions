package com.adaptionsoft.games.trivia.runner

import com.adaptionsoft.games.uglytrivia.Game

object GameRunner {
  def run(dice: Iterator[Int], answers: Iterator[Int]): Result = {

    val rolls = List.empty[Int]
    val stops = List.empty[Int]
    val aGame = new Game(dice, answers, "Chet", "Pat", "Sue")
    do {
      aGame.play()
    } while (aGame.assessAnswer())
    Result(rolls, stops, aGame.log.mkString("\n"))
  }
}

case class Result(rolls: List[Int], stops: List[Int], out: String) {
  override def toString: String =
    s"""${rolls.mkString(",")}
      |${stops.mkString(",")}
      |$out""".stripMargin
}