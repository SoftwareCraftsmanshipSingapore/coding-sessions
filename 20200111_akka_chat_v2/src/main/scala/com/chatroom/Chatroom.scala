package com.chatroom

import java.time.Instant

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.chatroom.Chatroom.{JoinChatroom, LeaveChatroom, ListUsers, ReceiveMessage}
import com.chatroom.User.ReceiveHistory
import com.chatroom.model.Message


class Chatroom extends Actor with ActorLogging{

  var users: Map[String, ActorRef] = Map.empty
  var chatHistory: Seq[Message] = List.empty[Message]

  override def receive: Receive = {
    case JoinChatroom(username) =>
      log.info(s"New user $username joined.")
      //1. Create the actor reference
      val user = context.actorOf(Props(classOf[User], username),  username)

      users = users + (username -> user)
      // Should a new joiner be shown the last n messages?
      user ! ReceiveHistory(chatHistory)

    case LeaveChatroom(username) =>
      users = users - username

    case ListUsers =>
      ???

    case m@ReceiveMessage(username, message) =>
      log.info(s"""Received msg "$message" from $username""")
      chatHistory = chatHistory :+ model.Message(username, message, Instant.now())

      //Send the message to all the actors in the chatroom
      for(u <- users){
        u._2 ! User.ReceiveBroadcast(username, message)
      }

  }
}


object Chatroom {

  trait Command

  case class JoinChatroom(username: String) extends Command
  case class LeaveChatroom(username: String) extends Command
  case object ListUsers extends Command
  case class ReceiveMessage(username: String, body: String) extends Command

}
