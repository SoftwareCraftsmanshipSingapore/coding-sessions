package rover

case class Plateau(maxX: Int, maxY: Int) {
  def contains(location: Location): Option[Location] =
    Option(location).filter {
      case Location(x, y) =>
        x >= 0 && y >= 0 && x <= maxX && y <= maxY
    }
}

object Plateau {
  def apply(x: String, y: String): Plateau = Plateau(x.toInt, y.toInt)
}
