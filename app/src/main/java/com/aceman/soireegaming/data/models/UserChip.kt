package com.aceman.soireegaming.data.models

/**
 * Created by Lionel JOFFRAY - on 21/11/2019.3
 *
 * Chip class for console and style choose, saved on firebase.
 */
data class UserChip(val name: String, val group: String, var check : Boolean){

    constructor(): this("chip","group",false)
}