package com.aceman.soireegaming.utils

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView
import com.aceman.soireegaming.R
import com.aceman.soireegaming.data.extensions.backSlashRemover
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Lionel JOFFRAY - on 18/11/2019.
 */

/**
 * Default snackbar for app.
 */

object Utils {

    fun snackBarPreset(view: View, message: String) {
        val snack = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        val snackView = snack.view
        val txtView =
            snackView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        snackView.setPadding(0, 0, 0, 0)
        snackView.setBackgroundResource(R.color.primaryColor)
        txtView.textAlignment = View.TEXT_ALIGNMENT_CENTER
        txtView.setTextColor(Color.WHITE)
        return snack.show()
    }

    /**
     * Convert Date to millis for comparing in SearchActivity.
     */
    fun dateToMillis(year: Int, monthOfYear: Int, dayOfMonth: Int): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.MONTH, monthOfYear)
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        return cal.timeInMillis
    }


    /**
     * Convert date with / custom format to millis.
     */
    fun dateWithBSToMillis(dateString: String): Long {
        var string = backSlashRemover(dateString)
        if(string.length == 7)
            string = "0$string"

        return dateToMillis(string.substring(4, 8).toInt(), string.substring(2, 4).toInt(), string.substring(0, 2).toInt())
    }

    /**
     * Conversion de la date d'aujourd'hui en un format plus approprié
     * @return
     */
    val todayDate: String
        get() {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy")
            return dateFormat.format(Date())
        }

    val isTime: String
        get() {
            val time = SimpleDateFormat("HH:mm")
            return time.format(Date())
        }
    /**
     * Start a fade animation, for Recyclerview items.
     */
    fun setFadeAnimation(view: View, context: Context) {
        val anim = AnimationUtils.loadAnimation(context, R.anim.fade_in)
        view.startAnimation(anim)
    }

    fun chipColor(chip: Chip) : Int {
        return when (chip.text) {
            "PS4", "PS3" -> R.color.playstation
            "Xbox 360", "Xbox One" -> R.color.xbox
            "PS2", "Xbox" -> android.R.color.black
            "Dreamcast" -> R.color.dreamcast
            "Gamecube" -> R.color.gamecube
            "Wii", "Wii U", "3DS", "Switch" -> R.color.wii
            "Android" -> R.color.android
            "PC" -> R.color.pc
            "Autre" -> R.color.autre
            else -> R.color.primaryLightColor
        }
    }
    fun getRandomColor(): Int {
        val rnd = Random()
        return Color.argb(
            255,
            rnd.nextInt(256),
            rnd.nextInt(256),
            rnd.nextInt(256)
        )

    }
    /**
     * This object is used in numberpicker, makes save easier in Estate (only Int no full string).
     */
    object ListOfString {

        fun listOfConsole(): Array<String> {
            return arrayOf(
                "PS4", "Xbox One", "PC", "Switch", "Wii U",
                "PS3", "Xbox 360", "3DS", "Xbox", "PS2",
                "Wii", "Gamecube", "Dreamcast", "Android",
                "Autre"
            ).sortedArray()
        }
        fun listOfStyle(): Array<String> {
            return arrayOf(
                "Aventure", "Course", "FPS", "Stratégie", "MMORPG",
                "MMO", "RPG", "Plate-forme", "Simulation", "Sport",
                "Combat", "Réflexion", "BattleRoyal", "Party Game",
                "Co-op", "2D", "VR", "Horreur", "Autre"
            ).sortedArray()
        }
    }
}