package com.adaptionsoft.games.uglytrivia

import scala.collection.mutable

class Game(dice: Iterator[Int], answers: Iterator[Int], playerNames: String*) {
  private val _log = mutable.Buffer.empty[String]
  private val questions = new Questions(addLog, answers)
  private val players = {
    val ps = playerNames.zipWithIndex.map {
      case (n, i) => new Player(i + 1, n, questions)(addLog)
    }
    Iterator.continually(ps.iterator).flatten.buffered
  }
  private def player: Player = players.head

  def roll(): Int = player.takeTurn(dice.next())

  def answer(correct: Boolean): Boolean = if (correct) wasCorrectlyAnswered else wrongAnswer

  private def wasCorrectlyAnswered: Boolean = {
    if (player.inPenaltyBox) {
      if (player.isGettingOutOfPenaltyBox) //FIXME: never gets out of the penalty box - BUG?
         correctlyAnswered("Answer was correct!!!!")
      else {
        advancePlayer()
        true
      }
    }
    else correctlyAnswered("Answer was corrent!!!!")
  }

  private def wrongAnswer: Boolean = {
    addLog("Question was incorrectly answered")
    player.gotoPenaltyBox()
    advancePlayer()
    true
  }

  private def correctlyAnswered(message: String):Boolean = {
    addLog(message)
    player.addCoin()
    addLog(player.name + " now has " + player.purse + " Gold Coins.")
    val winner: Boolean = didPlayerWin
    advancePlayer()
    winner
  }
  private def advancePlayer(): Unit = players.next()
  private def didPlayerWin: Boolean = !player.hasWon

  private def addLog(msg: String): Unit = _log += msg
  def log: List[String] = _log.toList
}