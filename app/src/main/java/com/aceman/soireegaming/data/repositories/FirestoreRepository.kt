package com.aceman.soireegaming.data.repositories

import com.aceman.soireegaming.data.models.*
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot


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

    fun saveUser(userItem: User, uid: String): Task<Void> {
        val documentReference = firestoreDB.collection("user").document(uid)
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

    fun getUserList(): Task<QuerySnapshot> {
        return firestoreDB.collection("user").get()
    }

    fun deleteUser(userItem: User): Task<Void> {
        val documentReference =  firestoreDB.collection("users/${user!!.email.toString()}/saved_addresses")
            .document(userItem.name)

        return documentReference.delete()
    }

    fun setEventParticipation(eventList: MutableList<String>): Task<Void>{
        return firestoreDB.collection("user").document(user!!.uid).update("eventList", eventList)
    }

    fun saveEvent(eventInfos: EventInfos, eventId: String): Task<Void> {
        return firestoreDB.collection("event").document(eventId).set(eventInfos)
    }

    fun getEvents(eventId: String): Task<DocumentSnapshot> {
        return firestoreDB.collection("event").document(eventId).get()
    }
    fun getAllEvents(): Task<QuerySnapshot> {
        return firestoreDB.collection("event").get()
    }
}


