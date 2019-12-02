package com.aceman.soireegaming.ui.login

import com.aceman.soireegaming.data.firebase.FirestoreOperations
import com.aceman.soireegaming.data.models.User
import com.aceman.soireegaming.utils.base.BasePresenter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import timber.log.Timber

/**
 * Created by Lionel JOFFRAY - on 19/11/2019.
 */
class LoginPresenter : BasePresenter(), LoginContract.LoginPresenterInterface {
    var firebaseRepository = FirestoreOperations
    // var user: MutableLiveData<List<User>> = MutableLiveData()

    /**
     * Getting current user check.
     *
     * @return actual user
     */
    override fun getCurrentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    override fun saveUserToFirebase(userItem: User) {

        firebaseRepository.getUser(userItem.uid).addOnSuccessListener { documentSnapshot ->
            if (!documentSnapshot.exists()) {
                firebaseRepository.saveUser(userItem,userItem.uid).addOnSuccessListener {
                    Timber.i("New User saved!")
                }.addOnFailureListener {
                    Timber.e("Failed to save User!")
                }
            } else {
                Timber.i("Already Registered !")
            }
        }
    }
}