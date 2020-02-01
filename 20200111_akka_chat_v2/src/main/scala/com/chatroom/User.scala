package com.chatroom

import akka.actor.Actor
import com.chatroom.Chatroom.ReceiveMessage
import com.chatroom.User.{ReceiveBroadcast, ReceiveHistory}
import com.chatroom.model.Message

class User (username: String) extends Actor with akka.actor.ActorLogging {

  override def receive: Receive = {
    case rh : ReceiveHistory =>
      log.info(s"Received chat history: ${rh.messages}")
      rh.messages.foreach(println)
      context.sender() ! ReceiveMessage(username, s"Hello from $username")

    case rb : ReceiveBroadcast =>
      log.info(s"""Received broadcast msg "${rb.body}" from ${rb.username} """)
  }
}

object User {

  trait Command

  case class ReceiveBroadcast (username: String, body: String) extends Command
  case class ReceiveHistory (messages: Seq[Message]) extends Command

}
