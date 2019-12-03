package com.aceman.soireegaming.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.aceman.soireegaming.ui.home.main.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


/**
 * Created by Lionel JOFFRAY - on 03/12/2019.
 */
class NotificationsService : FirebaseMessagingService() {
    private val NOTIFICATION_ID = 7
    private val NOTIFICATION_TAG = "FIREBASEOC"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.notification != null) { // 1 - Get message sent by Firebase
            val message = remoteMessage.notification!!.body
            //2 - Show message in console
            Log.e("TAG", message)
            sendVisualNotification(remoteMessage)
        }

    }
    override fun onNewToken(token: String) {
        //handle token
    }

    private fun sendVisualNotification(messageBody: RemoteMessage) { // 1 - Create an Intent that will be shown when user will click on the Notification
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        // 2 - Create a Style for the Notification
        val inboxStyle: NotificationCompat.InboxStyle = NotificationCompat.InboxStyle()
        inboxStyle.setBigContentTitle("Hello ")
        inboxStyle.addLine(messageBody.notification!!.title)
        // 3 - Create a Channel (Android 8)
        val channelId = "Soirée Gaming"
        // 4 - Build a Notification object
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
          //  .setSmallIcon(R.drawable.ic_image_notification)
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
        // 7 - Show notification
        notificationManager.notify(NOTIFICATION_TAG, NOTIFICATION_ID, notificationBuilder.build())
    }
}