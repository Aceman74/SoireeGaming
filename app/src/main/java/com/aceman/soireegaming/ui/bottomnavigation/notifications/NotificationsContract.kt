package com.aceman.soireegaming.ui.bottomnavigation.notifications

import android.annotation.TargetApi
import android.content.Context
import com.aceman.soireegaming.utils.base.BaseView
import com.google.firebase.firestore.QuerySnapshot

/**
 * Created by Lionel JOFFRAY - on 19/11/2019.
 */

interface NotificationsContract {

interface NotificationsPresenterInterface {
    @TargetApi(value = 26)
    fun openAppNotifications(context: Context, packageName: String)

    fun getNotifications()
    fun deleteNotification(notifId: String)
}

    interface NotificationsViewInterface : BaseView {
        fun updateUI(mutableList: QuerySnapshot?)
        fun refreshView()
        fun launchChatIntent(userId: String)
        fun launchEventDetail(eventId: String)
        fun launchProfile()
    }
}