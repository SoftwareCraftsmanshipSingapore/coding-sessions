package diff

import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.wordspec.AnyWordSpec

class packageTest extends AnyWordSpec with TableDrivenPropertyChecks with Matchers {
  "diff" when {
    val td = Table(
       ("a"                         , "b"                          , "diff a"    , "diff b"              )
      ,("ab"                        , "b"                          , "ab"
                                                                   , "_b"                                )
      ,("aabb"                      , "bbc"                        , "aabb_"
                                                                   , "__bbc"                             )
      ,("aabbdded"                  , "bbcdd"                      , "aabb_dded"
                                                                   , "__bbcdd__"                         )
      ,("aabbcccdded"               , "bbccdd"                     , "aabbcccdded"
                                                                   , "__bbcc_dd__"                       )
      ,("aabb cccdded"              , "bbccdd"                     , "aabb cccdded"
                                                                   , "__bb__ccdd__"                      )
      ,("aabb cccddedfgg"           , "bbccddeffffgggggg"          , "aabb cccdded__fgg____"
                                                                   , "__bb__ccddeffffgggggg"             )
      ,("I speak up"                , "You speak down"             , "I__ speak up__"
                                                                   , "You speak down"                    )
      ,("This is a pencil."         , "The pencil is red."         , "This is a pencil._______"
                                                                   , "The______ pencil is red."          )
      ,("To be or not to be."       , "To have been or not."       , "To _____be__ or not to be."
                                                                   , "To have been or not.______"        )
      ,("Honolulu is next stop."    , "Honour in the lulu stops."  , "Hono__________lulu is next stop_."
                                                                   , "Honour in the lulu ________stops." )
      ,("A book a day is the way!"  , "I book a ticket this way."  , "A book a day is the_ way!"         
                                                                   , "I book a ticket this way."         )
      ,("through the looking glass" , "thorough glass cleaner"     , "th_rough the looking glass________"
                                                                   , "thorough ____________glass cleaner")
    )
    td.forEvery{
      (a, b, da, db) =>
        s"'$a' vs '$b'" in {
          val d = diff2(a, b)
          diff(a, b) shouldBe d
          (d.diffA, d.diffB) shouldBe (da, db)
        }
    }
  }

  "gcs" when {
    val td = Table(
       ("a"  , "b"  , "gcs"        )
      ,("a"  , "b"  , None         )
      ,("a"  , ""   , None         )
      ,("a"  , "ab" , Option("a")  )
      ,("abc", "ab" , Option("ab") )
      ,("abc", "bcd", Option("bc") )
    )
    td.forEvery{
      (a, b, s) =>
        s"a: $a, b: $b, s: $s" in {
          gcs(a, b) shouldBe s
        }
    }

  }

  "substrings" when {
    val td = Table(
       ("string", "subs")
      ,("abc"   , List("abc"   ,"ab", "bc"/*, "a", "b", "c"*/))
      ,("abcd"  , List("abcd"  ,"abc", "bcd", "ab", "bc", "cd"/*, "a", "b", "c", "d"*/))
      ,("abcde" , List("abcde" ,"abcd", "bcde", "abc", "bcd", "cde", "ab", "bc", "cd", "de"/*, "a", "b", "c", "d", "e"*/))
    )
    td.forEvery {
      (s, subs) =>
        s"s:$s => ${subs.mkString(", ")}" in {
          substrings(s).toList shouldBe subs
        }
    }
  }

  "string2chunks" in {
    string2chunks("abc", "a") shouldBe List("", "a", "bc")
    string2chunks("abc", "b") shouldBe List("a","b","c")
    string2chunks("abcdef", "cd") shouldBe List("ab","cd","ef")
  }
}
