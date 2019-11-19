package com.aceman.soireegaming.utils

import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar

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
        val txtView = snackView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        snackView.setPadding(0, 0, 0, 0)
        txtView.textAlignment = View.TEXT_ALIGNMENT_CENTER
        return snack.show()
    }

}