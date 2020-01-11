package com.chatroom

import akka.actor.Actor

class EventListener extends Actor{
  override def receive: Receive = {
    case m => println(s" Event listener: $m")
  }
}
