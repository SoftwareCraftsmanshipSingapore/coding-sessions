import diff.Chunk.{Done, Init}

package object diff {

  def diff(a: String, b: String): Diff = {
    def loop(c: Chunk): List[Chunk.Done] = c match {
      case i:Init => i.diff.flatMap(loop)
      case d:Done => List(d)
    }
    Diff(loop(Chunk(a,b)))
  }

  //tail recursive and necessarily more complicated ensuring stack safety
  def diff2(a: String, b: String): Diff = {
    @scala.annotation.tailrec
    def loop(cs: List[Chunk], chunks: List[Chunk.Done]): List[Chunk.Done] = cs match {
      case (i:Init)::rest => loop(i.diff ::: rest, chunks)
      case (d:Done)::rest => loop(rest, chunks ::: List(d))
      case Nil            => chunks
    }
    Diff(loop(List(Chunk(a,b)), List.empty))
  }
}
