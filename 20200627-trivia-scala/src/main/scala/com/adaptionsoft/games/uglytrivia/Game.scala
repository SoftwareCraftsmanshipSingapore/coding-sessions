package com.adaptionsoft.games.uglytrivia

class Game(playerNames: String*) {
  private val players: Array[Player] = playerNames.toArray.map(name => new Player(name))
  private val playerIndices = Iterator.continually(players.indices.iterator).flatten
  private val questions = {
    List("Pop", "Science", "Sports", "Rock").map {
      cat => cat -> Iterator.range(0, 49).map(i => s"$cat Question $i")
    }.toMap
  }
  private var player: Player = players.head
  private var currentPlayer: Int = playerIndices.next()
  private var isGettingOutOfPenaltyBox: Boolean = false

  addPlayers()

  private def addPlayers(): Unit = {
    playerNames.zipWithIndex.foreach{
      case (p, i) =>
        println(p + " was added")
        println(s"They are player number ${i + 1}")
    }
  }

  def roll(roll: Int): Unit = {
    println(player.name + " is the current player")
    println("They have rolled a " + roll)
    if (player.inPenaltyBox) {
      if (roll % 2 != 0) {
        isGettingOutOfPenaltyBox = true
        println(player.name + " is getting out of the penalty box")
        outOfPenaltyBoxRoll(roll)
      }
      else {
        println(player.name + " is not getting out of the penalty box")
        isGettingOutOfPenaltyBox = false
      }
    }
    else outOfPenaltyBoxRoll(roll)
  }

  private def outOfPenaltyBoxRoll(places: Int): Unit = {
    player.move(places)
    println(player.name + "'s new location is " + player.place)
    askQuestion()
  }

  private def askQuestion(): Unit = {
    println("The category is " + currentCategory)
    println(questions(currentCategory).next())
  }

  private def currentCategory: String = player.place match {
    case 0 | 4 |  8 => "Pop"
    case 1 | 5 |  9 => "Science"
    case 2 | 6 | 10 => "Sports"
    case 3 | 7 | 11 => "Rock"
  }

  def wasCorrectlyAnswered: Boolean = {
    if (player.inPenaltyBox) {
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
    println(player.name + " was sent to the penalty box")
    player.gotoPenaltyBox()
    advancePlayer()
    true
  }

  private def correctlyAnswered(message: String):Boolean = {
    println(message)
    incCurrentPlayerPurse()
    println(player.name + " now has " + player.purse + " Gold Coins.")
    val winner: Boolean = didPlayerWin
    advancePlayer()
    winner
  }
  private def incCurrentPlayerPurse(): Unit = player.addCoin()
  private def advancePlayer(): Unit = {
    currentPlayer = playerIndices.next()
    player = players(currentPlayer)
  }
  private def didPlayerWin: Boolean = !(player.purse == 6)
}

class Player(val name: String) {
  private var _purse: Int = 0
  private var _place: Int = 0
  private var _inPenaltyBox:Boolean = false

  def move(count: Int): Unit = {
    _place += count
    if (_place > 11) _place -= 12
  }
  def place: Int = _place
  def inPenaltyBox: Boolean = _inPenaltyBox
  def gotoPenaltyBox():Unit = _inPenaltyBox = true
  def purse:Int = _purse
  def addCoin():Unit = _purse += 1
}