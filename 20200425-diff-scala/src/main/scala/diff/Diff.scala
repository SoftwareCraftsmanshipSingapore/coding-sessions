package diff

case class Diff(d: List[Chunk.Done]) {
  def diffA: String = d.map(_.diffA).mkString
  def diffB: String = d.map(_.diffB).mkString
}
