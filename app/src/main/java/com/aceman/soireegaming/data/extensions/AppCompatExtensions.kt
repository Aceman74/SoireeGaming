package com.aceman.soireegaming.data.extensions

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

/**
 * Created by Lionel JOFFRAY - on 14/11/2019.
 */


fun AppCompatActivity.showFragment(id: Int, name: Fragment): Boolean {
    return try {
        supportFragmentManager
            .beginTransaction()
            .replace(id, name)
            .commit()
        true
    } catch (e: Exception) {
        false
    }
}

