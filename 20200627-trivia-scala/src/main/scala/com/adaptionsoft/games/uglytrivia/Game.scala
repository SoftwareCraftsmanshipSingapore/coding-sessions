package com.adaptionsoft.games.uglytrivia

import scala.collection.mutable

class Game(playerNames: String*) {
  private val players: Array[String] = playerNames.toArray
  private val playerIndices = Iterator.continually(players.indices.iterator).flatten
  private var places: Array[Int] = new Array[Int](6)
  private var purses: Array[Int] = new Array[Int](6)
  private val penaltyBox:mutable.Set[Int] = mutable.Set.empty
  private val questions = {
    List("Pop", "Science", "Sports", "Rock").map {
      cat => cat -> Iterator.range(0, 49).map(i => s"$cat Question $i")
    }.toMap
  }
  private var currentPlayer: Int = playerIndices.next()
  private var isGettingOutOfPenaltyBox: Boolean = false

  addPlayers()

  private def addPlayers(): Unit = {
    playerNames.zipWithIndex.foreach{
      case (p, i) =>
        val playerIndex = i + 1
        places(playerIndex) = 0
        purses(playerIndex) = 0
        println(p + " was added")
        println("They are player number " + playerIndex)
    }
  }

  def roll(roll: Int): Unit = {
    println(players(currentPlayer) + " is the current player")
    println("They have rolled a " + roll)
    if (penaltyBox(currentPlayer)) {
      if (roll % 2 != 0) {
        isGettingOutOfPenaltyBox = true
        println(players(currentPlayer) + " is getting out of the penalty box")
        places(currentPlayer) = places(currentPlayer) + roll
        if (places(currentPlayer) > 11) places(currentPlayer) = places(currentPlayer) - 12
        println(players(currentPlayer) + "'s new location is " + places(currentPlayer))
        askQuestion()
      }
      else {
        println(players(currentPlayer) + " is not getting out of the penalty box")
        isGettingOutOfPenaltyBox = false
      }
    }
    else {
      places(currentPlayer) = places(currentPlayer) + roll
      if (places(currentPlayer) > 11) places(currentPlayer) = places(currentPlayer) - 12
      println(players(currentPlayer) + "'s new location is " + places(currentPlayer))
      askQuestion()
    }
  }

  private def askQuestion(): Unit = {
    println("The category is " + currentCategory)
    println(questions(currentCategory).next())
  }

  private def currentCategory: String = places(currentPlayer) match {
    case 0 | 4 | 8  => "Pop"
    case 1 | 5 | 9  => "Science"
    case 2 | 6 | 10 => "Sports"
    case 3 | 7 | 11 => "Rock"
  }

  def wasCorrectlyAnswered: Boolean = {
    if (penaltyBox(currentPlayer)) {
      if (isGettingOutOfPenaltyBox)
        correctlyAnswered("Answer was correct!!!!")
      else {
        advancePlayer()
        true
      }
    }
    else correctlyAnswered("Answer was corrent!!!!")
  }

  def wrongAnswer: Boolean = {
    println("Question was incorrectly answered")
    println(players(currentPlayer) + " was sent to the penalty box")
    penaltyBox += currentPlayer
    advancePlayer()
    true
  }

  private def correctlyAnswered(message: String):Boolean = {
    println(message)
    incCurrentPlayerPurse()
    println(players(currentPlayer) + " now has " + purses(currentPlayer) + " Gold Coins.")
    val winner: Boolean = didPlayerWin
    advancePlayer()
    winner
  }
  private def incCurrentPlayerPurse(): Unit = purses(currentPlayer) += 1
  private def advancePlayer(): Unit = currentPlayer = playerIndices.next()
  private def didPlayerWin: Boolean = !(purses(currentPlayer) == 6)
}