package com.aceman.soireegaming.ui.home

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
class HomePresenter : BasePresenter(), HomeContract.HomePresenterInterface {
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

    override fun getUserDataFromFirestore() {
        if (getCurrentUser() != null) {
            firebaseRepository.getUser(getCurrentUser()!!.uid)
                .addOnSuccessListener { documentSnapshot ->
                    val currentUser = documentSnapshot.toObject<User>(User::class.java)
                    if (currentUser != null) {    //  logout if no username set (account delete by admin )
                        (getView() as HomeContract.HomeViewInterface).updateUI(currentUser)
                    }
                }
        }
    }

    override fun addEventInfos(eventId: String){
        firebaseRepository.getEvents(eventId).addOnSuccessListener {
            val event = it.toObject<EventInfos>(EventInfos::class.java)
            if (event != null) {
                (getView() as HomeContract.HomeViewInterface).updateEvents(event)
            }
        }
    }
}