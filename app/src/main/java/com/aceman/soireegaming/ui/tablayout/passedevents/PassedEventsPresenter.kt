package com.aceman.soireegaming.ui.tablayout.passedevents

import androidx.lifecycle.MutableLiveData
import com.aceman.soireegaming.data.firebase.FirestoreOperations
import com.aceman.soireegaming.data.models.EventInfos
import com.aceman.soireegaming.data.models.User
import com.aceman.soireegaming.utils.base.BasePresenter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import timber.log.Timber

/**
 * Created by Lionel JOFFRAY - on 19/11/2019.
 *
 * A classic presenter class for activity/fragment with functions.
 */
class PassedEventsPresenter : BasePresenter(), PassedEventsContract.PassedEventsPresenterInterface {
    var firebaseRepository = FirestoreOperations
    var user: MutableLiveData<List<User>> = MutableLiveData()

    /**
     * Getting current user check.
     */
    override fun getCurrentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    /**
     * Get all events from Firebase.
     */
    override fun getAllEvents() {
        if (getCurrentUser() != null) {
            val eventsList = mutableListOf<String>()
            firebaseRepository.getAllEvents().addOnSuccessListener { result ->
                for (document in result) {
                    Timber.tag("PassedEvents").d("${document.id} => ${document.data}")
                    eventsList.add(document.id)
                }

                (getView() as PassedEventsContract.PassedEventsViewInterface).updateUI(eventsList)
            }
                .addOnFailureListener { exception ->
                    Timber.tag("PassedEvents").d("Error getting documents: $exception")
                }
        }
    }

    /**
     * Add event info.
     */
    override fun addEventInfos(eventId: String) {
        firebaseRepository.getEventDetail(eventId).addOnSuccessListener {
            val event = it.toObject(EventInfos::class.java)
            (getView() as PassedEventsContract.PassedEventsViewInterface).updateEvents(event!!)
        }
    }
}