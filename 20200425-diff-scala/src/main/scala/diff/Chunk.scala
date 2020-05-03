package diff

sealed trait Chunk {
  def nonEmpty: Boolean = true
}

object Chunk {
  def apply(c: (String, String)): Chunk = {
    import Done._
    c match {
      case (a, b) => if (a == b) Same(a) else gcs(a, b).map(Init(a, b, _)).getOrElse(Diff(a, b))
    }
  }

  private def gcs(a: String, b: String): Option[String] = {
    val (s, l) = if (a < b) a -> b else b -> a
    s.length match {
      case 0 => None
      case 1 => Option(s).find(l.contains)
      case _ => substrings(s).find(l.contains)
    }
  }

  private def substrings(string: String): Iterator[String] = {
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

  case class Init(a: String, b: String, c: String) extends Chunk {
    def diff: List[Chunk] =
      (string2chunks(a, c) zip string2chunks(b, c))
        .map (Chunk(_))
        .filter (_.nonEmpty)

    private def string2chunks(s: String, sub: String): List[String] = {
      val Array(b, a) = s.split(sub, 2)
      List(b, sub, a)
    }
  }

  trait Done extends Chunk {
    def diffA: String
    def diffB: String
  }
  object Done {
    case class Same(s: String) extends Done {
      override def nonEmpty: Boolean = s.nonEmpty
      def diffA: String = s
      def diffB: String = s
    }
    case class Diff(a: String, b: String) extends Done {
      def diffA: String = zz(a, b)
      def diffB: String = zz(b, a)
      private def zz(x: String, y: String) = {
        val (xl, yl) = (x.length, y.length)
        if (xl > yl) x else x + "_" * (yl - xl)
      }
    }
  }
}