package com.aceman.soireegaming.ui.home

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
class HomePresenter : BasePresenter(), HomeContract.HomePresenterInterface {
    var firebaseRepository = FirestoreOperations
    var user: MutableLiveData<List<User>> = MutableLiveData()
    var mUser = FirebaseAuth.getInstance().currentUser!!

    /**
     * Getting current user check.
     *
     * @return actual user
     */
    override fun getCurrentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    override fun getUserDataFromFirestore() {
        if (getCurrentUser() != null) {
            firebaseRepository.getUser(mUser.uid)
                .addOnSuccessListener { documentSnapshot ->
                    val currentUser = documentSnapshot.toObject(User::class.java)
                    if (currentUser != null) {    //  logout if no username set (account delete by admin )
                        getEventFromUser()
                    }
                }
        }
    }

    override fun getEventFromUser() { // GET DOC LIST
        val list = mutableListOf<String>()
        firebaseRepository.getAllEvents().addOnSuccessListener {
            for (document in it) {
                Timber.tag("EVENTID").d(document.id)
                if(document.data["uid"] == mUser.uid)
                    list.add(document.id)
            }
        }.addOnCompleteListener {
            for(item in list)
            (getView() as HomeContract.HomeViewInterface).updateUI(item)
        }
    }

    override fun addEventInfos(eventId: String){
        firebaseRepository.getEventDetail(eventId).addOnSuccessListener {
            val event = it.toObject(EventInfos::class.java)
            if (event != null) {
                (getView() as HomeContract.HomeViewInterface).updateEvents(event)
            }
        }
    }
}