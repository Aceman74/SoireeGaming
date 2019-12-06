package com.aceman.soireegaming.ui.tablayout.passedevents

import com.aceman.soireegaming.data.models.EventInfos
import com.aceman.soireegaming.utils.base.BaseView
import com.google.firebase.auth.FirebaseUser

/**
 * Created by Lionel JOFFRAY - on 19/11/2019.
 *
 * A classic contract for class/fragment with all functions.
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
        fun sortByDate()
        fun filterList()
        fun launchEventDetailActivity(eid: String)
        fun launchProfileDetailActivity(uid: String)
        fun configureRecyclerView()
    }
}