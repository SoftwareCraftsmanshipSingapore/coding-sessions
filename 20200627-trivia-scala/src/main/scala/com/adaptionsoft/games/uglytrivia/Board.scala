package com.adaptionsoft.games.uglytrivia

class Board(size: Int, categories: Questions.Categories)(implicit log: Log) {
  private val locationCategories: Map[Int, Questions.Category] =
    Iterator.continually(categories.iterator).flatten.take(size).zipWithIndex.map(_.swap).toMap

  def getQuestionCategory(location: Int): Questions.Category = {
    val category = locationCategories(location)
    log.add(s"The category is $category")
    category
  }

  def move(from: Int, steps: Int): Int = (from + steps) % 12
}
