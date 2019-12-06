package com.aceman.soireegaming.ui.tablayout.allevents

import com.aceman.soireegaming.data.firebase.FirestoreOperations
import com.aceman.soireegaming.data.models.EventInfos
import com.aceman.soireegaming.utils.base.BasePresenter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import timber.log.Timber

/**
 * Created by Lionel JOFFRAY - on 19/11/2019.
 *
 * A classic presenter class for activity/fragment with functions.
 */
class AllEventsPresenter : BasePresenter(), AllEventsContract.AllEventsPresenterInterface {
    var firebaseRepository = FirestoreOperations

    /**
     * Getting current user check.
     */
    override fun getCurrentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    /**
     * Get all events from Firestore.
     */
    override fun getAllEvents() {
        if (getCurrentUser() != null) {
            val list = mutableListOf<String>()
            firebaseRepository.getAllEvents().addOnSuccessListener { result ->
                for (document in result) {
                    list.add(document.id)
                }

                (getView() as AllEventsContract.AllEventsViewInterface).updateUI(list)
            }
                .addOnFailureListener { exception ->
                    Timber.tag("AllEvents").d(exception, "Error getting documents: ")
                }
        }
    }

    /**
     * Add event info from ID.
     */
    override fun addEventInfos(eventId: String) {
        firebaseRepository.getEventDetail(eventId).addOnSuccessListener {
            val event = it.toObject(EventInfos::class.java)
            (getView() as AllEventsContract.AllEventsViewInterface).updateEvents(event!!)
        }
    }
}