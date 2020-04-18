package marsrover

case class Position(location: Location, direction: Direction) {
  def left(): Position = copy(direction = direction.left)
  def right(): Position = copy(direction = direction.right)
  def forward(): Position = copy(location = location.move(direction))
  def asString: String = s"${location.x} ${location.y} $direction"
}

object Position {
  def apply(x: String, y: String, d: String): Position = Position(Location(x, y), Direction(d))
  def apply(x: Int, y: Int, d: Direction): Position = Position(Location(x, y), d)
}