package com.adaptionsoft.games.uglytrivia

class Player(id: Int, val name: String, questions: Questions)(addLog: String => Unit) {
  addLog(s"$name was added")
  addLog(s"They are player number $id")

  private var _purse: Int = 0
  private var _place: Int = 0
  private var _inPenaltyBox:Boolean = false
  private var _isGettingOutOfPenaltyBox: Boolean = false

  def place: Int = _place
  def inPenaltyBox: Boolean = _inPenaltyBox
  def gotoPenaltyBox():Unit = {
    _inPenaltyBox = true
    addLog(s"$name was sent to the penalty box")
  }
  def purse:Int = _purse
  def addCoin():Unit = _purse += 1

  def reportCoins: String = s"$name now has $purse Gold Coins."

  def hasWon: Boolean = purse == 6

  def isGettingOutOfPenaltyBox: Boolean = _isGettingOutOfPenaltyBox

  def takeTurn(rolledNumber: Int): Int = {
    addLog(name + " is the current player")
    addLog("They have rolled a " + rolledNumber)
    if (inPenaltyBox) {
      if (rolledNumber % 2 != 0) {
        _isGettingOutOfPenaltyBox = true
        addLog(name + " is getting out of the penalty box")
        moveAndAskQuestion(rolledNumber)
      }
      else {
        addLog(name + " is not getting out of the penalty box")
        _isGettingOutOfPenaltyBox = false
      }
    }
    else moveAndAskQuestion(rolledNumber)

    rolledNumber
  }

  private def moveAndAskQuestion(places: Int): Unit = {
    move(places)
    questions.pickQuestion(place)
  }

  private def move(count: Int): Unit = {
    _place = + (_place + count) % 12
    addLog(s"$name's new location is $place")
  }
}
