package com.aceman.soireegaming.ui.tablayout.comingevents

import com.aceman.soireegaming.data.models.EventInfos
import com.aceman.soireegaming.utils.base.BaseView

/**
 * Created by Lionel JOFFRAY - on 19/11/2019.
 */

interface ComingEventsContract {

interface ComingEventsPresenterInterface {

    fun getAllEvents()
    fun getEventInfos(eventId: String)
}

    interface ComingEventsViewInterface : BaseView {
        fun updateUI(eventsList: String)
        fun updateEvents(event: EventInfos)
        fun updateAllEvents(eventsId: MutableList<String>)
    }
}