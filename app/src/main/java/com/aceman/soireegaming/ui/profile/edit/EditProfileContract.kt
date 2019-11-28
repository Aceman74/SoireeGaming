package com.aceman.soireegaming.ui.profile.edit

import com.aceman.soireegaming.data.models.User
import com.aceman.soireegaming.data.models.UserInfos
import com.aceman.soireegaming.utils.base.BaseView
import com.google.firebase.auth.FirebaseUser

/**
 * Created by Lionel JOFFRAY - on 19/11/2019.
 */

interface EditProfileContract {

interface EditProfilePresenterInterface {
    fun getUserDataFromFirestore()
    fun getCurrentUser(): FirebaseUser?
    fun saveUserInfosToFirebase(userInfos: UserInfos)
    fun updateEmailOnFirestore(email: String)
    fun updateNameOnFirestore(name: String)
}

    interface EditProfileViewInterface : BaseView {
        fun deleteCheckBox()
        fun loadUserInfos(currentUser: User)
    }
}