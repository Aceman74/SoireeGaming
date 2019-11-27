package com.aceman.soireegaming.ui.bottomnavigation.messages

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.aceman.soireegaming.data.models.EventInfos
import com.aceman.soireegaming.data.models.User
import com.aceman.soireegaming.data.repositories.FirestoreRepository
import com.aceman.soireegaming.utils.base.BasePresenter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

/**
 * Created by Lionel JOFFRAY - on 19/11/2019.
 */
class PassedEventsPresenter : BasePresenter(), PassedEventsContract.PassedEventsPresenterInterface {
    var firebaseRepository = FirestoreRepository()
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
                    Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
                    eventsList.add(document.id)
                }

                (getView() as PassedEventsContract.PassedEventsViewInterface).updateUI(eventsList)
            }
                .addOnFailureListener { exception ->
                    Log.d(ContentValues.TAG, "Error getting documents: ", exception)
                }
        }
    }

    override fun addEventInfos(eventId: String){
        firebaseRepository.getEvents(eventId).addOnSuccessListener {
            val event = it.toObject<EventInfos>(EventInfos::class.java)
            (getView() as PassedEventsContract.PassedEventsViewInterface).updateEvents(event!!)
        }
    }
}