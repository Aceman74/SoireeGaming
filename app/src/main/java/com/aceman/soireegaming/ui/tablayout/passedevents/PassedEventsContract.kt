package com.aceman.soireegaming.ui.tablayout.passedevents

import com.aceman.soireegaming.data.models.EventInfos
import com.aceman.soireegaming.utils.base.BaseView
import com.google.firebase.auth.FirebaseUser

/**
 * Created by Lionel JOFFRAY - on 19/11/2019.
 */

interface PassedEventsContract {

    interface PassedEventsPresenterInterface {

        fun addEventInfos(eventId: String)
        fun getAllEvents()
        fun getCurrentUser(): FirebaseUser?
    }

    interface PassedEventsViewInterface : BaseView {
        fun updateUI(eventsList: MutableList<String>)
        fun updateEvents(event: EventInfos)
    }
}