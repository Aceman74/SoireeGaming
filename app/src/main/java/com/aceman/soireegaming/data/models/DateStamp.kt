package com.aceman.soireegaming.data.models

/**
 * Created by Lionel JOFFRAY - on 04/12/2019.
 *
 * Create a DateStamp for messaging.
 */
data class DateStamp(var date: String, var hour: String){

    constructor(): this("","")
}