package com.aceman.soireegaming.ui.tablayout.comingevents

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
 */
class ComingEventsPresenter : BasePresenter(), ComingEventsContract.ComingEventsPresenterInterface {
    var firebaseRepository = FirestoreOperations
    var user: MutableLiveData<List<User>> = MutableLiveData()

    /**
     * Getting current user check.
     *
     * @return actual user
     */
    override fun getCurrentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    override fun getAllEvents() {
        if (getCurrentUser() != null) {
            val eventsList = mutableListOf<String>()
            firebaseRepository.getAllEvents().addOnSuccessListener { result ->
                for (document in result) {
                    Timber.tag("ComingEvents").d("${document.id} => ${document.data}")
                    eventsList.add(document.id)
                }

                (getView() as ComingEventsContract.ComingEventsViewInterface).updateUI(eventsList)
            }
                .addOnFailureListener { exception ->
                    Timber.tag("ComingEvents").d(exception, "Error getting documents: ")
                }
        }
    }

    override fun addEventInfos(eventId: String){
        firebaseRepository.getEvents(eventId).addOnSuccessListener {
            val event = it.toObject(EventInfos::class.java)
            (getView() as ComingEventsContract.ComingEventsViewInterface).updateEvents(event!!)
        }
    }
}