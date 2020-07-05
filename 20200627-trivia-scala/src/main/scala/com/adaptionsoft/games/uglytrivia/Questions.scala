package com.adaptionsoft.games.uglytrivia

class Questions(addLog: String => Unit) {
  import Questions._
  import Category._
  private val questions: Map[Category, Iterator[Question]] = {
    List(Pop, Science, Sports, Rock).map {
      cat => cat -> Iterator.range(0, 49).map(i => new Question(cat, i))
    }.toMap
  }

  def pickQuestion(place: Int): Question = {
    val category = currentCategory(place)
    addLog(s"The category is $category")
    val question = questions(category).next()
    addLog(s"$question")
    question
  }

  private def currentCategory(place: Int): Category = place match {
    case 0 | 4 |  8 => Pop
    case 1 | 5 |  9 => Science
    case 2 | 6 | 10 => Sports
    case 3 | 7 | 11 => Rock
  }
}

object Questions {
  sealed trait Category
  object Category {
    case object Pop     extends Category
    case object Science extends Category
    case object Sports  extends Category
    case object Rock    extends Category
  }

  class Question(category: Category, index: Int) {
    override def toString: String = s"$category Question $index"
  }
}