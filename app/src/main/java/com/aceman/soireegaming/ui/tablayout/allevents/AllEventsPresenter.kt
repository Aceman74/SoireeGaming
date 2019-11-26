package com.aceman.soireegaming.ui.tablayout.allevents

import androidx.lifecycle.MutableLiveData
import com.aceman.soireegaming.data.models.EventInfos
import com.aceman.soireegaming.data.models.User
import com.aceman.soireegaming.data.repositories.FirestoreRepository
import com.aceman.soireegaming.utils.base.BasePresenter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

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
            val eventList = mutableListOf<QuerySnapshot>()
            var i = 0
            firebaseRepository.getAllEvents()
                .addOnCompleteListener {
                    if(it.isSuccessful){

                        for(doc: QueryDocumentSnapshot in it.result!!){
                            eventList.add(i,it.result!!)
                            i++
                    }
                    }
                        (getView() as AllEventsContract.AllEventsViewInterface).updateUI(eventList)
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