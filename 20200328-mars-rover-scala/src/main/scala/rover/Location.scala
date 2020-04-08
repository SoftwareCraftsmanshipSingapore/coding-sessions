package rover

import rover.Direction.{E, N, S, W}

case class Location(x: Int, y: Int) {
  def move(direction: Direction): Location =
    direction match {
      case N => copy(y = y + 1)
      case S => copy(y = y - 1)
      case E => copy(x = x + 1)
      case W => copy(x = x - 1)
    }
}

object Location {
  def apply(x: String, y: String): Location = Location(x.toInt, y.toInt)
}
