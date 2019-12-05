package com.aceman.soireegaming.data.models

import java.util.*


data class TextMessage(val text: String,
                       override val time: Date,
                       override val senderName: String,
                       override val senderId: String,
                       override val receiverId: String,
                       override val type: String = MessageType.TEXT
)
    : Message {
    constructor() : this("", Date(0),"", "","")
}