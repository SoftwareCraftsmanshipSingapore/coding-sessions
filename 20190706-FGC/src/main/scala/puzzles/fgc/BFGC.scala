package puzzles.fgc

object BFGC extends App {

  private val fullSide  = Set(m, b, f, g, c)
  private val emptySide = Set.empty[Char]
  private val start: Sides = Sides(fullSide , emptySide)
  private val goal : Sides = Sides(emptySide, fullSide )

  private def solve(currentSides: Sides, branch: List[Sides] = Nil): History = {
    val newSides = currentSides match {
      case s if s.x(m) => s.move2fromXtoY
      case s if s.y(m) => s.move2fromYtoX
    }
    val history: History = newSides
      .toList
      .flatMap {
        case sides if sides.isOK(bf, fg, gc) => List(Nil)
        case sides if branch.contains(sides) => List(Nil)
        case sides if sides == goal          => List(branch ::: List(sides))
        case sides                           => solve(sides, branch ::: List(sides))
      }
    history.filter(_.nonEmpty)
  }

  private val template = List(m, b, f, g, c)

  private val solution:History = solve(start, List(start))
  printSolution(solution, template, 10) //over 6k so taking the first 10
}
