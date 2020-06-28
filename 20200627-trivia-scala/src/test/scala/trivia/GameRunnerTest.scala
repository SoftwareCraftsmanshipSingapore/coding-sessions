package trivia

import java.nio.file.{Files, Paths}

import com.adaptionsoft.games.trivia.runner.{GameRunner, Result}
import org.scalatest.OneInstancePerTest
import org.scalatest.flatspec.AnyFlatSpecLike
import org.scalatest.matchers.should.Matchers

class GameRunnerTest extends AnyFlatSpecLike with Matchers with OneInstancePerTest {
  private val baselinePath = Paths.get(".").resolve("baseline.text")
  private val nl = sys.props("line.separator")
  private val delimiter = s"AAABBBCCC$nl"

  it should "still work" in {
    val s = new String(Files.readAllBytes(baselinePath))
    s
      .split(delimiter)
      .map(_.split(nl).toList)
      .map {
        case rolls::stops::output =>
          def asInts(s: String) = s.split(',').map(_.toInt).toList
          Result(asInts(rolls), asInts(stops), output.mkString(nl))
      }.foreach {
       r =>
        GameRunner.run(r.rolls.iterator, r.stops.iterator).out.trim shouldBe r.out
    }
  }

  it should "produce a baseline results to be used to verify continual correctness during refactoring" ignore {
    def rollValues: Iterator[Int] = {
      val it = List(1, 2, 3, 4, 5)
      Iterator.continually(scala.util.Random.shuffle(it)).flatten
    }
    def stopValues: Iterator[Int] = {
      val it = List(0, 1, 2, 3, 4, 5, 6, 7, 8)
      Iterator.continually(scala.util.Random.shuffle(it)).flatten
    }
    val results = Iterator
      .continually(GameRunner.run(rollValues, stopValues))
      .take(100)
      .distinctBy(r => r.rolls -> r.stops)
      .toList
    println(results.size)
    val stats = results
      .map(r => r.rolls.size -> r.stops.size)
      .groupBy(identity)
      .view.mapValues(_.size)
    stats.foreach{
      case ((rs,ss), c) => println(s"rolls: $rs, stops: $ss :: freq: $c")
    }
    val baseline = results.mkString(delimiter)
    Files.write(baselinePath, baseline.getBytes())
  }
}