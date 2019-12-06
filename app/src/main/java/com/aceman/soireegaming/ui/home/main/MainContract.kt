package com.aceman.soireegaming.ui.home.main

import com.aceman.soireegaming.data.models.UserLocation
import com.aceman.soireegaming.utils.base.BaseView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseUser

/**
 * Created by Lionel JOFFRAY - on 19/11/2019. *
 *
 * A classic contract class for activity/fragment.
 */

interface MainContract {

interface MainPresenterInterface {

    fun getCurrentUser(): FirebaseUser?
    fun saveUserLocationToFirebase(userLoc: UserLocation)
    fun updateToken(token: String?)
    fun getLocation(onComplete: (isNew: Boolean) -> Unit)
    fun updateOrCreateTokenList(token: String, tokenMap: MutableMap<String, String>, user: String, onComplete: (isNew: Boolean) -> Unit)
}

    interface MainViewInterface : BaseView {
        fun checkLocation()
        fun getTokenFCM()
        val mOnNavigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener
    }
}