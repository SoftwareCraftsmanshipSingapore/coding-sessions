package mines

import mines.SquareBoard.{Cells, RevealedCells}

class SquareBoard(mines: Cells)(implicit sideLength: Int) {

  def allCells: Cells = {
    for {
      x <- Iterator.from(1).take(sideLength)
      y <- Iterator.from(1).take(sideLength)
    } yield Cell(x, y)
  }.toSet

  def reveal(c:Cell): RevealedCells = {
    //@tailrec
    def _reveal(c:Cell, visited: Cells, unvisitedCells: Cells, revealed: RevealedCells): RevealedCells = {
      if (unvisitedCells.isEmpty)
        revealed
      else {
        unvisitedCells.flatMap {
          cell =>
            val _visited: Cells = visited + cell
            val _unvisited: Cells = cell.adjacents(sideLength) -- (_visited ++ mines)
            _reveal(cell, _visited, _unvisited , revealed + (cell -> minesAroundCell(cell)))
        }.toMap
      }
    }
    minesAroundCell(c) match {
      case m if m > 0 => Map(c -> m)
      case 0          => _reveal(c, Set.empty, c.adjacents(sideLength), Map(c -> 0))
    }
  }

  private def minesAroundCell(c: Cell): Int = c.adjacents(sideLength).count (mines)
  def empties(): Cells = allCells -- mines


}
object SquareBoard {
  type Cells = Set[Cell]

  type RevealedCells = Map[Cell, Int]
}
