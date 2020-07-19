package com.adaptionsoft.games.uglytrivia

class Board(size: Int, categories: Questions.Categories, playerNames: Players.Names)(implicit log: Log) {
  private val locationCategories: Map[Int, Questions.Category] =
    Iterator.continually(categories.iterator).flatten.take(size).zipWithIndex.map(_.swap).toMap

  private var playerLocations = playerNames.names.map(name => name -> 0).toMap

  def getQuestionCategory(playerName: Player.Name): Questions.Category = {
    val category = locationCategories(playerLocation(playerName))
    log.add(s"The category is $category")
    category
  }

  def move(playerName: Player.Name, steps: Int): Unit = {
    val newLocation = (playerLocation(playerName) + steps) % size
    playerLocations = playerLocations + (playerName -> newLocation)
    log.add(s"$playerName's new location is $newLocation")
  }

  private def playerLocation(playerName: Player.Name): Int = playerLocations(playerName) //TODO: handle unknown player
}
