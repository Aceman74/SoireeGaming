package com.aceman.soireegaming.ui.bottomnavigation.explore

import com.aceman.soireegaming.data.firebase.FirestoreOperations
import com.aceman.soireegaming.data.models.EventInfos
import com.aceman.soireegaming.data.models.User
import com.aceman.soireegaming.utils.base.BasePresenter
import com.google.firebase.auth.FirebaseAuth
import timber.log.Timber

/**
 * Created by Lionel JOFFRAY - on 19/11/2019.
 */
class ExplorePresenter : BasePresenter(), ExploreContract.ExplorePresenterInterface{
    var firebaseRepository = FirestoreOperations
    val mUser = FirebaseAuth.getInstance().currentUser!!


    override fun getUserDataFromFirestore() {
        firebaseRepository.getUser(mUser.uid)
            .addOnSuccessListener { documentSnapshot ->
                val currentUser = documentSnapshot.toObject<User>(User::class.java)
                if (currentUser != null) {    //  logout if no username set (account delete by admin )
                    (getView() as ExploreContract.ExploreViewInterface).updateUI(currentUser)
                }
            }
    }
    override fun getAllEvents() {
            val list = mutableListOf<String>()
            firebaseRepository.getAllEvents().addOnSuccessListener { result ->
                for (document in result) {
                    list.add(document.id)
                }

                (getView() as ExploreContract.ExploreViewInterface).updateEventList(list)
            }
                .addOnFailureListener { exception ->
                    Timber.tag("AllEvents").d(exception, "Error getting documents: ")
                }
        }

    override fun addEventInfos(eventId: String){
        firebaseRepository.getEventDetail(eventId).addOnSuccessListener {
            val event = it.toObject(EventInfos::class.java)
            (getView() as ExploreContract.ExploreViewInterface).updateEvents(event!!)
        }
    }
}