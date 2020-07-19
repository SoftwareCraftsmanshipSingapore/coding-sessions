package com.adaptionsoft.games.trivia.runner

import com.adaptionsoft.games.uglytrivia.{Board, Game, Log, Player, Players, Questions}

object GameRunner {
  def run(dice: Dice, book: Book): Result = {
    implicit val log: Log = new Log
    val names = Players.Names(Player.Name("Chet"), Player.Name("Pat"), Player.Name("Sue"))
    val board = new Board(12, Questions.categories, names)
    val questions = new Questions(Questions.categories)
    val players = Players(names, board, questions)
    val aGame = new Game(dice, book, players)
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