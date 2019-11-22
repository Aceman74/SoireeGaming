package com.aceman.soireegaming.data.repositories

import com.aceman.soireegaming.data.models.*
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore


/**
 * Created by Lionel JOFFRAY - on 18/11/2019.
 */
class FirestoreRepository {

    val TAG = "FIREBASE_REPOSITORY"
    var firestoreDB = FirebaseFirestore.getInstance()
    var user = FirebaseAuth.getInstance().currentUser


    fun getUser(uid: String): Task<DocumentSnapshot> {
        return firestoreDB.collection("user").document(uid).get()
    }

    fun saveUser(userItem: User, uId: String): Task<Void> {
        val documentReference = firestoreDB.collection("user").document(uId)
        return documentReference.set(userItem)
    }

    fun updateName(name: String): Task<Void> {
        return firestoreDB.collection("user").document(user!!.uid).update("name", name)
    }
    fun updateEmail(email: String): Task<Void> {
        return firestoreDB.collection("user").document(user!!.uid).update("email", email)
    }
    fun saveUserLocation(userLoc: UserLocation): Task<Void> {
        return firestoreDB.collection("user").document(user!!.uid).update("userLocation", userLoc)
    }
    fun saveDate(date: String): Task<Void> {
        return firestoreDB.collection("user").document(user!!.uid).update("Date", date)
    }

    fun updateChip(chipList: MutableList<UserChip>): Task<Void> {
        return firestoreDB.collection("user").document(user!!.uid).update("chipList",chipList)
    }

    fun saveUserInfos(userInfos: UserInfos): Task<Void> {
        return firestoreDB.collection("user").document(user!!.uid).update("userInfos", userInfos)
    }

    fun getUserList(): CollectionReference {
        return firestoreDB.collection("user").document(user!!.uid).parent
    }

    fun deleteUser(userItem: User): Task<Void> {
        val documentReference =  firestoreDB.collection("users/${user!!.email.toString()}/saved_addresses")
            .document(userItem.name)

        return documentReference.delete()
    }

}


