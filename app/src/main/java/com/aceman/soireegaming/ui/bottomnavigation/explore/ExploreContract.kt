package com.aceman.soireegaming.ui.bottomnavigation.explore

import com.aceman.soireegaming.data.models.EventInfos
import com.aceman.soireegaming.data.models.User
import com.aceman.soireegaming.utils.base.BaseView

/**
 * Created by Lionel JOFFRAY - on 19/11/2019.
 */

interface ExploreContract {

interface ExplorePresenterInterface {
    fun getUserDataFromFirestore()
    fun getAllEvents()
    fun addEventInfos(eventId: String)
}

    interface ExploreViewInterface : BaseView {
        fun updateUI(currentUser: User)
        fun updateEventList(list: MutableList<String>)
        fun updateEvents(event: EventInfos)
    }
}