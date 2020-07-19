package com.adaptionsoft.games.uglytrivia

import com.adaptionsoft.games.uglytrivia.Questions.Question

class Player(id: Int, val name: Player.Name, board: Board, questions: Questions)(implicit log: Log) {
  private var question = Option.empty[Question]

  log.add(s"$name was added")
  log.add(s"They are player number $id")

  private var purse: Int = 0
  private var inPenaltyBox:Boolean = false

  def tryToMove(rolledNumber: Int): Unit = {
    var isGettingOutOfPenaltyBox: Boolean = false
    log.add(s"They have rolled a $rolledNumber")
    if (inPenaltyBox) {
      isGettingOutOfPenaltyBox = rolledNumber % 2 != 0
      val not = if (isGettingOutOfPenaltyBox) "" else "not "
      log.add(s"$name is ${not}getting out of the penalty box")
    }
    question = tryToMoveIf(isGettingOutOfPenaltyBox || !inPenaltyBox)(rolledNumber)
      .map(board.getQuestionCategory)
      .map(questions.pickQuestion)
  }

  //FIXME: never gets out of the penalty box - BUG?
  def wasCorrectlyAnswered(): Unit = question.foreach {
    _ =>
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
      .map(board.move(name, _))
      .map(_ => name)
}

object Player {
  case class Name(underlying: String) extends AnyVal {
    override def toString: String = underlying
  }
}

class Players private(players: Iterator[Player])(implicit log: Log) {

  private var currentPlayer: Player = _

  def advancePlayer(): Unit = {
    currentPlayer = players.next()
    log.add(s"${currentPlayer.name} is the current player")
  }

  def move(rolledNumber: Int): Unit = currentPlayer.tryToMove(rolledNumber)

  def keepPlaying: Boolean = //FIXME: name???
    if (currentPlayer.keepPlaying) {
      advancePlayer()
      true
    } else false

  def wasCorrectlyAnswered(): Unit = currentPlayer.wasCorrectlyAnswered()

  def wrongAnswer(): Unit = currentPlayer.wrongAnswer()
}
object Players {
  def apply(names: Names, board: Board, questions: Questions)(implicit log: Log): Players = {
    val players: Iterator[Player] = {
      val ps = names.names.zipWithIndex.map {
        case (n, i) => new Player(i + 1, n, board, questions)
      }
      Iterator.continually(ps.iterator).flatten
    }

    new Players(players)
  }

  case class Names(names: Seq[Player.Name])
  object Names {
    def apply(player1Name: Player.Name, player2Name: Player.Name): Names =
      new Names(Seq(player1Name, player2Name))
    def apply(player1Name: Player.Name, player2Name: Player.Name, player3Name: Player.Name): Names =
      new Names(Seq(player1Name, player2Name, player3Name))
    def apply(player1Name: Player.Name, player2Name: Player.Name, player3Name: Player.Name, player4Name: Player.Name): Names =
      new Names(Seq(player1Name, player2Name, player3Name, player4Name))
    def apply(player1Name: Player.Name, player2Name: Player.Name, player3Name: Player.Name, player4Name: Player.Name, player5Name: Player.Name): Names =
      new Names(Seq(player1Name, player2Name, player3Name, player4Name, player5Name))
    def apply(player1Name: Player.Name, player2Name: Player.Name, player3Name: Player.Name, player4Name: Player.Name, player5Name: Player.Name, player6Name: Player.Name): Names =
      new Names(Seq(player1Name, player2Name, player3Name, player4Name, player5Name, player6Name))
  }
}
