package com.aceman.soireegaming.ui.bottomnavigation.messages

import com.aceman.soireegaming.data.models.User
import com.aceman.soireegaming.utils.base.BaseView
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot

/**
 * Created by Lionel JOFFRAY - on 19/11/2019.
 */

interface MessagesContract {

interface MessagesPresenterInterface {

    fun getEngagedChat()
    fun getCurrentUser(): FirebaseUser?
    fun addUserInfos(userId: String)
    fun getLastMessageTime(chatID: String): Task<DocumentSnapshot>
}

    interface MessagesViewInterface : BaseView {
        fun updateUI(
            list: MutableList<String>,
            chanList: MutableList<String>
        )
        fun updateUsers(user: User)

    }
}