package rover

sealed abstract class Direction private (l: => Direction, r: => Direction) {
  def left: Direction = l
  def right: Direction = r
}

object Direction {
  case object N extends Direction(W, E)
  case object S extends Direction(E, W)
  case object E extends Direction(N, S)
  case object W extends Direction(S, N)

  def from(s: String): Direction =
    s match {
      case "N" => N
      case "S" => S
      case "E" => E
      case "W" => W
    }
}