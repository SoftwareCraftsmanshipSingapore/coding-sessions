package com.adaptionsoft.games.uglytrivia

class Player(id: Int, val name: String, board: Board)(implicit log: Log) {
  log.add(s"$name was added")
  log.add(s"They are player number $id")

  private var purse: Int = 0
  private var place: Int = 0
  private var inPenaltyBox:Boolean = false

  def tryToMove(rolledNumber: Int): Option[Int] = {
    var isGettingOutOfPenaltyBox: Boolean = false
    log.add(s"They have rolled a $rolledNumber")
    if (inPenaltyBox) {
      isGettingOutOfPenaltyBox = rolledNumber % 2 != 0
      val not = if (isGettingOutOfPenaltyBox) "" else "not "
      log.add(s"$name is ${not}getting out of the penalty box")
    }
    tryToMoveIf(isGettingOutOfPenaltyBox || !inPenaltyBox)(rolledNumber)
  }

  //FIXME: never gets out of the penalty box - BUG?
  def wasCorrectlyAnswered(): Unit = {
    log.add("Answer was correct!!!!")
    purse += 1
    log.add(s"$name now has $purse Gold Coins.")
  }

  def wrongAnswer(): Unit = {
    log.add("Question was incorrectly answered")
    inPenaltyBox = true
    log.add(s"$name was sent to the penalty box")
  }

  def keepPlaying: Boolean = purse != 6 //FIXME: name???

  private def tryToMoveIf(canMove: Boolean)(steps: Int) =
    Option(steps)
      .filter(_ => canMove)
      .map {
        c =>
          place = board.move(place, c)
          log.add(s"$name's new location is $place")
          place
      }
}

class Players private(names: Seq[String], board: Board)(implicit log: Log) {
  private val players = {
    val ps = names.zipWithIndex.map {
      case (n, i) => new Player(i + 1, n, board)
    }
    Iterator.continually(ps.iterator).flatten
  }

  private var currentPlayer: Player = _

  def advancePlayer(): Unit = {
    currentPlayer = players.next()
    log.add(s"${currentPlayer.name} is the current player")
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
  def apply(board: Board)(implicit log: Log): Names = new Names(board)
  class Names(board: Board)(implicit log: Log) {
    def apply(player1Name: String, player2Name: String): Players =
      new Players(Seq(player1Name, player2Name), board)
    def apply(player1Name: String, player2Name: String, player3Name: String): Players =
      new Players(Seq(player1Name, player2Name, player3Name), board)
    def apply(player1Name: String, player2Name: String, player3Name: String, player4Name: String): Players =
      new Players(Seq(player1Name, player2Name, player3Name, player4Name), board)
    def apply(player1Name: String, player2Name: String, player3Name: String, player4Name: String, player5Name: String): Players =
      new Players(Seq(player1Name, player2Name, player3Name, player4Name, player5Name), board)
    def apply(player1Name: String, player2Name: String, player3Name: String, player4Name: String, player5Name: String, player6Name: String): Players =
      new Players(Seq(player1Name, player2Name, player3Name, player4Name, player5Name, player6Name), board)
  }
}
