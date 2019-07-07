package puzzles

object BFGC extends App {
  private val m = 'm' // man
  private val b = 'b' // bear
  private val f = 'f' // fox
  private val g = 'g' // goat
  private val c = 'c' // corn

  private val bf = Set(b, f)
  private val fg = Set(f, g)
  private val gc = Set(g, c)

  private type Side = Set[Char]
  case class State(a: Side, b: Side)
  private type History = List[List[State]]
  private val start: State = State(Set(m, b, f, g, c), Set.empty[Char])
  private val goal : State = State(Set.empty[Char], Set(m, b, f, g, c))

  private def solve(currentState: State, depth: Int, path: String, branch: List[State] = Nil): History = {
//    println(s"$path: ${branch.map(state_toString).mkString("|")}")
    val newStates = currentState match {
      case State(a, z) if a(m) => pairs(a).map(_ + m).map(m => State(a -- m, z ++ m))
      case State(a, z) if z(m) => pairs(z).map(_ + m).map(m => State(a ++ m, z -- m))
    }
    val history: History = newStates
      .toList
      .zipWithIndex
      .flatMap {
        case (State(a, z), _) if bf.subsetOf(a) && z(m) => List(Nil)
        case (State(a, z), _) if fg.subsetOf(a) && z(m) => List(Nil)
        case (State(a, z), _) if gc.subsetOf(a) && z(m) => List(Nil)
        case (State(a, z), _) if bf.subsetOf(z) && a(m) => List(Nil)
        case (State(a, z), _) if fg.subsetOf(z) && a(m) => List(Nil)
        case (State(a, z), _) if gc.subsetOf(z) && a(m) => List(Nil)
        case (state      , _) if branch.contains(state) => List(Nil)
        case (state      , _) if state == goal          => List(branch ::: List(state))
        case (state      , i)                           => solve(state, depth + 1, s"${depth + 1}.$i", branch ::: List(state))
      }
    history.filter(_.nonEmpty)
  }

  private def pairs(s: Side): Set[Side] = for {
     h <- s
     t <- s
  } yield Set(h, t)

  val solution:History = solve(start, 0, "0.0", List(start)).filter(_.nonEmpty)
  println("solution:")
  solution
    .sortBy(_.length)
    .take(10)
    .zipWithIndex
    .map {
      case (l, i) => l
        .zipWithIndex
        .map{
          case (move, j) => f"$j%02d. ${state_toString(move)}"
        }.mkString(s"$i\n","\n","\n")
    }.foreach(println)

  private def state_toString(s: State): String = {
    val x = List(m, b, f, g, c)
    def s_toString(side: Side): String = x.map(e => if (side(e)) e else ' ').mkString
    val State(a, z) = s
    s"(${s_toString(a)}):(${s_toString(z)})"
  }

}
