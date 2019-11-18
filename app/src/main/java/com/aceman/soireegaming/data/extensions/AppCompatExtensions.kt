package com.aceman.soireegaming.data.extensions

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

/**
 * Created by Lionel JOFFRAY - on 14/11/2019.
 */


fun AppCompatActivity.showFragment(id: Int, name: String): Boolean {
    return try {
        val fragment = getInstanceByString<Fragment>(name + "Fragment")
        supportFragmentManager
            .beginTransaction()
            .replace(id, fragment)
            .commit()
        true
    } catch (e: Exception) {
        false
    }
}

fun <T> getInstanceByString(name: String): T {
    val myClass = Class.forName("com.aceman.soireegaming.ui.home.$name")
    val constructor = myClass.getConstructor()
    return constructor.newInstance() as T
}
