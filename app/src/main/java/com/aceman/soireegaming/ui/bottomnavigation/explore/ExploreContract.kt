package com.aceman.soireegaming.ui.bottomnavigation.explore

import com.aceman.soireegaming.data.models.EventInfos
import com.aceman.soireegaming.data.models.User
import com.aceman.soireegaming.utils.base.BaseView

/**
 * Created by Lionel JOFFRAY - on 19/11/2019.
 *
 * A classic contract for class/fragment with all functions.
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
        fun configureRecyclerView()
        fun configureMaps()
        fun filterSearch()
        fun onSearchClick()
        fun sortByDate()
        fun launchEventDetailActivity(eid: String)
        fun launchProfileDetailActivity(uid: String)
        fun configureMarkers(mFilteredList: MutableList<EventInfos>)
    }
}