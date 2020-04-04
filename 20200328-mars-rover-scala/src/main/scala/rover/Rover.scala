package rover

class Rover(
  var location: Location,
  var direction: Direction,
  val plateau: Plateau,
  var lastMoveSuccess: Boolean = true
) {
  def move(commands: String): Unit = commands.foreach {
    case 'F' => forward()
    case 'L' => left()
    case 'R' => right()
  }

  def forward(): Unit = {
    plateau.contains(location.move(direction)) match {
      case Some(nl) =>
        lastMoveSuccess = true
        location = nl
      case None     =>
        lastMoveSuccess = false
    }
  }

  def left(): Unit = direction = direction.left

  def right(): Unit = direction = direction.right

  def position: (Location, Direction) = (location, direction)

  def positionString: String = s"${location.x} ${location.y} $direction"
}

object Rover {
  def apply(x: String, y: String, d: String)(implicit p: Plateau): Rover =
    new Rover(Location(x, y), Direction.from(d), p)
}