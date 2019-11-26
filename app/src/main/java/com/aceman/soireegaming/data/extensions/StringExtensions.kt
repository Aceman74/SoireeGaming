package com.aceman.soireegaming.data.extensions

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Lionel JOFFRAY - on 26/11/2019.
 */

/**
 * Remove backslash to convert date saved to millis.
 */
fun String.backSlashRemover(string: String): String {
    val s: StringBuilder = StringBuilder(string.replace("/", ""))
    return s.toString()
}

/**
 * Remove backslash to convert date saved to millis.
 */
fun String.hourSetting(hour: Int, minutes: Int): String {
    var setHour : StringBuilder = if(hour.toString().length < 2){
        StringBuilder("0$hour")
    }else StringBuilder(hour.toString())
    var setMinutes : StringBuilder = if(minutes.toString().length < 2){
        StringBuilder("0$minutes")
    }else StringBuilder(minutes.toString())

    val s: StringBuilder = StringBuilder("$setHour:$setMinutes")
    return s.toString()
}
/**
 * Use to save event.
 */
fun String.customTimeStamp(): String {
    val dateFormat = SimpleDateFormat("HHmmssdd")
    return dateFormat.format(Date()).toString()
}