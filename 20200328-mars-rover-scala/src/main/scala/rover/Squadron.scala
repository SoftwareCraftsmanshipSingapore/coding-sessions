package rover

class Squadron {

  var plateau: Plateau = null
  var rovers: Seq[Rover] = Seq.empty
  var roversCommands: Seq[String] = Seq.empty

  def receive(instructions: String): Unit = {
    val lines: Array[String] = instructions.trim.split("\n\n")
    plateau = parsePlateau(lines(0))
    parseRoverInstructions(lines.drop(1))
  }

  private def parseRoverInstructions(lines: Array[String]): Unit = {
    lines.toList.sliding(2, 2).foreach{
      case position :: commands :: Nil =>
        rovers = rovers :+ parseRover(position)
        roversCommands = roversCommands :+ commands
    }
  }

  private def parsePlateau(line: String): Plateau = {
    val x::y::Nil = line.split(" ").map(_.toInt).toList
    Plateau(x, y)
  }

  private def parseRover(position: String): Rover ={
    val x::y::d::Nil = position.split(" ").toList
    new Rover(Location(x.toInt, y.toInt), Direction.from(d), plateau)
  }

  //execute instructions
  def execute() : Unit =
    rovers.zip(roversCommands).foreach{
      case (r, cs) => r.move(cs)
    }

  def result: String = rovers.map(_.positionString).mkString("\n", "\n\n", "\n")

}
