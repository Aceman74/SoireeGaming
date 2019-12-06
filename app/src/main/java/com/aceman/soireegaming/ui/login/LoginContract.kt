package com.aceman.soireegaming.ui.login

import com.aceman.soireegaming.data.models.User
import com.aceman.soireegaming.utils.base.BaseView

/**
 * Created by Lionel JOFFRAY - on 19/11/2019.
 *
 * A classic contract for class/fragment with all functions.
 */

interface LoginContract {

interface LoginPresenterInterface {

    fun saveUserToFirebase(userItem: User)
}

    interface LoginViewInterface : BaseView {

        fun isLoggedUser()
        fun checkPermission()
        fun askPermission()
        fun startSignInActivity()
        fun dexterInit()



    }
}