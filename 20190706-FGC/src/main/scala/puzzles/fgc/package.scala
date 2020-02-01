package puzzles

package object fgc {
  val m = 'm' // man
  val b = 'b' // bear
  val f = 'f' // fox
  val g = 'g' // goat
  val c = 'c' // corn

  val bf: Set[Char] = Set(b, f) //bear eats fox
  val fg: Set[Char] = Set(f, g) //fox eats goat
  val gc: Set[Char] = Set(g, c) //goat eats corn

  type Side = Set[Char]
  type History = List[List[Sides]]

  def printSolution(solution:History, template: List[Char], count: Int):Unit = {
    println("solution:")
    solution
      .sortBy(_.length)
      .take(count)
      .zipWithIndex
      .map {
        case (l, i) => l
          .zipWithIndex
          .map{
            case (move, j) => f"$j%02d. ${move.asString(template)}"
          }.mkString(f"${i+1}%05d\n","\n","\n")
      }.foreach(println)
  }

}
