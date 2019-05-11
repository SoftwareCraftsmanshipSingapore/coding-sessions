package mines

case class Cell(x: Int, y:Int) {
  def adjacents(side:Int):Set[Cell] = Cell.adjacent.map {
    case (_x, _y) => Cell(x + _x, y + _y)
  }.filter(onBoard(side))

  private def onBoard(side: Int)(c: Cell): Boolean = c.x > 0 && c.y > 0 && c.x <= side && c.y <= side

}

object Cell {
  val adjacent = Set(
     -1 -> -1
    ,-1 ->  0
    ,-1 ->  1
    , 0 -> -1
    , 0 ->  1
    , 1 -> -1
    , 1 ->  0
    , 1 ->  1
  )
}