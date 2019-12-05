package com.aceman.soireegaming.ui.home.main

import android.location.Address
import android.location.Geocoder
import com.aceman.soireegaming.data.firebase.FirestoreOperations
import com.aceman.soireegaming.data.models.User
import com.aceman.soireegaming.data.models.UserLocation
import com.aceman.soireegaming.utils.base.BasePresenter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import timber.log.Timber
import java.io.IOException

/**
 * Created by Lionel JOFFRAY - on 19/11/2019.
 */
class MainPresenter : BasePresenter(), MainContract.MainPresenterInterface {

    var firebaseRepository = FirestoreOperations
    val mUser = FirebaseAuth.getInstance().currentUser!!

    override fun getCity(lat: Double, lon: Double, geocoder: Geocoder): String {
        var cityName = "City"
        val addresses: List<Address>
            try {
            addresses = geocoder.getFromLocation(lat, lon, 10)
            if (addresses.isNotEmpty()) {
                for (adr: Address in addresses) {
                    if (adr.locality != null && adr.locality.isNotEmpty()) {
                        cityName = adr.locality
                        Timber.tag("LOCALITY: ").i(adr.locality)
                        break
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return cityName
    }

    /**
     * Getting current user check.
     *
     * @return actual user
     */
    override fun getCurrentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    override fun getLocation(onComplete: (isNew: Boolean) -> Unit
    ) {
        firebaseRepository.userCollection.document(mUser.uid).get().addOnSuccessListener {
           var user = it.toObject(User::class.java)
            if (user!!.userLocation.city == "SG")
            onComplete(true)
            else
                onComplete(false)
        }
    }

    override fun saveUserLocationToFirebase(userLoc: UserLocation) {
        firebaseRepository.getUser(mUser.uid).addOnSuccessListener {
            firebaseRepository.saveUserLocation(userLoc).addOnSuccessListener {
                    Timber.i("New Location saved!")
                }.addOnFailureListener {
                    Timber.e("Failed to save User Location!")
                }
        }
    }

    override fun updateToken(token: String?) {
        firebaseRepository.userCollection.document(mUser.uid).update("Token",token)
    }

    override fun updateOrCreateTokenList(token: String,tokenMap: MutableMap<String, String>, user: String,
                                         onComplete: (isNew: Boolean) -> Unit) {
        firebaseRepository.getOrCreateTokensList(token,tokenMap,user,onComplete)
    }

}