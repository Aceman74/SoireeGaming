package com.aceman.soireegaming.ui.event.create

import com.aceman.soireegaming.data.firebase.FirestoreOperations
import com.aceman.soireegaming.data.models.EventInfos
import com.aceman.soireegaming.data.models.ObjectId
import com.aceman.soireegaming.data.models.User
import com.aceman.soireegaming.utils.Utils
import com.aceman.soireegaming.utils.base.BasePresenter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

/**
 * Created by Lionel JOFFRAY - on 19/11/2019.
 *
 * A classic presenter class for activity/fragment with functions.
 */
class CreateEventActivityPresenter : BasePresenter(),
    CreateEventActivityContract.CreateEventActivityPresenterInterface {
    var firebaseRepository = FirestoreOperations
    val mUser = FirebaseAuth.getInstance().currentUser!!

    /**
     * Getting current user check.
     */
    override fun getCurrentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    /**
     * Get user data.
     */
    override fun getUserDataFromFirestore() {
        if (getCurrentUser() != null) {
            firebaseRepository.getUser(getCurrentUser()!!.uid)
                .addOnSuccessListener { documentSnapshot ->
                    val currentUser = documentSnapshot.toObject(User::class.java)
                    if (currentUser != null) {    //  logout if no username set (account delete by admin )
                        (getView() as CreateEventActivityContract.CreateEventActivityViewInterface).updateUI(
                            currentUser
                        )
                    }
                }
        }
    }

    /**
     * Save newly created event to Firebase.
     */
    override fun saveEventToFirebase(eventInfos: EventInfos, eventId: String) {
        firebaseRepository.saveEvent(eventInfos, eventId).addOnSuccessListener {
            createEventPresence(eventId, mUser.uid)
        }
    }

    /**
     * Set the presence in the event for the creator.
     */
    override fun createEventPresence(eventId: String, uid: String) {
        firebaseRepository.userCollection.document(mUser.uid).collection("Events")
            .add(ObjectId("event", eventId, mUser.uid))
        firebaseRepository.eventCollection.document(eventId).collection("Users")
            .add(ObjectId("user", mUser.uid, mUser.uid))
    }

    /**
     * Save the date of creation.
     */
    override fun saveDate(user: FirebaseUser) {
        val date = Utils.todayDate
        firebaseRepository.getUser(user.uid).addOnSuccessListener {
            firebaseRepository.saveDate(date).addOnSuccessListener {
            }.addOnFailureListener {
            }
        }
    }
}