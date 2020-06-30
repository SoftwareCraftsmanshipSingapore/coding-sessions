package com.adaptionsoft.games.uglytrivia

import scala.collection.mutable

class Game(playerNames: String*) {
  private val _log = mutable.Buffer.empty[String]
  private val questions = new Questions(addLog)
  private val players = {
    val ps = playerNames.zipWithIndex.map {
      case (n, i) => new Player(i + 1, n)(addLog)
    }
    Iterator.continually(ps.iterator).flatten.buffered
  }
  private def player: Player = players.head
  private var isGettingOutOfPenaltyBox: Boolean = false

  def roll(roll: Int): Unit = {
    addLog(player.name + " is the current player")
    addLog("They have rolled a " + roll)
    if (player.inPenaltyBox) {
      if (roll % 2 != 0) {
        isGettingOutOfPenaltyBox = true
        addLog(player.name + " is getting out of the penalty box")
        outOfPenaltyBoxRoll(roll)
      }
      else {
        addLog(player.name + " is not getting out of the penalty box")
        isGettingOutOfPenaltyBox = false
      }
    }
    else outOfPenaltyBoxRoll(roll)
  }

  private def outOfPenaltyBoxRoll(places: Int): Unit = {
    player.move(places)
    addLog(player.name + "'s new location is " + player.place)
    questions.askQuestion(player)
  }

  def wasCorrectlyAnswered: Boolean = {
    if (player.inPenaltyBox) {
      if (isGettingOutOfPenaltyBox) //FIXME: never gets out of the penalty box - BUG?
        correctlyAnswered("Answer was correct!!!!")
      else {
        advancePlayer()
        true
      }
    }
    else correctlyAnswered("Answer was corrent!!!!")
  }

  def wrongAnswer: Boolean = {
    addLog("Question was incorrectly answered")
    addLog(player.name + " was sent to the penalty box")
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
  private def didPlayerWin: Boolean = !(player.purse == 6)

  private def addLog(msg: String): Unit = _log += msg
  def log: List[String] = _log.toList
}

class Player(id: Int, val name: String)(addLog: String => Unit) {
  addLog(s"$name was added")
  addLog(s"They are player number $id")

  private var _purse: Int = 0
  private var _place: Int = 0
  private var _inPenaltyBox:Boolean = false

  //FIXME: these advancements will loose places - BUG
  def move(count: Int): Unit = {
    _place += count
    if (_place > 11) _place -= 12
  }
  def place: Int = _place
  def inPenaltyBox: Boolean = _inPenaltyBox
  def gotoPenaltyBox():Unit = _inPenaltyBox = true
  def purse:Int = _purse
  def addCoin():Unit = _purse += 1

  def reportCoins: String = s"$name now has $purse Gold Coins."
}

class Questions(addLog: String => Unit) {
  private val questions = {
    List("Pop", "Science", "Sports", "Rock").map {
      cat => cat -> Iterator.range(0, 49).map(i => s"$cat Question $i")
    }.toMap
  }

  def askQuestion(player:Player): Unit = {
    val category = currentCategory(player.place)
    addLog(s"The category is $category")
    addLog(questions(category).next())
  }

  private def currentCategory(place: Int): String = place match {
    case 0 | 4 |  8 => "Pop"
    case 1 | 5 |  9 => "Science"
    case 2 | 6 | 10 => "Sports"
    case 3 | 7 | 11 => "Rock"
  }
}