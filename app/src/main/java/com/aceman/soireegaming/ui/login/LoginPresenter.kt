package com.aceman.soireegaming.ui.login

import androidx.lifecycle.MutableLiveData
import com.aceman.soireegaming.data.models.User
import com.aceman.soireegaming.data.repositories.FirestoreRepository
import com.aceman.soireegaming.utils.Utils
import com.aceman.soireegaming.utils.base.BasePresenter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import okhttp3.internal.Util
import timber.log.Timber

/**
 * Created by Lionel JOFFRAY - on 19/11/2019.
 */
class LoginPresenter : BasePresenter(), LoginContract.LoginPresenterInterface {
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

    override fun saveDate(){
       val mUser = getCurrentUser()!!
        val date = Utils.todayDate
        firebaseRepository.getUser(mUser.uid).addOnSuccessListener {
            firebaseRepository.saveDate(date).addOnSuccessListener {
                }.addOnFailureListener {
                }
        }
    }

    override fun saveUserToFirebase(userItem: User) {

        firebaseRepository.getUser(userItem.uId).addOnSuccessListener { documentSnapshot ->
            if (!documentSnapshot.exists()) {
                firebaseRepository.saveUser(userItem).addOnSuccessListener {
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