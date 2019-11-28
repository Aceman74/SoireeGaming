package com.aceman.soireegaming.data.models


data class ChatChannel(val userIds: MutableList<String>) {
    constructor() : this(mutableListOf())
}