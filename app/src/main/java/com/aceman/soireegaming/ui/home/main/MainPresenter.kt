package com.aceman.soireegaming.ui.home.main

import com.aceman.soireegaming.data.firebase.FirestoreOperations
import com.aceman.soireegaming.data.models.User
import com.aceman.soireegaming.data.models.UserLocation
import com.aceman.soireegaming.utils.base.BasePresenter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import timber.log.Timber

/**
 * Created by Lionel JOFFRAY - on 19/11/2019. *
 *
 * A classic presenter class for activity/fragment with functions.
 */
class MainPresenter : BasePresenter(), MainContract.MainPresenterInterface {

    var firebaseRepository = FirestoreOperations
    val mUser = FirebaseAuth.getInstance().currentUser

    /**
     * Getting current user check.
     */
    override fun getCurrentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    /**
     * Check if user location is default "SG" and ask him to change it to city.
     */
    override fun getLocation(
        onComplete: (isNew: Boolean) -> Unit
    ) {
        firebaseRepository.userCollection.document(mUser!!.uid).get().addOnSuccessListener {
            val user = it.toObject(User::class.java)
            if (user!!.userLocation.city == "SG")
                onComplete(true)
            else
                onComplete(false)
        }
    }

    /**
     * Save user location.
     */
    override fun saveUserLocationToFirebase(userLoc: UserLocation) {
        firebaseRepository.getUser(mUser!!.uid).addOnSuccessListener {
            firebaseRepository.saveUserLocation(userLoc).addOnSuccessListener {
                Timber.i("New Location saved!")
            }.addOnFailureListener {
                Timber.e("Failed to save User Location!")
            }
        }
    }

    /**
     * Update user token for FCM.
     */
    override fun updateToken(token: String?) {
        firebaseRepository.userCollection.document(mUser!!.uid).update("Token", token)
    }

    /**
     * Update or create token list.
     */
    override fun updateOrCreateTokenList(
        token: String, tokenMap: MutableMap<String, String>, user: String,
        onComplete: (isNew: Boolean) -> Unit
    ) {
        firebaseRepository.getOrCreateTokensList(token, tokenMap, user, onComplete)
    }

}