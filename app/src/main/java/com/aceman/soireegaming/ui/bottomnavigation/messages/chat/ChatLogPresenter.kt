package com.aceman.soireegaming.ui.bottomnavigation.messages.chat

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
 *
 * A classic presenter class for activity/fragment with functions.
 */
class ChatLogPresenter : BasePresenter(), ChatLogContract.ChatLogPresenterInterface {
    var firebaseRepository = FirestoreOperations
    var user: MutableLiveData<List<User>> = MutableLiveData()

    /**
     * Getting current user check.
     */
    override fun getCurrentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    /**
     * Get user data from Firestore.
     */
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

    /**
     * Get chat channel.
     */
    override fun getChannel(mOtherUser: String, onComplete: (channelId: String) -> Unit) {
        firebaseRepository.getOrCreateChatChannel(mOtherUser) {
            onComplete(it)
        }
    }

    /**
     * Get message live.
     */
    override fun addChatMessagesListener(
        channelId: String, context: Context,
        onListen: (List<Item>) -> Unit
    ): ListenerRegistration {
        return firebaseRepository.chatListener(channelId, context, onListen)
    }

    /**
     * Send a message, save it to Firebase.
     */
    override fun sendMessage(message: Message, channelId: String) {
        return firebaseRepository.sendMessage(message, channelId)
    }
}