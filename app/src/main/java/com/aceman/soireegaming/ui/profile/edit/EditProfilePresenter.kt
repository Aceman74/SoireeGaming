package com.aceman.soireegaming.ui.profile.edit

import com.aceman.soireegaming.data.firebase.FirestoreOperations
import com.aceman.soireegaming.data.models.User
import com.aceman.soireegaming.data.models.UserInfos
import com.aceman.soireegaming.utils.base.BasePresenter
import com.google.firebase.auth.FirebaseAuth
import timber.log.Timber

/**
 * Created by Lionel JOFFRAY - on 19/11/2019.
 */
class EditProfilePresenter : BasePresenter(), EditProfileContract.EditProfilePresenterInterface {
    var firebaseRepository = FirestoreOperations
    val mUser = FirebaseAuth.getInstance().currentUser!!


    override fun getUserDataFromFirestore() {
            firebaseRepository.getUser(mUser.uid)
                .addOnSuccessListener { documentSnapshot ->
                    val currentUser = documentSnapshot.toObject(User::class.java)
                    (getView() as EditProfileContract.EditProfileViewInterface).loadUserInfos(currentUser!!)
        }
    }

    override fun saveUserInfosToFirebase(userInfos: UserInfos) {
        firebaseRepository.getUser(mUser.uid).addOnSuccessListener {
            firebaseRepository.saveUserInfos(userInfos).addOnSuccessListener {
                    Timber.i("Infos Updated saved!")
                }.addOnFailureListener {
                    Timber.e("Failed to update User!")
                }
        }
    }

    override fun updateNameOnFirestore(name: String){
        firebaseRepository.getUser(mUser.uid).addOnSuccessListener {
            firebaseRepository.updateName(name).addOnSuccessListener {
                Timber.i("Name Updated saved!")
            }.addOnFailureListener {
                Timber.e("Failed to update Name!")
            }
        }
    }

    override fun updatePictureOnFirestore(toUri: String){
        firebaseRepository.getUser(mUser.uid).addOnSuccessListener {
            firebaseRepository.updatePicture(toUri)
        }
    }
    override fun updateEmailOnFirestore(email: String){
        firebaseRepository.getUser(mUser.uid).addOnSuccessListener {
            firebaseRepository.updateEmail(email).addOnSuccessListener {
                Timber.i("Email Updated saved!")
            }.addOnFailureListener {
                Timber.e("Failed to update Email!")
            }
        }
    }
}