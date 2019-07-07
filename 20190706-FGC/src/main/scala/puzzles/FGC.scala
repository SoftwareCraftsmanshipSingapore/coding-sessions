package puzzles

object FGC extends App {
  private val m = 'm' // man
  private val f = 'f' // fox
  private val g = 'g' // goat
  private val c = 'c' // corn

  private val fg = Set(f, g)
  private val gc = Set(g, c)

  private type Side = Set[Char]
  case class State(a: Side, b: Side)
  private type History = List[List[State]]
  private val start: State = State(Set(m, f, g, c), Set.empty[Char])
  private val goal : State = State(Set.empty[Char], Set(m, f, g, c))

  private def solve(currentState: State, depth: Int, path: String, branch: List[State] = Nil): History = {
    val newStates = currentState match {
      case State(a, b) if a(m) => a.map(e => Set(m, e)).map(m => State(a -- m, b ++ m))
      case State(a, b) if b(m) => b.map(e => Set(m, e)).map(m => State(a ++ m, b -- m))
    }
    val history: History = newStates
      .toList
      .zipWithIndex
      .flatMap {
        case (State(a, b), _) if fg.subsetOf(a) && b(m) => List(Nil)
        case (State(a, b), _) if gc.subsetOf(a) && b(m) => List(Nil)
        case (State(a, b), _) if fg.subsetOf(b) && a(m) => List(Nil)
        case (State(a, b), _) if gc.subsetOf(b) && a(m) => List(Nil)
        case (state      , _) if branch.contains(state) => List(Nil)
        case (state      , _) if state == goal          => List(branch ::: List(state))
        case (state      , i)                           => solve(state, depth + 1, s"${depth + 1}.$i", branch ::: List(state))
      }
    history.filter(_.nonEmpty)
  }

  val solution:History = solve(start, 0, "0.0", List(start)).filter(_.nonEmpty)
  println("solution:")
  solution.map {
    l => l
      .zipWithIndex
      .map{
        case (move, i) => f"$i% 2d. ${state_toString(move)}"
      }.mkString("","\n","\n")
  }.foreach(println)

  private def state_toString(s: State): String = {
    val x = List(m, f, g, c)
    def s_toString(side: Side): String = x.map(e => if (side(e)) e else ' ').mkString//(",")
    val State(a, b) = s
    s"(${s_toString(a)}):(${s_toString(b)})"
  }
}
