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
  case class Init(a: String, b: String, c: String) extends Chunk {
    def diff: List[Chunk] =
      (string2chunks(a, c) zip string2chunks(b, c))
        .map (Chunk(_))
        .filter (_.nonEmpty)
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