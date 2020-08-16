package com.adaptionsoft.games.trivia.runner

import com.adaptionsoft.games.uglytrivia.{Game, Log, Player, Players}

object GameRunner {
  def run(dice: Dice, book: Book): Result = {
    implicit val log: Log = new Log
    val names = Players.Names(Player.Name("Chet"), Player.Name("Pat"), Player.Name("Sue"))
    val aGame = new Game(dice, book, names)
    do {
      aGame.play()
    } while (aGame.keepPlaying())
    Result(dice.rolls, book.answers, log.all.mkString("\n"))
  }
}

case class Result(rolls: List[Int], stops: List[Int], out: String) {
  override def toString: String =
    s"""${rolls.mkString(",")}
      |${stops.mkString(",")}
      |$out""".stripMargin
}

abstract class Input(is: Iterator[Int]) {
  protected var _is = List.empty[Int]
  protected def next(): Int = {
    val n = is.next()
    _is :+= n
    n
  }
}
class Dice(dice: Iterator[Int]) extends Input(dice) {
  def rolls: List[Int] = _is
  def roll(): Int = next()
}

class Book(cheatSheet: Iterator[Int]) extends Input(cheatSheet) {
  def answers: List[Int] = _is
  def answer(): Int = next()
}