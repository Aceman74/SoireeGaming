package com.aceman.soireegaming.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.aceman.soireegaming.R
import com.aceman.soireegaming.data.firebase.FirestoreOperations
import com.aceman.soireegaming.data.models.FirestoreNotification
import com.aceman.soireegaming.ui.bottomnavigation.messages.chat.ChatLogActivity
import com.aceman.soireegaming.ui.home.main.MainActivity
import com.aceman.soireegaming.utils.AppConstants.BODY_TYPE
import com.aceman.soireegaming.utils.AppConstants.USER_ID
import com.aceman.soireegaming.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber


/**
 * Created by Lionel JOFFRAY - on 03/12/2019.
 */
class NotificationsService : FirebaseMessagingService() {
    var firebaseRepository = FirestoreOperations
    val mUser = FirebaseAuth.getInstance().currentUser!!
    private val NOTIFICATION_ID = 7
    private val NOTIFICATION_TAG = "FIREBASEOC"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.notification != null) { // 1 - Get message sent by Firebase
            val message = remoteMessage.notification!!.body
            //2 - Show message in console
            Timber.tag("FCM").i(remoteMessage.data.toString())
            sendVisualNotification(remoteMessage)
        }

    }
    override fun onNewToken(token: String) {
        //handle token
    }

    private fun sendVisualNotification(messageBody: RemoteMessage) { // 1 - Create an Intent that will be shown when user will click on the Notification
        var intent: Intent
        when(messageBody.data[BODY_TYPE]) {
            "Chat" -> {
                intent = Intent(this, ChatLogActivity::class.java)
                intent.putExtra("uid", messageBody.data[USER_ID])
            }
            else -> {
                intent = Intent(this, MainActivity::class.java)
            }
        }
        val pendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        // 2 - Create a Style for the Notification
        val inboxStyle: NotificationCompat.InboxStyle = NotificationCompat.InboxStyle()
        inboxStyle.setBigContentTitle("Soirée Gaming")
        inboxStyle.addLine(messageBody.notification!!.title)
        // 3 - Create a Channel (Android 8)
        val channelId = "Soirée Gaming"
        // 4 - Build a Notification object
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_not_logo)
            .setContentTitle(messageBody.notification!!.title)
            .setContentText(messageBody.notification!!.body)
            .setAutoCancel(true)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setContentIntent(pendingIntent)
            .setStyle(inboxStyle)
        // 5 - Add the Notification to the Notification Manager and show it.
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // 6 - Support Version >= Android 8
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName: CharSequence = "Message provenant de Firebase"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel =
                NotificationChannel(channelId, channelName, importance)
            notificationManager.createNotificationChannel(mChannel)
        }
        firebaseRepository.userCollection.document(mUser.uid).collection("Notifications")
            .add(FirestoreNotification(Utils.todayDate,Utils.isTime, messageBody.data[BODY_TYPE]!!,
                messageBody.notification!!.title.toString(),messageBody.notification!!.body.toString(),
                messageBody.data[USER_ID]!!))
        // 7 - Show notification
        notificationManager.notify(NOTIFICATION_TAG, NOTIFICATION_ID, notificationBuilder.build())
    }
}