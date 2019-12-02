package com.aceman.soireegaming.ui.home

import com.aceman.soireegaming.data.models.EventInfos
import com.aceman.soireegaming.utils.base.BaseView
import com.google.firebase.auth.FirebaseUser

/**
 * Created by Lionel JOFFRAY - on 19/11/2019.
 */

interface HomeContract {

interface HomePresenterInterface {

    fun getCurrentUser(): FirebaseUser?
    fun getUserDataFromFirestore()
    fun addEventInfos(eventId: String)
    fun getEventFromUser()
}

    interface HomeViewInterface : BaseView {

        fun updateUI(eventId: String)
        fun updateEvents(event: EventInfos)


    }
}