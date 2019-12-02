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
 */
class EventDetailActivityPresenter : BasePresenter(),
    EventDetailActivityContract.EventDetailActivityPresenterInterface {
    var firebaseRepository = FirestoreOperations
    // var user: MutableLiveData<List<User>> = MutableLiveData()
    val mUser = FirebaseAuth.getInstance().currentUser!!


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

    override fun saveEventToFirebase(eventInfos: EventInfos, eventId: String) {
        firebaseRepository.getUser(mUser.uid).addOnSuccessListener {
            firebaseRepository.saveEvent(eventInfos, eventId).addOnSuccessListener {
            }.addOnFailureListener {
            }
        }

    }

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
                        userPresent.add(document["id"].toString())
                    }
                }.addOnCompleteListener {
                    (getView() as EventDetailActivityContract.EventDetailActivityViewInterface).updateEvents(
                        event,
                        userPresent
                    )
                }
        }
    }

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
                        userDemand.add(document["id"].toString())
                    }
                }.addOnCompleteListener {
                    (getView() as EventDetailActivityContract.EventDetailActivityViewInterface)
                        .updateEventsDemands(userDemand)
                }
        }
    }

    override fun typeOfUser(eventId: String) {
        var type = ""
        firebaseRepository.userCollection.document(mUser.uid).collection("EventsDemand")
            .get().addOnSuccessListener {
                for (doc in it) {
                    if (doc["id"] == eventId) {
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
                    if (doc["id"] == eventId) {
                        type = "Present"
                        (getView() as EventDetailActivityContract.EventDetailActivityViewInterface).setUserType(
                            type
                        )
                    }
                }
            }
    }

    override fun getUserDemandList(userDemand: MutableList<String>) {
        var userDemandList = mutableListOf<User>()
        for ((i, item) in userDemand.withIndex()) {
            firebaseRepository.getUser(userDemand[i]).addOnSuccessListener {
                userDemandList.add(userDemandList.size, it.toObject(User::class.java)!!)
                (getView() as EventDetailActivityContract.EventDetailActivityViewInterface).setDemandList(
                    userDemandList
                )
            }
        }
    }

    override fun getUserPresentList(userPresent: MutableList<String>) {
        var userList = mutableListOf<User>()
        for ((i, item) in userPresent.withIndex()) {
            firebaseRepository.getUser(userPresent[i]).addOnSuccessListener {
                userList.add(userList.size, it.toObject(User::class.java)!!)
                (getView() as EventDetailActivityContract.EventDetailActivityViewInterface).setUserList(
                    userList
                )
            }
        }
    }

    override fun createEventDemand(eventId: String) {
        firebaseRepository.userCollection.document(mUser.uid).collection("EventsDemand")
            .add(ObjectId("eventDemand", eventId))
        firebaseRepository.eventCollection.document(eventId).collection("UsersDemand")
            .add(ObjectId("userDemand", mUser.uid))
    }

    override fun removeEventDemand(eventId: String) {
        firebaseRepository.userCollection.document(mUser.uid).collection("EventsDemand")
            .get().addOnSuccessListener {
                for (item in it) {
                    if (item["id"] == eventId) {
                        firebaseRepository.userCollection.document(mUser.uid)
                            .collection("EventsDemand")
                            .document(item.id).delete()
                    }
                }
            }
        firebaseRepository.eventCollection.document(eventId).collection("UsersDemand")
            .get().addOnSuccessListener {
                for (item in it) {
                    if (item["id"] == mUser.uid) {
                        firebaseRepository.eventCollection.document(eventId)
                            .collection("UsersDemand")
                            .document(item.id).delete()
                    }
                }
            }
    }

        fun addEventToUserList(eventId: MutableList<String>) {
            firebaseRepository.getUser(mUser.uid)
                .addOnSuccessListener { documentSnapshot ->
                    val currentUser = documentSnapshot.toObject<User>(User::class.java)
                    if (currentUser != null) {    //  logout if no username set (account delete by admin )
                        firebaseRepository.setEventParticipation(eventId)
                    }
                }
        }
    }