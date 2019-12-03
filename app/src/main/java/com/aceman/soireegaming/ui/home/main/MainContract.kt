package com.aceman.soireegaming.ui.home.main

import android.location.Geocoder
import com.aceman.soireegaming.data.models.UserLocation
import com.aceman.soireegaming.utils.base.BaseView
import com.google.firebase.auth.FirebaseUser

/**
 * Created by Lionel JOFFRAY - on 19/11/2019.
 */

interface MainContract {

interface MainPresenterInterface {

    fun getCity(lat: Double, lon: Double, geocoder: Geocoder): String
    fun getCurrentUser(): FirebaseUser?
    fun saveUserLocationToFirebase(userLoc: UserLocation)
    fun updateToken(token: String?)


}

    interface MainViewInterface : BaseView
}