package com.aceman.soireegaming.ui.bottomnavigation.notifications

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.core.content.ContextCompat
import com.aceman.soireegaming.data.firebase.FirestoreOperations
import com.aceman.soireegaming.utils.base.BasePresenter
import com.google.firebase.auth.FirebaseAuth

/**
 * Created by Lionel JOFFRAY - on 19/11/2019.
 */
class NotificationsPresenter : BasePresenter(),
    NotificationsContract.NotificationsPresenterInterface{
    var firebaseRepository = FirestoreOperations
    val mUser = FirebaseAuth.getInstance().currentUser!!


    @TargetApi(Build.VERSION_CODES.O)
    override fun openAppNotifications(context: Context, packageName: String) {
        val settingsIntent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
            .putExtra(Settings.EXTRA_CHANNEL_ID, 44)
        ContextCompat.startActivity(context, settingsIntent, null)
    }

    override fun getNotifications() {
        firebaseRepository.userCollection.document(mUser.uid).collection("Notifications").get().addOnSuccessListener {

            (getView() as NotificationsContract.NotificationsViewInterface).updateUI(it)
        }
    }

    override fun deleteNotification(notifId: String) {
        firebaseRepository.userCollection.document(mUser.uid).collection("Notifications").get().addOnSuccessListener {
            for (item in it)
                if(item["id"] == notifId)
                    firebaseRepository.userCollection.document(mUser.uid).collection("Notifications").document(item.id).delete()
            (getView() as NotificationsContract.NotificationsViewInterface).refreshView()

        }
    }
}