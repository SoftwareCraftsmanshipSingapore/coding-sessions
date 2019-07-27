List(1, 5, 10)
  .sliding(2, 1)


val m2 = Map(1 -> 3, 10 -> 1)
val m1 = Map(1 -> 1, 5 -> 1)
m1.map {
  case (k, v) => (k, m2.get(k).map(_ + v).getOrElse(v))
} ++ (m2 -- m1.keys)

