package com.aceman.soireegaming.ui.chat

import com.aceman.soireegaming.data.models.User
import com.aceman.soireegaming.utils.base.BaseView
import com.google.firebase.auth.FirebaseUser

/**
 * Created by Lionel JOFFRAY - on 19/11/2019.
 */

interface ChatLogContract {

interface ChatLogPresenterInterface {

    fun getCurrentUser(): FirebaseUser?
    fun getUserDataFromFirestore(uid: String)
}

    interface ChatLogViewInterface : BaseView {
        fun updateUI(otherUser: User)

    }
}