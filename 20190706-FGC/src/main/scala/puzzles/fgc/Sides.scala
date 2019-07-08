package puzzles.fgc

case class Sides(x: Side, y: Side) {
  def isOK(subsets: Set[Char]*): Boolean = isOK_X(subsets) || isOK_Y(subsets)
  private def isOK_X(subsets: Seq[Set[Char]]) = subsets.exists(_.subsetOf(x) && y(m))
  private def isOK_Y(subsets: Seq[Set[Char]]) = subsets.exists(_.subsetOf(y) && x(m))

  def move1fromXtoY: Set[Sides] = x map(Set(m, _)) map (m => copy(x -- m, y ++ m))
  def move1fromYtoX: Set[Sides] = y map(Set(m, _)) map (m => copy(x ++ m, y -- m))

  def move2fromXtoY: Set[Sides] = pairs(x) map(_ + m) map (m => copy(x -- m, y ++ m))
  def move2fromYtoX: Set[Sides] = pairs(y) map(_ + m) map (m => copy(x ++ m, y -- m))

  private def pairs(s: Side): Set[Side] = for {
    h <- s
    t <- s
  } yield Set(h, t)

  def asString(template: List[Char]): String = {
    def s_toString(side: Side): String = template.map(e => if (side(e)) e else ' ').mkString
    s"(${s_toString(x)}):(${s_toString(y)})"
  }

}
