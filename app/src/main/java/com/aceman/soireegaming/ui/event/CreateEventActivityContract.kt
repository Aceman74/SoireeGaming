package com.aceman.soireegaming.ui.event

import com.aceman.soireegaming.data.models.EventInfos
import com.aceman.soireegaming.data.models.User
import com.aceman.soireegaming.utils.base.BaseView
import com.google.firebase.auth.FirebaseUser

/**
 * Created by Lionel JOFFRAY - on 19/11/2019.
 */

interface CreateEventActivityContract {

interface CreateEventActivityPresenterInterface {
    fun getCurrentUser(): FirebaseUser?
    fun saveDate(user: FirebaseUser)
    fun getUserDataFromFirestore()
    fun saveEventToFirebase(eventInfos: EventInfos, eventId: String)

}

    interface CreateEventActivityViewInterface : BaseView {
       fun updateUI(currentUser : User)

    }
}