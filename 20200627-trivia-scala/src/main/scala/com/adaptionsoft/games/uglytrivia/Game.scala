package com.adaptionsoft.games.uglytrivia

import java.util.LinkedList

import scala.collection.mutable


class Game(playerNames: String*) {
  private val players: Array[String] = playerNames.toArray
  private val playerIndices = Iterator.continually(players.indices.iterator).flatten
  private var places: Array[Int] = new Array[Int](6)
  private var purses: Array[Int] = new Array[Int](6)
  private val penaltyBox:mutable.Set[Int] = mutable.Set.empty
  private val popQuestions    :Iterator[String] = Iterator.range(0, 49).map(i => "Pop Question " + i)
  private val scienceQuestions:Iterator[String] = Iterator.range(0, 49).map(i => "Science Question " + i)
  private val sportsQuestions :Iterator[String] = Iterator.range(0, 49).map(i => "Sports Question " + i)
  private val rockQuestions   :Iterator[String] = Iterator.range(0, 49).map(i => "Rock Question " + i)
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
        println("The category is " + currentCategory)
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
      println("The category is " + currentCategory)
      askQuestion()
    }
  }

  private def askQuestion(): Unit = {
    val questions = currentCategory match {
      case "Pop"     => popQuestions
      case "Science" => scienceQuestions
      case "Sports"  => sportsQuestions
      case "Rock"    => rockQuestions
    }
    val question = questions.next()
    println(question)
  }

  private def currentCategory: String = places(currentPlayer) match {
    case 0 | 4 | 8  => "Pop"
    case 1 | 5 | 9  => "Science"
    case 2 | 6 | 10 => "Sports"
    case 3 | 7 | 11 => "Rock"
  }

  def wasCorrectlyAnswered: Boolean = {
    if (penaltyBox(currentPlayer)) {
      if (isGettingOutOfPenaltyBox) {
        println("Answer was correct!!!!")
        incCurrentPlayerPurse()
        println(players(currentPlayer) + " now has " + purses(currentPlayer) + " Gold Coins.")
        var winner: Boolean = didPlayerWin
        advancePlayer()
        winner
      }
      else {
        advancePlayer()
        true
      }
    }
    else {
      println("Answer was corrent!!!!")
      incCurrentPlayerPurse()
      println(players(currentPlayer) + " now has " + purses(currentPlayer) + " Gold Coins.")
      var winner: Boolean = didPlayerWin
      advancePlayer()
      winner
    }
  }

  def wrongAnswer: Boolean = {
    println("Question was incorrectly answered")
    println(players(currentPlayer) + " was sent to the penalty box")
    penaltyBox += currentPlayer
    advancePlayer()
    true
  }

  private def incCurrentPlayerPurse(): Unit = purses(currentPlayer) += 1
  private def advancePlayer(): Unit = currentPlayer = playerIndices.next()
  private def didPlayerWin: Boolean = !(purses(currentPlayer) == 6)
}