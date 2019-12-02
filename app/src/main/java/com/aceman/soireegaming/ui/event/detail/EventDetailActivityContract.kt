package com.aceman.soireegaming.ui.event.detail

import com.aceman.soireegaming.data.models.EventInfos
import com.aceman.soireegaming.data.models.User
import com.aceman.soireegaming.utils.base.BaseView

/**
 * Created by Lionel JOFFRAY - on 19/11/2019.
 */

interface EventDetailActivityContract {

interface EventDetailActivityPresenterInterface {
    fun getUserDataFromFirestore()
    fun saveEventToFirebase(eventInfos: EventInfos, eventId: String)

    fun getEventInfos(eventId: String)
    fun getUserPresentList(userPresent: MutableList<String>)
    fun createEventDemand(eventId: String)
    fun typeOfUser(eventId: String)
    fun getEventDemandInfos(eventId: String)
    fun getUserDemandList(userDemand: MutableList<String>)
    fun removeEventDemand(eventId: String)
}

    interface EventDetailActivityViewInterface : BaseView {
       fun updateUI(currentUser : User)
        fun updateEvents(eventDetails: EventInfos,userPresent: MutableList<String>)
        fun setUserList(userList: MutableList<User>)
        fun setUserType(type: String)
        fun updateEventsDemands(userDemand: MutableList<String>)
        fun setDemandList(userDemandList: MutableList<User>)

    }
}