package com.aceman.soireegaming.data.extensions

import org.junit.Assert
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Lionel JOFFRAY - on 06/12/2019.
 */
class StringExtensionsKtTest {

    @Test
    fun backSlashRemover() {
        val string = "CEC/I/ EST/ UN// T/EST/"
        val s: StringBuilder = StringBuilder(string.replace("/", ""))
        Assert.assertNotNull(s.toString())
        Assert.assertTrue(s.toString() == "CECI EST UN TEST")
    }

    @Test
    fun hourSetting() {
        val hour = 5
        val minutes = 2
        val setHour : StringBuilder = if(hour.toString().length < 2){
            StringBuilder("0$hour")
        }else StringBuilder(hour.toString())
        val setMinutes : StringBuilder = if(minutes.toString().length < 2){
            StringBuilder("0$minutes")
        }else StringBuilder(minutes.toString())

        val s: StringBuilder = StringBuilder("$setHour:$setMinutes")
        Assert.assertNotNull(s.toString())
        Assert.assertTrue(s.toString() == "05:02")
    }

    @Test
    fun customTimeStamp() {
        val dateFormat = SimpleDateFormat("HHmmssdd")
        Assert.assertNotNull(dateFormat.format(Date()).toString())
    }
}