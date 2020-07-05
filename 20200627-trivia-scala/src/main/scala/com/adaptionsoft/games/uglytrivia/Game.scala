package com.adaptionsoft.games.uglytrivia

import scala.collection.mutable

class Game(dice: Iterator[Int], answers: Iterator[Int], playerNames: String*) {
  private val _log = mutable.Buffer.empty[String]
  private val questions = new Questions(addLog)
  private val players = {
    val ps = playerNames.zipWithIndex.map {
      case (n, i) => new Player(i + 1, n, answers)(addLog)
    }
    Iterator.continually(ps.iterator).flatten.buffered
  }
  private def player: Player = players.head

  def play(): Int = {
    val rolledNumber = dice.next()
    player.takeTurn(rolledNumber).foreach(questions.pickQuestion) //FIXME: should remember the question
    rolledNumber
  }

  def assessAnswer(): Boolean = {
    //FIXME: should be asking the question if it was answered correctly
    if (player.answer() == 7) {
      wrongAnswer
    } else wasCorrectlyAnswered
  }

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