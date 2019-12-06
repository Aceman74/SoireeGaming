package com.aceman.soireegaming.ui.profile.edit

import com.aceman.soireegaming.data.firebase.FirestoreOperations
import com.aceman.soireegaming.data.models.User
import com.aceman.soireegaming.data.models.UserInfos
import com.aceman.soireegaming.data.models.UserLocation
import com.aceman.soireegaming.utils.base.BasePresenter
import com.google.firebase.auth.FirebaseAuth
import timber.log.Timber

/**
 * Created by Lionel JOFFRAY - on 19/11/2019.
 *
 * A classic presenter class for activity/fragment with functions.
 */
class EditProfilePresenter : BasePresenter(), EditProfileContract.EditProfilePresenterInterface {
    var firebaseRepository = FirestoreOperations
    val mUser = FirebaseAuth.getInstance().currentUser!!

    /**
     * Get the user data.
     */
    override fun getUserDataFromFirestore() {
        firebaseRepository.getUser(mUser.uid)
            .addOnSuccessListener { documentSnapshot ->
                val currentUser = documentSnapshot.toObject(User::class.java)
                (getView() as EditProfileContract.EditProfileViewInterface).loadUserInfos(
                    currentUser!!
                )
            }
    }

    /**
     * Save the user data.
     */
    override fun saveUserInfosToFirebase(userInfos: UserInfos) {
        firebaseRepository.getUser(mUser.uid).addOnSuccessListener {
            firebaseRepository.saveUserInfos(userInfos).addOnSuccessListener {
                Timber.i("Infos Updated saved!")
            }.addOnFailureListener {
                Timber.e("Failed to update User!")
            }
        }
    }

    /**
     * Update only name.
     */
    override fun updateNameOnFirestore(name: String) {
        firebaseRepository.getUser(mUser.uid).addOnSuccessListener {
            firebaseRepository.updateName(name).addOnSuccessListener {
                Timber.i("Name Updated saved!")
            }.addOnFailureListener {
                Timber.e("Failed to update Name!")
            }
        }
    }

    /**
     * Update Picture URL.
     */
    override fun updatePictureOnFirestore(toUri: String) {
        firebaseRepository.getUser(mUser.uid).addOnSuccessListener {
            firebaseRepository.updatePicture(toUri)
        }
    }

    /**
     * Update Location of user.
     */
    override fun updateLocationOnFirestore(location: UserLocation) {
        firebaseRepository.userCollection.document(mUser.uid).update("userLocation", location)
    }

    /**
     * Update only Email.
     */
    override fun updateEmailOnFirestore(email: String) {
        firebaseRepository.getUser(mUser.uid).addOnSuccessListener {
            firebaseRepository.updateEmail(email).addOnSuccessListener {
                Timber.i("Email Updated saved!")
            }.addOnFailureListener {
                Timber.e("Failed to update Email!")
            }
        }
    }
}