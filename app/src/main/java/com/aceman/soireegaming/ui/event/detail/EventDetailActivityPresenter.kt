package com.aceman.soireegaming.ui.event.detail

import com.aceman.soireegaming.data.firebase.FirestoreOperations
import com.aceman.soireegaming.data.models.EventInfos
import com.aceman.soireegaming.data.models.ObjectId
import com.aceman.soireegaming.data.models.User
import com.aceman.soireegaming.utils.base.BasePresenter
import com.google.firebase.auth.FirebaseAuth
import timber.log.Timber

/**
 * Created by Lionel JOFFRAY - on 19/11/2019.
 *
 * A classic presenter class for activity/fragment with functions.
 */
class EventDetailActivityPresenter : BasePresenter(),
    EventDetailActivityContract.EventDetailActivityPresenterInterface {
    var firebaseRepository = FirestoreOperations
    val mUser = FirebaseAuth.getInstance().currentUser!!
    /**
     * Get the user data.
     */
    override fun getUserDataFromFirestore() {
        firebaseRepository.getUser(mUser.uid)
            .addOnSuccessListener { documentSnapshot ->
                val currentUser = documentSnapshot.toObject<User>(User::class.java)
                if (currentUser != null) {    //  logout if no username set (account delete by admin )
                    (getView() as EventDetailActivityContract.EventDetailActivityViewInterface).updateUI(
                        currentUser
                    )
                }
            }
    }

    /**
     * Get the event Details.
     */
    override fun getEventInfos(eventId: String) {
        val userPresent = mutableListOf<String>()
        var event = EventInfos()
        firebaseRepository.getEventDetail(eventId).addOnSuccessListener {
            event = it.toObject<EventInfos>(EventInfos::class.java)!!
        }.addOnCompleteListener {
            firebaseRepository.eventCollection.document(eventId)
                .collection("Users").get().addOnSuccessListener {
                    for (document in it) {
                        Timber.tag("User Presence").d("%s%s", document.id, document.data)
                        userPresent.add(document["senderId"].toString())
                    }
                }.addOnCompleteListener {
                    (getView() as EventDetailActivityContract.EventDetailActivityViewInterface).updateEvents(
                        event,
                        userPresent
                    )
                }
        }
    }

    /**
     * Get the event user list.
     */
    override fun getEventDemandInfos(eventId: String) {
        val userDemand = mutableListOf<String>()
        var event = EventInfos()
        firebaseRepository.getEventDetail(eventId).addOnSuccessListener {
            event = it.toObject<EventInfos>(EventInfos::class.java)!!
        }.addOnCompleteListener {
            firebaseRepository.eventCollection.document(eventId)
                .collection("UsersDemand").get().addOnSuccessListener {
                    for (document in it) {
                        Timber.tag("User Demand").d("%s%s", document.id, document.data)
                        userDemand.add(document["senderId"].toString())
                    }
                    (getView() as EventDetailActivityContract.EventDetailActivityViewInterface)
                        .updateEventsDemands(userDemand)
                }
        }
    }

    /**
     * Check if user is accepted or waiting.
     */
    override fun typeOfUser(eventId: String) {
        var type = ""
        firebaseRepository.userCollection.document(mUser.uid).collection("EventsDemand")
            .get().addOnSuccessListener {
                for (doc in it) {
                    if (doc["senderId"] == eventId) {
                        type = "Waiting"
                        (getView() as EventDetailActivityContract.EventDetailActivityViewInterface).setUserType(
                            type
                        )
                    }
                }
            }
        firebaseRepository.userCollection.document(mUser.uid).collection("Events")
            .get().addOnSuccessListener {
                for (doc in it) {
                    if (doc["senderId"] == eventId) {
                        type = "Present"
                        (getView() as EventDetailActivityContract.EventDetailActivityViewInterface).setUserType(
                            type
                        )
                    }
                }
            }
    }

    /**
     * Get the user waiting.
     */
    override fun getUserDemandList(userDemand: MutableList<String>) {
        val userDemandList = mutableListOf<User>()
        for ((i, item) in userDemand.withIndex()) {
            firebaseRepository.getUser(userDemand[i]).addOnSuccessListener {
                userDemandList.add(userDemandList.size, it.toObject(User::class.java)!!)
                (getView() as EventDetailActivityContract.EventDetailActivityViewInterface).setDemandList(
                    userDemandList
                )
            }
        }
    }

    /**
     * Get the user Present.
     */
    override fun getUserPresentList(userPresent: MutableList<String>) {
        val userList = mutableListOf<User>()
        for ((i, item) in userPresent.withIndex()) {
            firebaseRepository.getUser(userPresent[i]).addOnSuccessListener {
                userList.add(userList.size, it.toObject(User::class.java)!!)
                (getView() as EventDetailActivityContract.EventDetailActivityViewInterface).setUserList(
                    userList)
            }
        }
    }

    /**
     * Ask to join an event function.
     */
    override fun createEventDemand(eventId: String, uid: String) {
        firebaseRepository.userCollection.document(mUser.uid).collection("EventsDemand")
            .add(ObjectId("eventDemand", eventId, uid))
        firebaseRepository.eventCollection.document(eventId).collection("UsersDemand")
            .add(ObjectId("userDemand", mUser.uid, uid))
        (getView() as EventDetailActivityContract.EventDetailActivityViewInterface).refreshView()
    }

    /**
     * Remvoe event participation, from own user.
     */
    override fun removeEventDemand(
        eventId: String,
        userId: String,
        uid: String
    ) {
        firebaseRepository.userCollection.document(userId).collection("EventsDemand")
            .get().addOnSuccessListener {
                for (item in it) {
                    if (item["senderId"] == eventId) {
                        firebaseRepository.userCollection.document(userId)
                            .collection("EventsDemand")
                            .document(item.id).delete()
                    }
                }
                (getView() as EventDetailActivityContract.EventDetailActivityViewInterface).refreshView()
            }
        firebaseRepository.eventCollection.document(eventId).collection("UsersDemand")
            .get().addOnSuccessListener {
                for (item in it) {
                    if (item["senderId"] == userId) {
                        firebaseRepository.eventCollection.document(eventId)
                            .collection("UsersDemand")
                            .document(item.id).delete()
                    }
                }
            }
        (getView() as EventDetailActivityContract.EventDetailActivityViewInterface).refreshView()
    }

    /**
     * Remove a user by creator.
     */
    override fun removeEventParticipation(eventId: String, userId: String) {
        firebaseRepository.userCollection.document(userId).collection("Events")
            .get().addOnSuccessListener {
                for (item in it) {
                    if (item["senderId"] == eventId) {
                        firebaseRepository.userCollection.document(userId)
                            .collection("Events")
                            .document(item.id).delete()
                        (getView() as EventDetailActivityContract.EventDetailActivityViewInterface).refreshView()
                    }
                }
            }
        firebaseRepository.eventCollection.document(eventId).collection("Users")
            .get().addOnSuccessListener {
                for (item in it) {
                    if (item["senderId"] == userId) {
                        firebaseRepository.eventCollection.document(eventId)
                            .collection("Users")
                            .document(item.id).delete()
                        (getView() as EventDetailActivityContract.EventDetailActivityViewInterface).refreshView()
                    }
                }
            }
    }

    /**
     * Accept a user to join.
     */
    override fun acceptEventDemand(
        eventId: String,
        userId: String,
        uid: String
    ) {
        firebaseRepository.userCollection.document(userId).collection("Events")
            .add(ObjectId("event", eventId, uid))
        firebaseRepository.eventCollection.document(eventId).collection("Users")
            .add(ObjectId("user", userId, uid))
    }
}