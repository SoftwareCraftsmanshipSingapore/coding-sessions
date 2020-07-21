package com.adaptionsoft.games.uglytrivia

import com.adaptionsoft.games.trivia.runner.{Book, Dice}

import scala.collection.mutable

class Game(dice: Dice, book: Book, playersNames: Players.Names)(implicit log: Log) {
  private val questions = new Questions(Questions.categories)
  private val board = new Board(12, Questions.categories, playersNames)
  private val players = Players(playersNames, board, questions)

  players.advancePlayer()

  def play(): Unit = players.move(dice.roll())

  def keepPlaying(): Boolean = {
    //FIXME: should be getting answer only if a question was actually asked, if no move no question is asked
    if (book.answer() == 7)
      players.wrongAnswer() //FIXME: should not be called if no question was picked
    else players.wasCorrectlyAnswered()
    players.keepPlaying
  }
}

class Log {
  private val log = mutable.Buffer.empty[String]
  def add(msg: String): Unit = log += msg
  def all: List[String] = log.toList
}
