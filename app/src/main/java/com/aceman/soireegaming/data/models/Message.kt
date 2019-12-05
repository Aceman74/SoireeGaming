package com.aceman.soireegaming.data.models

import java.util.*


object MessageType {
    const val TEXT = "TEXT"
    const val IMAGE = "IMAGE"
}

interface Message {
    val time: Date
    val senderName: String
    val senderId: String
    val receiverId: String
    val type: String
}