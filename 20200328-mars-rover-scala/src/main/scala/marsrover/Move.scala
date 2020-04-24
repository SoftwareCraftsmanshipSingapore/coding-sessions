package marsrover

sealed trait Move

object Move {
  type Moves = List[Move]

  case object L extends Move
  case object R extends Move
  case object F extends Move

  def apply(c: Char): Move = c match {
    case 'L' => L
    case 'R' => R
    case 'F' => F
  }
}