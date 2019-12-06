package com.aceman.soireegaming.utils

import com.aceman.soireegaming.data.extensions.backSlashRemover
import org.junit.Assert
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Lionel JOFFRAY - on 06/12/2019.
 */
class UtilsTest {

    @Test
    fun dateToMillis() {
        val year = 1990
        val monthOfYear = 11
        val dayOfMonth = 29
        val cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.MONTH, monthOfYear)
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

         Assert.assertNotNull(cal.timeInMillis)
    }

    @Test
    fun dateWithBSToMillis() {
        val cal = Calendar.getInstance()
        cal.set(1990, 10, 29)

        val dateString = "29/11/1990"
        val string = backSlashRemover(dateString)

        val converted = Utils.dateToMillis(string.substring(4, 8).toInt(), string.substring(2, 4).toInt(), string.substring(0, 2).toInt())
        Assert.assertNotNull(converted)
    }

    @Test
    fun getTodayDate() {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        Assert.assertNotNull(dateFormat.format(Date()))
    }

    @Test
    fun isTime() {
        val time = SimpleDateFormat("HH:mm")
        Assert.assertNotNull(time.format(Date()))
    }

    @Test
    fun chipColor() {
        var text = ""
        var result = -1

        text = "PS3"
        result = when (text) {
            "PS4", "PS3" -> 0
            "Autre" -> 1
            else -> 2
        }
        Assert.assertEquals(result,0)
        Assert.assertTrue(result != 1 && result != 2)

        text = "Autre"
        result = when (text) {
            "PS4", "PS3" -> 0
            "Autre" -> 1
            else -> 3
        }
        Assert.assertEquals(result,1)
        Assert.assertTrue(result != 0 && result != 2)
    }

    @Test
    fun getRandomColor() {
        val rnd = Random()
        val first = rnd.nextInt(256)

        Assert.assertNotNull(first)

        val second= rnd.nextInt(256)

        Assert.assertNotNull(second)
        Assert.assertNotEquals(first,second)
    }
}