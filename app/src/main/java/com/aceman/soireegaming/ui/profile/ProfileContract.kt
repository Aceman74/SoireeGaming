package com.aceman.soireegaming.ui.profile

import com.aceman.soireegaming.data.models.OpinionAndRating
import com.aceman.soireegaming.data.models.User
import com.aceman.soireegaming.data.models.UserChip
import com.aceman.soireegaming.utils.base.BaseView
import com.google.android.material.chip.Chip
import com.google.firebase.firestore.QuerySnapshot

/**
 * Created by Lionel JOFFRAY - on 19/11/2019.
 *
 * A classic contract for class/fragment with all functions.
 */

interface ProfileContract {

    interface ProfilePresenterInterface {

        fun openAppNotifications(packageManager: ProfileActivity, packageName: String)
        fun getUserDataFromFirestore()
        fun updateChip(chipList: MutableList<UserChip>)
        fun getIntentUserDataFromFirestore(uid: String)
        fun getChipList(uid: String)
        fun rateUser(rating: OpinionAndRating)
    }

    interface ProfileViewInterface : BaseView {
        fun updateUI(currentUser: User)
        fun signOutUserFromFirebase()
        fun updateAndSaveChips(chip: Chip)
        fun updateList(currentUser: User)
        fun setRating(mutableList: QuerySnapshot?)
        fun configureRecyclerView()
        fun userOrIntent()
        fun chipSetting()
        fun clearAutocomplete()
        fun addChip(chipName: String, group: String)
    }
}