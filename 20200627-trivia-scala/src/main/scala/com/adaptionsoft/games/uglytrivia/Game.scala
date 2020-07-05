package com.adaptionsoft.games.uglytrivia

import com.adaptionsoft.games.trivia.runner.{Book, Dice}
import com.adaptionsoft.games.uglytrivia.Questions.Question

import scala.collection.mutable

class Game(dice: Dice, book: Book, playerNames: String*) {
  private val _log = mutable.Buffer.empty[String]
  private val questions = new Questions(addLog)
  private val players = {
    val ps = playerNames.zipWithIndex.map {
      case (n, i) => new Player(i + 1, n)(addLog)
    }
    Iterator.continually(ps.iterator).flatten
  }
  private var player: Player = _
  advancePlayer()

  private var question = Option.empty[Question]

  def play(): Unit = question = player.tryToMove(dice.roll()).map(questions.pickQuestion)

  def keepPlaying(): Boolean = {
    //FIXME: should be getting answer only if a question was actually asked, if no move no question is asked
    if (book.answer() == 7)
      player.wrongAnswer() //FIXME: should not be called if no question was picked
    else question.foreach(_ => player.wasCorrectlyAnswered())
    if (player.keepPlaying) {
      advancePlayer()
      true
    } else false
  }

  private def advancePlayer(): Unit = {
    player = players.next()
    addLog(s"${player.name} is the current player")
  }

  private def addLog(msg: String): Unit = _log += msg
  def log: List[String] = _log.toList
}
