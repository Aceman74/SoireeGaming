package com.aceman.soireegaming.ui.chat

import androidx.lifecycle.MutableLiveData
import com.aceman.soireegaming.data.models.User
import com.aceman.soireegaming.data.repositories.FirestoreRepository
import com.aceman.soireegaming.utils.base.BasePresenter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

/**
 * Created by Lionel JOFFRAY - on 19/11/2019.
 */
class ChatLogPresenter : BasePresenter(), ChatLogContract.ChatLogPresenterInterface {
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

    override fun getUserDataFromFirestore(uid: String) {
        if (getCurrentUser() != null) {
            firebaseRepository.getUser(uid)
                .addOnSuccessListener { documentSnapshot ->
                    val otherUser = documentSnapshot.toObject<User>(User::class.java)
                    if (otherUser != null) {
                        (getView() as ChatLogContract.ChatLogViewInterface).updateUI(otherUser)
                    }
                }
        }
    }
}