package com.aceman.soireegaming

import androidx.multidex.MultiDexApplication
import timber.log.Timber

/**
 * Created by Lionel JOFFRAY - on 13/11/2019.
 */

class SoireeGamingApp : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
    }
}