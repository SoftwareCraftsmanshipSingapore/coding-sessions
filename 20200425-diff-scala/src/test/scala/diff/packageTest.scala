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
      ,("Honolulu is next stop."    , "Honour in the lulu steps."  , "Hono__________lulu is next stop._"
                                                                   , "Honour in the lulu ________steps." )
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
}
