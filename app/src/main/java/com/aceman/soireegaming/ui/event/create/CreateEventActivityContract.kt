package com.aceman.soireegaming.ui.event.create

import com.aceman.soireegaming.data.models.EventInfos
import com.aceman.soireegaming.data.models.User
import com.aceman.soireegaming.utils.base.BaseView
import com.google.android.gms.maps.model.Marker
import com.google.firebase.auth.FirebaseUser

/**
 * Created by Lionel JOFFRAY - on 19/11/2019.
 *
 * A classic contract for class/fragment with all functions.
 */

interface CreateEventActivityContract {

interface CreateEventActivityPresenterInterface {
    fun getCurrentUser(): FirebaseUser?
    fun saveDate(user: FirebaseUser)
    fun getUserDataFromFirestore()
    fun saveEventToFirebase(eventInfos: EventInfos, eventId: String)
    fun createEventPresence(eventId: String, uid: String)

}

    interface CreateEventActivityViewInterface : BaseView {
       fun updateUI(currentUser : User)

        fun moveMarker(mMarker: Marker)
        fun chipSetter()
        fun clearAutocomplete()
        fun addChip(chipName: String, group: String)
    }
}