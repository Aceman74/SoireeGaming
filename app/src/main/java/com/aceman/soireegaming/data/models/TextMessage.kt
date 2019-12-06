package com.aceman.soireegaming.data.models

import java.util.*

/**
 * Text message class, used if later Image message is implemented.
 */
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