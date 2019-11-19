package com.aceman.soireegaming.ui.profile

import com.aceman.soireegaming.data.models.User
import com.aceman.soireegaming.utils.base.BaseView

/**
 * Created by Lionel JOFFRAY - on 19/11/2019.
 */

interface ProfileContract {

    interface ProfilePresenterInterface {

        fun openAppNotifications(packageManager: ProfileActivity, packageName: String)
        fun getUserDataFromFirestore()

    }

    interface ProfileViewInterface : BaseView {
        fun updateUI(currentUser: User)
        fun signOutUserFromFirebase()

    }
}