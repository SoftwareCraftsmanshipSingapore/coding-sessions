package marsrover

case class Plateau(maxX: Int, maxY: Int) {
  def contains(position: Position): Option[Position] =
    Option(position).filter {
      case Position(Location(x, y), _) =>
        x >= 0 && y >= 0 && x <= maxX && y <= maxY
    }
}

object Plateau {
  def apply(x: String, y: String): Plateau = Plateau(x.toInt, y.toInt)
}
