package com.adaptionsoft.games.uglytrivia

class Questions(addLog: String => Unit, answers: Iterator[Int]) {
  import Questions._
  import Category._
  private val questions: Map[Category, Iterator[Question]] = {
    List(Pop, Science, Sports, Rock).map {
      cat => cat -> Iterator.range(0, 49).map(i => new Question(cat, i, answers))
    }.toMap
  }

  def pickQuestion(place: Int): Unit = {
    val category = currentCategory(place)
    addLog(s"The category is $category")
    addLog(s"${questions(category).next()}")
  }

  private def currentCategory(place: Int): Category = place match {
    case 0 | 4 |  8 => Pop
    case 1 | 5 |  9 => Science
    case 2 | 6 | 10 => Sports
    case 3 | 7 | 11 => Rock
  }

  def correctAnswer: Boolean = answers.next != 7
}

object Questions {
  sealed trait Category
  object Category {
    case object Pop     extends Category
    case object Science extends Category
    case object Sports  extends Category
    case object Rock    extends Category
  }

  class Question(category: Category, index: Int, answers: Iterator[Int]) {
    override def toString: String = s"$category Question $index"
    val checkAnswer: String => Boolean = _ => answers.next() != 7
  }
}