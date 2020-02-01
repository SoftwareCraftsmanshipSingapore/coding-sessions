package com.chatroom

import akka.actor.{ActorSystem, Props}
import com.chatroom.Chatroom.JoinChatroom

object Chat extends App {

  val system = ActorSystem("Chat")
  val listener = system.actorOf(Props[EventListener], "listener")
  system.eventStream.subscribe(listener, classOf[Any])
  val chatroom = system.actorOf(Props[Chatroom], "chatroom")

  chatroom ! JoinChatroom("user1")
  chatroom ! JoinChatroom("user2")

}
