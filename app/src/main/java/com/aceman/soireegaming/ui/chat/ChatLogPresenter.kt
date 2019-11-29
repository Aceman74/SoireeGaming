package com.aceman.soireegaming.ui.chat

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.aceman.soireegaming.data.firebase.FirestoreOperations
import com.aceman.soireegaming.data.models.Message
import com.aceman.soireegaming.data.models.User
import com.aceman.soireegaming.utils.base.BasePresenter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.kotlinandroidextensions.Item

/**
 * Created by Lionel JOFFRAY - on 19/11/2019.
 */
class ChatLogPresenter : BasePresenter(), ChatLogContract.ChatLogPresenterInterface {
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

    override fun getChannel(mOtherUser: String, onComplete: (channelId: String) -> Unit) {
        firebaseRepository.getChatChannel(mOtherUser){
          onComplete(it)
       }
    }

    override  fun addChatMessagesListener(channelId: String, context: Context,
                                onListen: (List<Item>) -> Unit): ListenerRegistration {
        return firebaseRepository.chatListener(channelId,context,onListen)
    }

   override fun sendMessage(message: Message, channelId: String) {
        return firebaseRepository.sendMessage(message,channelId)
    }
}