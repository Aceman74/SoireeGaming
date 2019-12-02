package com.aceman.soireegaming.ui.bottomnavigation.messages.chat

import android.content.Context
import com.aceman.soireegaming.data.models.Message
import com.aceman.soireegaming.data.models.User
import com.aceman.soireegaming.utils.base.BaseView
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.kotlinandroidextensions.Item

/**
 * Created by Lionel JOFFRAY - on 19/11/2019.
 */

interface ChatLogContract {

interface ChatLogPresenterInterface {

    fun getCurrentUser(): FirebaseUser?
    fun getUserDataFromFirestore(uid: String)
    fun getChannel(mOtherUser: String, onComplete: (channelId: String) -> Unit)
    fun addChatMessagesListener(
        channelId: String,
        context: Context,
        onListen: (List<Item>) -> Unit
    ): ListenerRegistration

    fun sendMessage(message: Message, channelId: String)
}

    interface ChatLogViewInterface : BaseView {
        fun updateUI(otherUser: User)

    }
}