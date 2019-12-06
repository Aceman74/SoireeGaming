package com.aceman.soireegaming.ui.tablayout.allevents

import com.aceman.soireegaming.data.models.EventInfos
import com.aceman.soireegaming.utils.base.BaseView
import com.google.firebase.auth.FirebaseUser

/**
 * Created by Lionel JOFFRAY - on 19/11/2019.
 *
 * A classic contract for class/fragment with all functions.
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
        fun sortByDate()
        fun launchEventDetailActivity(eid: String)
        fun launchProfileDetailActivity(uid: String)
        fun configureRecyclerView()
        fun filterList()
    }
}