package craftsman

/*
  Two talking actors.
 */
import akka.actor.{Actor, ActorRef, ActorSystem, PoisonPill, Props, Terminated}
import craftsman.Chat.Command.Start

import scala.language.postfixOps

case object Ping
case object Pong

class Pinger extends Actor {
  var countDown = 10

  def receive = {
    case Pong =>
      println(s"${self.path} received pong, count down $countDown")

      if (countDown > 0) {
        countDown -= 1
        sender() ! Ping
      } else {
        sender() ! PoisonPill
        self ! PoisonPill
      }
  }
}

class Ponger(pinger: ActorRef) extends Actor {
  def receive = {
    case Ping =>
      println(s"${self.path} received ping")
      pinger ! Pong
  }
}

class Chat extends Actor {
  var runningChildren = 0
  override def receive: Receive = {
    case Start =>
      val pinger = context.actorOf(Props[Pinger], "pinger")
      addWatch(pinger)
      val ponger = context.actorOf(Props(classOf[Ponger], pinger), "ponger")
      addWatch(ponger)
      ponger ! Ping

    case Terminated(ar) =>
      runningChildren -= 1
      println(s"${ar.path} stopped")
      if (runningChildren == 0) {
        println("All children terminated")
        context.system.terminate()
      }
  }

  private def addWatch(ar: ActorRef) = {
    context.watch(ar)
    runningChildren += 1
  }
}

object Chat extends App{

  trait Command
  object Command {
    case object Start extends Command
  }
  val system = ActorSystem("pingpong")
  system.actorOf(Props[Chat], "chat") ! Start

}