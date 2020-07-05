package com.adaptionsoft.games.uglytrivia

class Player(id: Int, val name: String)(addLog: String => Unit) {
  addLog(s"$name was added")
  addLog(s"They are player number $id")

  private var purse: Int = 0
  private var place: Int = 0
  private var inPenaltyBox:Boolean = false

  def tryToMove(rolledNumber: Int): Option[Int] = {
    var isGettingOutOfPenaltyBox: Boolean = false
    addLog(s"They have rolled a $rolledNumber")
    if (inPenaltyBox) {
      isGettingOutOfPenaltyBox = rolledNumber % 2 != 0
      val not = if (isGettingOutOfPenaltyBox) "" else "not "
      addLog(s"$name is ${not}getting out of the penalty box")
    }
    tryToMoveIf(isGettingOutOfPenaltyBox || !inPenaltyBox)(rolledNumber)
  }

  //FIXME: never gets out of the penalty box - BUG?
  def wasCorrectlyAnswered(): Unit = {
    addLog("Answer was correct!!!!")
    purse += 1
    addLog(s"$name now has $purse Gold Coins.")
  }

  def wrongAnswer(): Unit = {
    addLog("Question was incorrectly answered")
    inPenaltyBox = true
    addLog(s"$name was sent to the penalty box")
  }

  def keepPlaying: Boolean = purse != 6

  private def tryToMoveIf(canMove: Boolean)(count: Int) =
    Option(count)
      .filter(_ => canMove)
      .map {
        c =>
          place = + (place + c) % 12
          addLog(s"$name's new location is $place")
          place
      }
}
