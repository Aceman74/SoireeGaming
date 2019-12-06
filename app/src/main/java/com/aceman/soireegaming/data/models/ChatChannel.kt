package com.aceman.soireegaming.data.models

/**
 * Use for creating Chat Channel.
 */
data class ChatChannel(val userIds: MutableList<String>) {
    constructor() : this(mutableListOf())
}