package com.aceman.soireegaming.utils

import androidx.multidex.MultiDexApplication
import com.aceman.soireegaming.BuildConfig
import timber.log.Timber

/**
 * Created by Lionel JOFFRAY - on 13/11/2019.
 *
 * Used for Timber.
 */

class SoireeGamingApp : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
    }
}