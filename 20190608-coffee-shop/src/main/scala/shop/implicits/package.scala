package shop

package object implicits {

  import scala.language.implicitConversions
  import events._

  implicit def Event2Events(e: Event): Events = Seq(e)
}
