package com.adaptionsoft.games.uglytrivia

import com.adaptionsoft.games.trivia.runner.{Book, Dice}
import com.adaptionsoft.games.uglytrivia.Questions.Question

import scala.collection.mutable

class Game(dice: Dice, book: Book, playerNames: PlayerNames) {
  private val _log = new Log
  private val questions = new Questions(_log)
  private val players = {
    val ps = playerNames.names.zipWithIndex.map {
      case (n, i) => new Player(i + 1, n)(_log)
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
    _log.addLog(s"${player.name} is the current player")
  }

  def log: List[String] = _log.log
}

class Log {
  private val _log = mutable.Buffer.empty[String]
  def addLog(msg: String): Unit = _log += msg
  def log: List[String] = _log.toList
}

class PlayerNames private (val names: Seq[String])
object PlayerNames {
  def apply(player1Name: String, player2Name: String): PlayerNames =
    new PlayerNames(Seq(player1Name, player2Name))
  def apply(player1Name: String, player2Name: String, player3Name: String): PlayerNames =
    new PlayerNames(Seq(player1Name, player2Name, player3Name))
  def apply(player1Name: String, player2Name: String, player3Name: String, player4Name: String): PlayerNames =
    new PlayerNames(Seq(player1Name, player2Name, player3Name, player4Name))
  def apply(player1Name: String, player2Name: String, player3Name: String, player4Name: String, player5Name: String): PlayerNames =
    new PlayerNames(Seq(player1Name, player2Name, player3Name, player4Name, player5Name))
  def apply(player1Name: String, player2Name: String, player3Name: String, player4Name: String, player5Name: String, player6Name: String): PlayerNames =
    new PlayerNames(Seq(player1Name, player2Name, player3Name, player4Name, player5Name, player6Name))
}
