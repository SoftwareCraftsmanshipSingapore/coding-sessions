package marsrover

class Rover(
  var position: Position,
  val plateau: Plateau,
  var lastMoveSuccess: Boolean = true
) {
  def move(commands: String): Unit = commands.foreach {
    case 'F' => forward()
    case 'L' => left()
    case 'R' => right()
  }

  def forward(): Unit = {
    plateau.contains(position.forward()) match {
      case Some(p) =>
        lastMoveSuccess = true
        position = p
      case None     =>
        lastMoveSuccess = false
    }
  }

  def left(): Unit = position = position.left()

  def right(): Unit = position = position.right()

  def positionString: String = position.asString
}

object Rover {
  def apply(x: Int, y: Int, d: Direction, p: Plateau) =
    new Rover(Position(Location(x, y), d), p)
}