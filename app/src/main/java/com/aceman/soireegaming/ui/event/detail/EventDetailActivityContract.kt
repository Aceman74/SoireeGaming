package com.aceman.soireegaming.ui.event.detail

import com.aceman.soireegaming.data.models.EventInfos
import com.aceman.soireegaming.data.models.User
import com.aceman.soireegaming.utils.base.BaseView

/**
 * Created by Lionel JOFFRAY - on 19/11/2019.
 *
 * A classic contract for class/fragment with all functions.
 */

interface EventDetailActivityContract {

interface EventDetailActivityPresenterInterface {
    fun getUserDataFromFirestore()
    fun getEventInfos(eventId: String)
    fun getUserPresentList(userPresent: MutableList<String>)
    fun createEventDemand(eventId: String, uid: String)
    fun typeOfUser(eventId: String)
    fun getEventDemandInfos(eventId: String)
    fun getUserDemandList(userDemand: MutableList<String>)
    fun removeEventDemand(eventId: String, userId: String, uid: String)
    fun acceptEventDemand(eventId: String, userId: String, uid: String)
    fun removeEventParticipation(eventId: String, userId: String)
}

    interface EventDetailActivityViewInterface : BaseView {
       fun updateUI(currentUser : User)
        fun updateEvents(eventDetails: EventInfos,userPresent: MutableList<String>)
        fun setUserList(userList: MutableList<User>)
        fun setUserType(type: String)
        fun updateEventsDemands(userDemand: MutableList<String>)
        fun setDemandList(userDemandList: MutableList<User>)

        fun getUserType()
        fun isEventOwner()
        fun isParticipant()
        fun onClickCalendar(mEvent: EventInfos)
        fun getIntentId()
        fun onClickParticipate()
        fun configureMaps()
        fun configureRecyclerView()
        fun refreshView()
        fun configureWaitingRecyclerView()
        fun launchProfileDetailActivity(uid: String)
        fun addChip(chipName: String, group: String)
    }
}