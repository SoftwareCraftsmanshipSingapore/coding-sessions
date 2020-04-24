package marsrover

import marsrover.Move._
import marsrover.Rover.MoveResult
import org.scalactic.{Bad, ErrorMessage, Good, Or}

import scala.collection.mutable

class Rover(
  val initialPosition: Position,
  val plateau: Plateau
) {
  private var _position = initialPosition
  private var _moves: Moves = List.empty
  private var _failureReason: Option[String] = None
  private val _commandQueue: mutable.Queue[Move] = mutable.Queue.empty

  mutable.Queue
  def canMove: Boolean = _commandQueue.nonEmpty && failureReason.isEmpty

  def takeCommands(commands: Move*): Unit = commands.foreach(_commandQueue.enqueue)

  def doOneCommand(): Unit = {
    nextMove().foreach {
      c =>
        moveOne(_position, c) match {
          case Good(np)   =>
            _position = np
            _moves :+= c
          case Bad(error) =>
            _failureReason = Option(error)
        }
    }
  }

  private def nextMove(): Option[Move] =
    if (canMove)
      Option(_commandQueue.dequeue())
    else None

  def move(commands: Move*): Unit = {
    @scala.annotation.tailrec
    def loop(commands: Moves, position: Position, moves: Moves): (Position, Moves, Option[ErrorMessage]) = commands match {
      case Nil   => (position, moves, None)
      case m::ms => moveOne(position, m) match {
        case Good(p)           => loop(ms, p, moves :+ m)
        case Bad(errorMessage) => (position, moves, Option(errorMessage))
      }
    }
    val (np, ms, fr) = loop(commands.toList, _position, _moves)
    _position = np
    _moves = ms
    _failureReason = fr
  }

  private def moveOne(p: Position, m: Move): MoveResult = m match {
    case F => forward(p)
    case L => left(p)
    case R => right(p)
  }

  private def forward(p: Position): MoveResult =
    plateau
      .contains(p.forward())
      .map(Good(_))
      .getOrElse(Bad("encountered edge of plateau"))

  private def left(p: Position):MoveResult = Good(p.left())

  private def right(p: Position): MoveResult = Good(p.right())

  def positionString: String = _position.asString

  def moves: Moves = _moves
  def failureReason: Option[String] = _failureReason
  def position: Position = _position
}

object Rover {
  private type MoveResult = Position Or ErrorMessage
  def apply(x: Int, y: Int, d: Direction, p: Plateau) =
    new Rover(Position(Location(x, y), d), p)
}



