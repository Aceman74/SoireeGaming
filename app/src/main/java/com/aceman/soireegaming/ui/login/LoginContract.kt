package com.aceman.soireegaming.ui.login

import com.aceman.soireegaming.data.models.User
import com.aceman.soireegaming.utils.base.BaseView
import com.google.firebase.auth.FirebaseUser

/**
 * Created by Lionel JOFFRAY - on 19/11/2019.
 */

interface LoginContract {

interface LoginPresenterInterface {

    fun getCurrentUser(): FirebaseUser?
    fun saveUserToFirebase(userItem: User)
    fun saveDate(user: FirebaseUser)
}

    interface LoginViewInterface : BaseView {

        fun isLoggedUser()
        fun checkPermission()
        fun askPermission()
        fun startSignInActivity()
        fun dexterInit()



    }
}