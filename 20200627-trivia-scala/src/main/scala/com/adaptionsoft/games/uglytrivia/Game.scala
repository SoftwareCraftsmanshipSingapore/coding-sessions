package com.adaptionsoft.games.uglytrivia

import com.adaptionsoft.games.trivia.runner.{Book, Dice}
import com.adaptionsoft.games.uglytrivia.Questions.Question

import scala.collection.mutable

class Game(dice: Dice, book: Book, board: Board, players: Players, questions: Questions)(implicit log: Log) {
  private var question = Option.empty[Question]
  players.advancePlayer()

  def play(): Unit = question =
    players
      .tryToMove(dice.roll())
      .map(board.getQuestionCategory)
      .map(questions.pickQuestion)

  def keepPlaying(): Boolean = {
    //FIXME: should be getting answer only if a question was actually asked, if no move no question is asked
    if (book.answer() == 7)
      players.wrongAnswer() //FIXME: should not be called if no question was picked
    else question.foreach(_ => players.wasCorrectlyAnswered())
    players.keepPlaying
  }
}

class Log {
  private val _log = mutable.Buffer.empty[String]
  def add(msg: String): Unit = _log += msg
  def all: List[String] = _log.toList
}
