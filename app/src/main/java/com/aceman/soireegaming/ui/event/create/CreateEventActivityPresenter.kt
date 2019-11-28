package com.aceman.soireegaming.ui.event.create

import com.aceman.soireegaming.data.models.EventInfos
import com.aceman.soireegaming.data.models.User
import com.aceman.soireegaming.data.repositories.FirestoreRepository
import com.aceman.soireegaming.utils.Utils
import com.aceman.soireegaming.utils.base.BasePresenter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

/**
 * Created by Lionel JOFFRAY - on 19/11/2019.
 */
class CreateEventActivityPresenter : BasePresenter(),
    CreateEventActivityContract.CreateEventActivityPresenterInterface {
    var firebaseRepository = FirestoreRepository()
    // var user: MutableLiveData<List<User>> = MutableLiveData()

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
                        (getView() as CreateEventActivityContract.CreateEventActivityViewInterface).updateUI(currentUser)
                    }
                }
        }
    }

    override fun saveEventToFirebase(eventInfos: EventInfos, eventId: String) {
        firebaseRepository.getUser(getCurrentUser()!!.uid).addOnSuccessListener {
            firebaseRepository.saveEvent(eventInfos, eventId).addOnSuccessListener {
            }.addOnFailureListener {
            }
        }

    }

    fun addEventToUserList(eventId: MutableList<String>){
        if (getCurrentUser() != null) {
            firebaseRepository.getUser(getCurrentUser()!!.uid)
                .addOnSuccessListener { documentSnapshot ->
                    val currentUser = documentSnapshot.toObject<User>(User::class.java)
                    if (currentUser != null) {    //  logout if no username set (account delete by admin )
                        firebaseRepository.setEventParticipation(eventId)
                    }
                }
        }
    }

    override fun saveDate(user: FirebaseUser) {
        val date = Utils.todayDate
        firebaseRepository.getUser(user.uid).addOnSuccessListener {
            firebaseRepository.saveDate(date).addOnSuccessListener {
                }.addOnFailureListener {
                }
        }
    }
}