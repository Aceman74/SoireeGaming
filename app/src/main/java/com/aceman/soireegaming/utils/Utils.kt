package com.aceman.soireegaming.utils

import android.view.View
import android.widget.TextView
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

    fun snackBarPreset(view: View, message: String): Unit {
        val snack = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        val snackView = snack.view
        val txtView =
            snackView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        snackView.setPadding(0, 0, 0, 0)
        txtView.textAlignment = View.TEXT_ALIGNMENT_CENTER
        return snack.show()
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
                "Combat", "Course", "Réflexion", "BattleRoyal",
                "Party Game", "Co-op", "2D", "VR", "Horreur", "Autre"
            ).sortedArray()
        }
    }
}