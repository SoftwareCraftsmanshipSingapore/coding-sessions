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
    def loop(c: List[Chunk], chunks: List[Chunk.Done]): List[Chunk.Done] = c match {
      case (i:Init)::rest => loop(i.diff ::: rest, chunks)
      case (d:Done)::rest => loop(rest, chunks ::: List(d))
      case Nil            => chunks
    }
    Diff(loop(List(Chunk(a,b)), List.empty))
  }

  def gcs(a: String, b: String): Option[String] = {
    val (s, l) = if (a < b) a -> b else b -> a
    s.length match {
      case 0 => None
      case 1 => Option(s).find(l.contains)
      case _ => substrings(s).find(l.contains)
    }
  }

  def string2chunks(s: String, sub: String): List[String] = {
    s.indexOf(sub) match {
      case -1 => List(s)
      case n  =>
        
        List(
        s.take(n),
        sub,
        s.drop(n + sub.length)
      )
    }
  }

  def substrings(string: String): Iterator[String] = {
    val minChunkLength = 2
    @scala.annotation.tailrec
    def loop(string: String, subSize: Int, subs: Iterator[String]): Iterator[String] = {
      val allSubs = subs ++ string.toSeq.sliding(subSize, 1).map(_.toString)
      if (subSize > minChunkLength) loop(string, subSize - 1, allSubs) else allSubs
    }
    if (string.length < minChunkLength)
      Iterator(string)
    else
      loop(string, string.length - 1, Iterator(string))
  }
}
