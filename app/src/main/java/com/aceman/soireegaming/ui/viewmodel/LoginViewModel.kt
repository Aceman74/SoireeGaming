package com.aceman.soireegaming.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aceman.soireegaming.data.models.User
import com.aceman.soireegaming.data.repositories.FirestoreRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import timber.log.Timber


/**
 * Created by Lionel JOFFRAY - on 18/11/2019.
 */
class LoginViewModel : ViewModel() {

    val TAG = "FIRESTORE_VIEW_MODEL"
    var firebaseRepository = FirestoreRepository()
    var user: MutableLiveData<List<User>> = MutableLiveData()


   private fun getUserFromFirestore(uId: String) : Task<DocumentSnapshot> {
       return firebaseRepository.getUser(uId)
    }

    fun isCurrentlyLogged(): Boolean {
        return FirebaseAuth.getInstance().currentUser != null
    }

    // save address to firebase
    fun saveUserToFirebase(userItem: User) {

        getUserFromFirestore(userItem.uId).addOnSuccessListener { documentSnapshot ->
            if (!documentSnapshot.exists()) {
                firebaseRepository.saveUser(userItem).addOnSuccessListener {
                    Timber.i("New User saved!")
                }.addOnFailureListener {
                    Timber.e("Failed to save User!")
                }
            } else {
                Timber.i("Already Registered !")
            }

        }
    }


    // get realtime updates from firebase
    fun getUsersOnFirebase(): LiveData<List<User>> {
        firebaseRepository.getUserList()
            .addSnapshotListener(EventListener<QuerySnapshot> { value, e ->
                if (e != null) {
                    Timber.tag(TAG).w(e, "Listen failed.")
                    user.value = null
                    return@EventListener
                }
                var savedUserList: MutableList<User> = mutableListOf()
                for (doc in value!!) {
                    var userList = doc.toObject(User::class.java)
                    savedUserList.add(userList)
                }
                user.value = savedUserList
            })

        return user
    }

    // delete a user from firebase
    fun deleteUser(userItem: User) {
        firebaseRepository.deleteUser(userItem).addOnSuccessListener {
            Timber.i("User have been deleted.")
        }.addOnFailureListener {
            Timber.e("Failed to delete User.")
        }
    }

}