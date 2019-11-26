package com.aceman.soireegaming.data.models

/**
 * Created by Lionel JOFFRAY - on 26/11/2019.
 */

data class EventsList(var eventsList: MutableList<EventInfos>){

    constructor(): this(mutableListOf())
}