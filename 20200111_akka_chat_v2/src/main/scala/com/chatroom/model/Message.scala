package com.chatroom.model

import java.time.Instant

case class Message (username: String, body: String, timestamp: Instant)
