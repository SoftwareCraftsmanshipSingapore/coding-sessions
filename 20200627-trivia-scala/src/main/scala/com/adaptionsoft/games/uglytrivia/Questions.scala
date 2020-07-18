package com.adaptionsoft.games.uglytrivia

class Questions(categories: Questions.Categories)(implicit log: Log) {
  import Questions._
  private val questions: Map[Category, Iterator[Question]] = {
    categories.map {
      cat =>
        val it = Iterator.range(0, cat.questionCount - 1).map(i => new Question(cat, i))
        cat -> Iterator.continually(it).flatten
    }.toMap
  }

  def pickQuestion(category: Questions.Category): Question = {
    val question = questions(category).next()
    log.add(s"$question")
    question
  }
}

object Questions {
  type Categories = List[Category]
  sealed trait Category {
    def questionCount: Int = 50
  }
  private object Category {
    case object Pop     extends Category
    case object Science extends Category
    case object Sports  extends Category
    case object Rock    extends Category
  }

  import Category._
  val categories = List(Pop, Science, Sports, Rock)
  class Question(category: Category, index: Int) {
    override def toString: String = s"$category Question $index"
  }
}


