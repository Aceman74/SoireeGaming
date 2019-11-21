package com.aceman.soireegaming.ui.profile

import com.aceman.soireegaming.data.models.User
import com.aceman.soireegaming.utils.base.BaseView
import com.google.android.gms.tasks.Task
import com.google.android.material.chip.Chip
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

/**
 * Created by Lionel JOFFRAY - on 19/11/2019.
 */

interface ProfileContract {

    interface ProfilePresenterInterface {

        fun openAppNotifications(packageManager: ProfileActivity, packageName: String)
        fun getUserDataFromFirestore()
        fun updateChip(name: String, group:String, check: Boolean)
    }

    interface ProfileViewInterface : BaseView {
        fun updateUI(currentUser: User)
        fun signOutUserFromFirebase()
        fun saveToFirestore(chip: Chip)
    }
}