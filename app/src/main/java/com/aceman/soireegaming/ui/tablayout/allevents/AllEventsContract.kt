package com.aceman.soireegaming.ui.tablayout.allevents

import com.aceman.soireegaming.data.models.EventInfos
import com.aceman.soireegaming.utils.base.BaseView
import com.google.firebase.auth.FirebaseUser

/**
 * Created by Lionel JOFFRAY - on 19/11/2019.
 */

interface AllEventsContract {

interface AllEventsPresenterInterface {

    fun getCurrentUser(): FirebaseUser?
    fun getAllEvents()
    fun addEventInfos(eventId: String)
}

    interface AllEventsViewInterface : BaseView {
        fun updateUI(eventsList: MutableList<String>)
        fun updateEvents(event: EventInfos)
    }
}