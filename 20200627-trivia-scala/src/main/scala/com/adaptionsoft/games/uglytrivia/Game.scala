package com.adaptionsoft.games.uglytrivia

import java.util.LinkedList


class Game(playerNames: String*) {
  private val players: Array[String] = playerNames.toArray
  private val howManyPlayers: Int = players.length
  private val playerIndices = Iterator.continually(players.indices.iterator).flatten
  private var places: Array[Int] = new Array[Int](6)
  private var purses: Array[Int] = new Array[Int](6)
  private var inPenaltyBox: Array[Boolean] = new Array[Boolean](6)
  private var popQuestions: LinkedList[String] = new LinkedList[String]
  private var scienceQuestions: LinkedList[String] = new LinkedList[String]
  private var sportsQuestions: LinkedList[String] = new LinkedList[String]
  private var rockQuestions: LinkedList[String] = new LinkedList[String]
  private var currentPlayer: Int = 0
  private var isGettingOutOfPenaltyBox: Boolean = false

  private def initialize(): Unit = {
    var i: Int = 0
    while (i < 50) {
      popQuestions.addLast("Pop Question " + i)
      scienceQuestions.addLast("Science Question " + i)
      sportsQuestions.addLast("Sports Question " + i)
      rockQuestions.addLast("Rock Question " + i)
      i += 1
    }
  }

  initialize()
  addPlayers()

  private def addPlayers(): Unit = {
    playerNames.zipWithIndex.foreach{
      case (p, i) =>
        val playerIndex = i + 1
        places(playerIndex) = 0
        purses(playerIndex) = 0
        inPenaltyBox(playerIndex) = false
        println(p + " was added")
        println("They are player number " + playerIndex)
    }
  }

  def roll(roll: Int): Unit = {
    println(players(currentPlayer) + " is the current player")
    println("They have rolled a " + roll)
    if (inPenaltyBox(currentPlayer)) {
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
    val question = questions.removeFirst()
    println(question)
  }

  private def currentCategory: String = places(currentPlayer) match {
    case 0 | 4 | 8  => "Pop"
    case 1 | 5 | 9  => "Science"
    case 2 | 6 | 10 => "Sports"
    case _          => "Rock"
  }

  def wasCorrectlyAnswered: Boolean = {
    if (inPenaltyBox(currentPlayer)) {
      if (isGettingOutOfPenaltyBox) {
        println("Answer was correct!!!!")
        purses(currentPlayer) += 1
        println(players(currentPlayer) + " now has " + purses(currentPlayer) + " Gold Coins.")
        var winner: Boolean = didPlayerWin
        currentPlayer += 1
        if (currentPlayer == howManyPlayers) currentPlayer = 0
        winner
      }
      else {
        currentPlayer += 1
        if (currentPlayer == howManyPlayers) currentPlayer = 0
        true
      }
    }
    else {
      println("Answer was corrent!!!!")
      purses(currentPlayer) += 1
      println(players(currentPlayer) + " now has " + purses(currentPlayer) + " Gold Coins.")
      var winner: Boolean = didPlayerWin
      currentPlayer += 1
      if (currentPlayer == howManyPlayers) currentPlayer = 0
      winner
    }
  }

  def wrongAnswer: Boolean = {
    println("Question was incorrectly answered")
    println(players(currentPlayer) + " was sent to the penalty box")
    inPenaltyBox(currentPlayer) = true
    currentPlayer += 1
    if (currentPlayer == howManyPlayers) currentPlayer = 0
    true
  }

  private def didPlayerWin: Boolean = !(purses(currentPlayer) == 6)
}