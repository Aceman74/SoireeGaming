package com.aceman.soireegaming.data.firebase

import android.content.Context
import com.aceman.soireegaming.data.models.*
import com.aceman.soireegaming.ui.adapters.chatlog.item.TextMessageItem
import com.aceman.soireegaming.utils.Utils
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import com.xwray.groupie.kotlinandroidextensions.Item
import timber.log.Timber


/**
 * Created by Lionel JOFFRAY - on 18/11/2019.
 * All the base opreation for Firesbase CRUD Requests.
 * Functions are pretty explicit.
 */
object FirestoreOperations {

    const val TAG = "FIREBASE_REPOSITORY"
    private val firestoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    val userCollection = firestoreInstance.collection("user")
    val eventCollection = firestoreInstance.collection("event")
    val chatCollection = firestoreInstance.collection("chatChannels")
    val notificationCollection = firestoreInstance.collection("notification")


    // User related //

    fun getUser(uid: String): Task<DocumentSnapshot> {
        return userCollection.document(uid).get()
    }

    fun saveUser(userItem: User, uid: String): Task<Void> {
        return userCollection.document(uid).set(userItem)
    }

    fun updateName(name: String): Task<Void> {
        return userCollection.document(FirebaseAuth.getInstance().currentUser!!.uid).update("name", name)
    }

    fun updateEmail(email: String): Task<Void> {
        return userCollection.document(FirebaseAuth.getInstance().currentUser!!.uid).update("email", email)
    }

    fun saveUserLocation(userLoc: UserLocation): Task<Void> {
        return userCollection.document(FirebaseAuth.getInstance().currentUser!!.uid).update("userLocation", userLoc)
    }

    fun saveDate(date: String): Task<Void> {
        return userCollection.document(FirebaseAuth.getInstance().currentUser!!.uid).update("Date", date)
    }

    fun updateChip(chipList: MutableList<UserChip>): Task<Void> {
        return userCollection.document(FirebaseAuth.getInstance().currentUser!!.uid).update("chipList", chipList)
    }

    fun updatePicture(pictureUrl: String): Task<Void> {
        return userCollection.document(FirebaseAuth.getInstance().currentUser!!.uid).update("urlPicture", pictureUrl)
    }

    fun saveUserInfos(userInfos: UserInfos): Task<Void> {
        return userCollection.document(FirebaseAuth.getInstance().currentUser!!.uid).update("userInfos", userInfos)
    }

    fun getUserList(): Task<QuerySnapshot> {
        return userCollection.get()
    }

    fun deleteUser(userItem: User): Task<Void> {
        return userCollection.document(userItem.uid).delete()
    }

    fun setEventParticipation(eventList: MutableList<String>): Task<Void> {
        return userCollection.document(FirebaseAuth.getInstance().currentUser!!.uid).update("eventList", eventList)
    }

    fun saveEvent(eventInfos: EventInfos, eventId: String): Task<Void> {
        return eventCollection.document(eventId).set(eventInfos)
    }

    fun getEventDetail(eventId: String): Task<DocumentSnapshot> {
        return eventCollection.document(eventId).get()
    }

    fun getAllEvents(): Task<QuerySnapshot> {
        return eventCollection.get()
    }

// Chat Related //

    fun getOrCreateChatChannel(
        otherUserId: String,
        onComplete: (channelId: String) -> Unit
    ) {
        userCollection.document(FirebaseAuth.getInstance().currentUser!!.uid)
            .collection("engagedChatChannels") // current user check if other ID is present
            .document(otherUserId).get().addOnSuccessListener {
                if (it.exists()) {
                    onComplete(it["channelId"] as String)
                    return@addOnSuccessListener
                } else {
                    createChatChannel(otherUserId){channelId->
                        onComplete(channelId)
                    }
                    return@addOnSuccessListener
                }
            }
    }

    fun createChatChannel(
        otherId: String,
        onComplete: (channelId: String) -> Unit
    ) {
        userCollection.document(FirebaseAuth.getInstance().currentUser!!.uid)
            .collection("engagedChatChannels") // current user check if other ID is present
            .document(otherId).get().addOnSuccessListener {
                val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid

                val newChannel = chatCollection.document()
                newChannel.set(ChatChannel(mutableListOf(currentUserId, otherId)))

                userCollection.document(FirebaseAuth.getInstance().currentUser!!.uid)
                    .collection("engagedChatChannels")
                    .document(otherId)
                    .set(mapOf("channelId" to newChannel.id))

                userCollection.document(otherId)
                    .collection("engagedChatChannels")
                    .document(currentUserId)
                    .set(mapOf("channelId" to newChannel.id))
                onComplete(newChannel.id)
            }
    }

    fun getEngagedUsers(eventId: String): Task<DocumentSnapshot> {
        return eventCollection.document(eventId)
            .collection("engagedUsers")
            .document().get()
    }

    fun chatListener(
        channelId: String, context: Context,
        onListen: (List<Item>) -> Unit
    ): ListenerRegistration {
        return chatCollection.document(channelId).collection("messages")
            .orderBy("time")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    Timber.tag("FIRESTORE")
                        .e(firebaseFirestoreException, "ChatMessagesListener error.")
                    return@addSnapshotListener
                }

                val items = mutableListOf<Item>()
                querySnapshot?.documents?.forEach {
                    if (it["type"] == MessageType.TEXT)
                        items.add(TextMessageItem(it.toObject(TextMessage::class.java)!!, context))
                    // later add image support
                }
                onListen(items)
            }
    }

    fun sendMessage(message: Message, channelId: String) {
        chatCollection.document(channelId)
            .collection("messages")
            .add(message)
        val date = DateStamp(Utils.todayDate, Utils.isTime)
        chatCollection.document(channelId)
            .collection("last").document("last").set(date)
    }

    // Token related //
    fun getOrCreateTokensList(
        token: String, tokenMap: MutableMap<String, String>, user: String,
        onComplete: (tokenExist: Boolean) -> Unit
    ) {
        userCollection.document("tokens").get().addOnSuccessListener {
            if (it.exists()) {
                getTokenList { mutableMap: MutableMap<String, String>? ->
                    mutableMap!![user] = token
                    addTokenList(mutableMap)
                }
                onComplete(true)
                return@addOnSuccessListener
            } else {
                addTokenList(tokenMap)
                onComplete(true)
                return@addOnSuccessListener
            }
        }
    }

    private fun getTokenList(onComplete: (list: MutableMap<String, String>?) -> Unit) {
        userCollection.document("tokens").get().addOnSuccessListener {
            val list = it.data as MutableMap<String, String>
            onComplete(list)
            return@addOnSuccessListener
        }
    }

    private fun addTokenList(token: MutableMap<String, String>) {
        userCollection.document("tokens")
            .set(token)
    }
}


