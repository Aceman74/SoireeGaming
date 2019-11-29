package com.aceman.soireegaming.ui.bottomnavigation.messages

import androidx.lifecycle.MutableLiveData
import com.aceman.soireegaming.data.firebase.FirestoreOperations
import com.aceman.soireegaming.data.models.User
import com.aceman.soireegaming.utils.base.BasePresenter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import timber.log.Timber

/**
 * Created by Lionel JOFFRAY - on 19/11/2019.
 */
class MessagesPresenter : BasePresenter(), MessagesContract.MessagesPresenterInterface {
    var firebaseRepository = FirestoreOperations
    var user: MutableLiveData<List<User>> = MutableLiveData()

    /**
     * Getting current user check.
     *
     * @return actual user
     */
    override fun getCurrentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    override fun getEngagedChat() {
        if (getCurrentUser() != null) {
            val list = mutableListOf<String>()
            firebaseRepository.getUserList()
                .addOnSuccessListener {
                    for (document in it) {
                        Timber.tag("Messages").d( "${document.id} => ${document.data}")
                        if(document.id != FirebaseAuth.getInstance().currentUser!!.uid)
                        list.add(document.id)
                    }
                    (getView() as MessagesContract.MessagesViewInterface).updateUI(list)
                }
        }
    }

    override fun addUserInfos(userId: String){
        firebaseRepository.getUser(userId).addOnSuccessListener {
            val user = it.toObject(User::class.java)
            if (user != null) {
                (getView() as MessagesContract.MessagesViewInterface).updateUsers(user)
            }
        }
    }
}