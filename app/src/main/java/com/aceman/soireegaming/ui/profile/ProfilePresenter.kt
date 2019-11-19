package com.aceman.soireegaming.ui.profile

import android.annotation.TargetApi
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.AsyncTask
import android.os.Build
import android.provider.Settings
import androidx.core.content.ContextCompat.startActivity
import com.aceman.soireegaming.data.models.User
import com.aceman.soireegaming.data.repositories.FirestoreRepository
import com.aceman.soireegaming.utils.base.BasePresenter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.io.IOException
import java.util.*

/**
 * Created by Lionel JOFFRAY - on 19/11/2019.
 */
class ProfilePresenter : BasePresenter(), ProfileContract.ProfilePresenterInterface {
    var firebaseRepository = FirestoreRepository()

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


    fun hereLocation(lat: Double, lon: Double, profileActivity: ProfileActivity): String {
        var cityName: String = "City"
        val geocoder: Geocoder = Geocoder(profileActivity, Locale.getDefault())
        val addresses: List<Address>
        try {
            addresses = geocoder.getFromLocation(lat, lon, 10)
            if (addresses.size > 0) {
                for (adr: Address in addresses) {
                    if (adr.locality != null && adr.locality.length > 0) {
                        cityName = adr.locality
                        break
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return cityName
    }

    override fun getUserDataFromFirestore() {
        if (getCurrentUser() != null) {
            firebaseRepository.getUser(getCurrentUser()!!.uid)
                .addOnSuccessListener { documentSnapshot ->
                    val currentUser = documentSnapshot.toObject<User>(User::class.java)
                    if (currentUser == null) {    //  logout if no username set (account delete by admin )
                        (getView() as ProfileContract.ProfileViewInterface).signOutUserFromFirebase()
                    } else {
                        (getView() as ProfileContract.ProfileViewInterface).updateUI(currentUser)
                    }
                }
        }
    }
}