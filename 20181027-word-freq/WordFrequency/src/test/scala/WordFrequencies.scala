import java.io.File

import org.scalatest.Pending

import scala.io.{Codec, Source}

class WordFrequencies extends FlatSpecTest {

  "it" must "work" in {
    succeed
  }

  "normalize" must "normalize text" in {
    val words:Iterator[String] = Words.load_words("style-wuthering-heights.txt")
    val normalisedWords:List[String] = Words.normalise(words)
    normalisedWords should contain ("gutenberg")
    normalisedWords should not contain (",")
  }

  "cleanup" must "remove stops text" in {
    val words:Iterator[String] = Words.load_words("style-wuthering-heights.txt")
    val normalisedWords:List[String] = Words.normalise(words)
    val cleanWords:List[String] = Words.removeStopWords(normalisedWords)
    cleanWords should contain ("gutenberg")
    cleanWords should not contain ("a")
  }

  "the method frequency" must "give me frequency" in {
    Pending
  }

  "the method load_words" must "load words" in {
    val words:Iterator[String] = Words.load_words("style-wuthering-heights.txt")
    words.next should include ("Gutenberg")
  }

  "the method load_stop_words" must "load words" in {
    val stop_words:Set[String] = StopWords.load_stop_words("style-stop-words.txt")
    stop_words should contain ("after")
  }
}

object StopWords {

    def load_stop_words(filename: String): Set[String] = {
     Source.fromResource(filename).getLines.toList.head.split(',').toSet
  }
}

object Words {


  def normalise(words: Iterator[String]): List[String] = {
    val regex = """[^a-z-]""".r
    val x: String = words.toList.mkString(" ").toLowerCase
    val s: String = regex.replaceAllIn(x, " ")
    s.split(" ").toList
  }

    def load_words(filename: String): Iterator[String] = {
     Source.fromResource(filename)(Codec.UTF8).getLines
  }
}

