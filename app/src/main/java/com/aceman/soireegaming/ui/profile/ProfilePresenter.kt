package com.aceman.soireegaming.ui.profile

import android.annotation.TargetApi
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.core.content.ContextCompat.startActivity
import com.aceman.soireegaming.data.firebase.FirestoreOperations
import com.aceman.soireegaming.data.models.OpinionAndRating
import com.aceman.soireegaming.data.models.User
import com.aceman.soireegaming.data.models.UserChip
import com.aceman.soireegaming.utils.base.BasePresenter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

/**
 * Created by Lionel JOFFRAY - on 19/11/2019.
 *
 * A classic presenter class for activity/fragment with functions.
 */
class ProfilePresenter : BasePresenter(), ProfileContract.ProfilePresenterInterface {
    var firebaseRepository = FirestoreOperations

    /**
     * Open Settings notification on device
     */
    @TargetApi(Build.VERSION_CODES.O)
    override fun openAppNotifications(context: ProfileActivity, packageName: String) {
        val settingsIntent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
            .putExtra(Settings.EXTRA_CHANNEL_ID, 44)
        startActivity(context, settingsIntent, null)
    }

    /**
     * Getting current user check.
     *
     * @return actual user
     */
    fun getCurrentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    /**
     * Update chip if owner.
     */
    override fun updateChip(chipList: MutableList<UserChip>) {
        firebaseRepository.getUser(getCurrentUser()!!.uid)
            .addOnSuccessListener {
                firebaseRepository.updateChip(chipList)
            }
    }

    /**
     * Get the chipList.
     */
    override fun getChipList(uid: String) {
        firebaseRepository.getUser(uid)
            .addOnSuccessListener {
                val currentUser = it.toObject(User::class.java)!!
                (getView() as ProfileContract.ProfileViewInterface).updateList(currentUser)
            }
    }

    /**
     * Get user data on Firebase.
     */
    override fun getUserDataFromFirestore() {
        if (getCurrentUser() != null) {
            firebaseRepository.getUser(getCurrentUser()!!.uid)
                .addOnSuccessListener { documentSnapshot ->
                    val currentUser = documentSnapshot.toObject(User::class.java)
                    if (currentUser == null) {    //  logout if no username set (account delete by admin )
                        (getView() as ProfileContract.ProfileViewInterface).signOutUserFromFirebase()
                    } else {
                        (getView() as ProfileContract.ProfileViewInterface).updateUI(currentUser)
                    }
                }
        }
    }

    /**
     * Get intent user data when clicked from a recyclerview.
     */
    override fun getIntentUserDataFromFirestore(uid: String) {
        if (getCurrentUser() != null) {
            firebaseRepository.getUser(uid)
                .addOnSuccessListener { documentSnapshot ->
                    val intentUser = documentSnapshot.toObject(User::class.java)
                    if (intentUser != null) {    //  logout if no username set (account delete by admin )
                        (getView() as ProfileContract.ProfileViewInterface).updateUI(intentUser)
                    }
                }
        }
    }

    /**
     * Get the rating of user.
     */
    fun getRating(uid: String) {
        firebaseRepository.userCollection.document(uid).collection("Ratings").get()
            .addOnSuccessListener {
                (getView() as ProfileContract.ProfileViewInterface).setRating(it)
            }
    }

    /**
     * let a rating to a user.
     */
    override fun rateUser(rating: OpinionAndRating) {
        firebaseRepository.userCollection.document(rating.ratedId).collection("Ratings").add(rating)
        (getView() as ProfileContract.ProfileViewInterface).userOrIntent()
    }
}