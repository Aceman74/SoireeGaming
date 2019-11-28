package com.aceman.soireegaming.ui.bottomnavigation.messages

import com.aceman.soireegaming.data.models.User
import com.aceman.soireegaming.utils.base.BaseView
import com.google.firebase.auth.FirebaseUser

/**
 * Created by Lionel JOFFRAY - on 19/11/2019.
 */

interface MessagesContract {

interface MessagesPresenterInterface {

    fun getUserList()
    fun getCurrentUser(): FirebaseUser?
    fun addUserInfos(userId: String)
}

    interface MessagesViewInterface : BaseView {
        fun updateUI(list: MutableList<String>)
        fun updateUsers(user: User)

    }
}