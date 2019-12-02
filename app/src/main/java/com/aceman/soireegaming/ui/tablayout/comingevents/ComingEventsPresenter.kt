package com.aceman.soireegaming.ui.tablayout.comingevents

import com.aceman.soireegaming.data.firebase.FirestoreOperations
import com.aceman.soireegaming.data.models.EventInfos
import com.aceman.soireegaming.utils.base.BasePresenter
import com.google.firebase.auth.FirebaseAuth

/**
 * Created by Lionel JOFFRAY - on 19/11/2019.
 */
class ComingEventsPresenter : BasePresenter(), ComingEventsContract.ComingEventsPresenterInterface {
    var firebaseRepository = FirestoreOperations
    val mUser = FirebaseAuth.getInstance().currentUser!!


    override fun getAllEvents() {
        var eventsId = mutableListOf<String>()
        firebaseRepository.getAllEvents().addOnSuccessListener { result ->
            for (document in result) {
                eventsId.add(document.id)
            }
            (getView() as ComingEventsContract.ComingEventsViewInterface).updateAllEvents(eventsId)
        }
    }

    fun getUserEvent(eventId: String){
        firebaseRepository.eventCollection.document(eventId)
            .collection("Users").get().addOnSuccessListener {
                for (document in it) {
                    if(document["id"] == mUser.uid)
                        (getView() as ComingEventsContract.ComingEventsViewInterface).updateUI(eventId)
                }
            }
    }

    override fun getEventInfos(eventId: String){
        var event = EventInfos()
        firebaseRepository.getEventDetail(eventId).addOnSuccessListener {
            event = it.toObject<EventInfos>(EventInfos::class.java)!!
        }.addOnCompleteListener {
                    (getView() as ComingEventsContract.ComingEventsViewInterface).updateEvents(event)
                }
        }
    }