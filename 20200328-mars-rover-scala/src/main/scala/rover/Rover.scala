package rover

class Rover(var position: Position, d: Char) {
  def move(): Unit = {
    position = if (d == 'S') position.decY else position.incY
  }

  def current: (Position, Char) = (position, d)
}


case class Position(x: Int, y: Int) {
  def incY: Position = copy(y = y + 1)
  def decY: Position = copy(y = y - 1)
}