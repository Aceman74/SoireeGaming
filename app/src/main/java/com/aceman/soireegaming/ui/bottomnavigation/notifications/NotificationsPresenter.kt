package com.aceman.soireegaming.ui.bottomnavigation.notifications

import com.aceman.soireegaming.data.firebase.FirestoreOperations
import com.aceman.soireegaming.utils.base.BasePresenter
import com.google.firebase.auth.FirebaseAuth

/**
 * Created by Lionel JOFFRAY - on 19/11/2019.
 *
 * A classic presenter class for activity/fragment with functions.
 */
class NotificationsPresenter : BasePresenter(),
    NotificationsContract.NotificationsPresenterInterface {
    var firebaseRepository = FirestoreOperations
    val mUser = FirebaseAuth.getInstance().currentUser!!
    /**
     * Get notification list from Firebase.
     */
    override fun getNotifications() {
        firebaseRepository.userCollection.document(mUser.uid).collection("Notifications").get()
            .addOnSuccessListener {

                (getView() as NotificationsContract.NotificationsViewInterface).updateUI(it)
            }
    }

    /**
     * Delete a notification.
     */
    override fun deleteNotification(notifId: String) {
        firebaseRepository.userCollection.document(mUser.uid).collection("Notifications").get()
            .addOnSuccessListener {
                for (item in it)
                    if (item["id"] == notifId)
                        firebaseRepository.userCollection.document(mUser.uid).collection("Notifications").document(
                            item.id
                        ).delete()
                (getView() as NotificationsContract.NotificationsViewInterface).refreshView()

            }
    }
}