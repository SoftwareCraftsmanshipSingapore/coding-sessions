package com.adaptionsoft.games.uglytrivia

class Player(id: Int, val name: String)(implicit log: Log) {
  log.addLog(s"$name was added")
  log.addLog(s"They are player number $id")

  private var purse: Int = 0
  private var place: Int = 0
  private var inPenaltyBox:Boolean = false

  def tryToMove(rolledNumber: Int): Option[Int] = {
    var isGettingOutOfPenaltyBox: Boolean = false
    log.addLog(s"They have rolled a $rolledNumber")
    if (inPenaltyBox) {
      isGettingOutOfPenaltyBox = rolledNumber % 2 != 0
      val not = if (isGettingOutOfPenaltyBox) "" else "not "
      log.addLog(s"$name is ${not}getting out of the penalty box")
    }
    tryToMoveIf(isGettingOutOfPenaltyBox || !inPenaltyBox)(rolledNumber)
  }

  //FIXME: never gets out of the penalty box - BUG?
  def wasCorrectlyAnswered(): Unit = {
    log.addLog("Answer was correct!!!!")
    purse += 1
    log.addLog(s"$name now has $purse Gold Coins.")
  }

  def wrongAnswer(): Unit = {
    log.addLog("Question was incorrectly answered")
    inPenaltyBox = true
    log.addLog(s"$name was sent to the penalty box")
  }

  def keepPlaying: Boolean = purse != 6 //FIXME: name???

  private def tryToMoveIf(canMove: Boolean)(count: Int) =
    Option(count)
      .filter(_ => canMove)
      .map {
        c =>
          place = + (place + c) % 12
          log.addLog(s"$name's new location is $place")
          place
      }
}

class Players private(val names: Seq[String])(implicit log: Log) {
  private val players = {
    val ps = names.zipWithIndex.map {
      case (n, i) => new Player(i + 1, n)
    }
    Iterator.continually(ps.iterator).flatten
  }

  private var currentPlayer: Player = _

  def advancePlayer(): Unit = {
    currentPlayer = players.next()
    log.addLog(s"${currentPlayer.name} is the current player")
  }

  def tryToMove(rolledNumber: Int): Option[Int] = currentPlayer.tryToMove(rolledNumber)

  def keepPlaying: Boolean = //FIXME: name???
    if (currentPlayer.keepPlaying) {
      advancePlayer()
      true
    } else false

  def wasCorrectlyAnswered(): Unit = currentPlayer.wasCorrectlyAnswered()

  def wrongAnswer(): Unit = currentPlayer.wrongAnswer()
}
object Players {
  def apply(player1Name: String, player2Name: String)(implicit log: Log): Players =
    new Players(Seq(player1Name, player2Name))
  def apply(player1Name: String, player2Name: String, player3Name: String)(implicit log: Log): Players =
    new Players(Seq(player1Name, player2Name, player3Name))
  def apply(player1Name: String, player2Name: String, player3Name: String, player4Name: String)(implicit log: Log): Players =
    new Players(Seq(player1Name, player2Name, player3Name, player4Name))
  def apply(player1Name: String, player2Name: String, player3Name: String, player4Name: String, player5Name: String)(implicit log: Log): Players =
    new Players(Seq(player1Name, player2Name, player3Name, player4Name, player5Name))
  def apply(player1Name: String, player2Name: String, player3Name: String, player4Name: String, player5Name: String, player6Name: String)(implicit log: Log): Players =
    new Players(Seq(player1Name, player2Name, player3Name, player4Name, player5Name, player6Name))
}
