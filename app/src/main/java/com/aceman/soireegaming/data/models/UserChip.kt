package com.aceman.soireegaming.data.models

/**
 * Created by Lionel JOFFRAY - on 21/11/2019.
 */
data class UserChip(val name: String, val group: String, var check : Boolean){

    constructor(): this("chip","group",false)
}