package rover

import rover.Direction._

class Rover(
  var location: Location,
  var direction: Direction,
  val plateau: Plateau,
  var lastMoveSuccess: Boolean = true
) {
  def forward(): Unit = {
    if (isMoveValid(computeNewLocation)) {
      lastMoveSuccess = true
      location = computeNewLocation
    }
    else {
      lastMoveSuccess = false
    }
  }

  private def computeNewLocation =
    direction match {
      case N => location.incY
      case S => location.decY
      case E => location.incX
      case W => location.decX
    }

  private def isMoveValid(nl: Location): Boolean =
    nl.x >= 0 && nl.y >= 0 && nl.x <= plateau.maxX && nl.y <= plateau.maxY

  def left(turns: Int = 1): Unit = {
    direction = direction match {
      case N => W
      case S => E
      case E => N
      case W => S
    }
  }

  def right(): Unit = {
    left()
    left()
    left()
  }

  def move(commands: String): Unit = commands.foreach {
    case 'F' => forward()
    case 'L' => left()
    case 'R' => right()
  }

  def position: (Location, Direction) = (location, direction)

  def positionString: String = s"${location.x} ${location.y} $direction"
}

case class Plateau(maxX: Int, maxY: Int)

case class Location(x: Int, y: Int) {
  def incY: Location = copy(y = y + 1)
  def decY: Location = copy(y = y - 1)

  def incX: Location = copy(x = x + 1)
  def decX: Location = copy(x = x - 1)
}

sealed trait Direction
object Direction {
  case object N extends Direction
  case object S extends Direction
  case object E extends Direction
  case object W extends Direction

  def from(s: String): Direction =
    s match {
      case "N" => N
      case "S" => S
      case "E" => E
      case "W" => W
    }
}