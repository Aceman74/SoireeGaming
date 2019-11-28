package com.aceman.soireegaming.ui.profile

import com.aceman.soireegaming.data.models.User
import com.aceman.soireegaming.data.models.UserChip
import com.aceman.soireegaming.utils.base.BaseView
import com.google.android.material.chip.Chip

/**
 * Created by Lionel JOFFRAY - on 19/11/2019.
 */

interface ProfileContract {

    interface ProfilePresenterInterface {

        fun openAppNotifications(packageManager: ProfileActivity, packageName: String)
        fun getUserDataFromFirestore()
        fun updateChip(chipList: MutableList<UserChip>)
        fun getIntentUserDataFromFirestore(uid: String)
        fun getChipList(uid: String)
    }

    interface ProfileViewInterface : BaseView {
        fun updateUI(currentUser: User)
        fun signOutUserFromFirebase()
        fun updateAndSaveChips(chip: Chip)
        fun updateList(currentUser: User)
    }
}