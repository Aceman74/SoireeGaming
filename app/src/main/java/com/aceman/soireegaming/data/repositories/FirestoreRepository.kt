package com.aceman.soireegaming.data.repositories

import com.aceman.soireegaming.data.models.User
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

    fun saveUser(userItem: User): Task<Void> {
        val documentReference = firestoreDB.collection("user").document(user!!.uid)
        return documentReference.set(userItem)
    }

    fun getUserList(): CollectionReference {
        val collectionReference = firestoreDB.collection("users/${user!!.email.toString()}/saved_addresses")
        return collectionReference
    }

    fun deleteUser(userItem: User): Task<Void> {
        val documentReference =  firestoreDB.collection("users/${user!!.email.toString()}/saved_addresses")
            .document(userItem.name)

        return documentReference.delete()
    }

}


