package com.aceman.soireegaming.ui.tablayout.allevents

import android.content.ContentValues.TAG
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
class AllEventsPresenter : BasePresenter(), AllEventsContract.AllEventsPresenterInterface {
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
            val list = mutableListOf<String>()
            firebaseRepository.getAllEvents().addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    list.add(document.id)
                }

                (getView() as AllEventsContract.AllEventsViewInterface).updateUI(list)
            }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "Error getting documents: ", exception)
                }
        }
    }

    override fun addEventInfos(eventId: String){
        firebaseRepository.getEvents(eventId).addOnSuccessListener {
            val event = it.toObject<EventInfos>(EventInfos::class.java)
            (getView() as AllEventsContract.AllEventsViewInterface).updateEvents(event!!)
        }
    }
}