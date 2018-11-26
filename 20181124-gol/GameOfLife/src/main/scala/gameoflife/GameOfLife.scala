package gameoflife

case class World(aliveCells: Cell*){

  def evolve(): World = {
    val stayAlive = aliveCells.filter(getNumberOfAliveNeighbours(_) == 3)
    val becomeAlive: Seq[Cell] = aliveCells.flatMap {
      c =>
           getNeighbours(c)
          .flatMap(getNeighbours)
          .filter(getNumberOfAliveNeighbours(_) == 3)
    }
    val allCells = stayAlive ++ becomeAlive
    World(allCells.distinct:_*)
  }

  def getNumberOfAliveNeighbours(c: Cell) : Int = {
    getNeighbours(c).filter(isAlive).length
  }

  private def getNeighbours(c: Cell): List[Cell] = {
    for{
      x <- (c.x - 1) to (c.x + 1)
      y <- (c.y - 1) to (c.y + 1)
    } yield Cell(x, y)
  }.filterNot(_ == c).toList

  def isAlive(cell: Cell): Boolean = {
    aliveCells.contains(cell)
  }
}
case class Cell(x: Int, y: Int)

object GameOfLife {

}
