package com.aceman.soireegaming.ui.bottomnavigation.notifications

import com.aceman.soireegaming.utils.base.BaseView
import com.google.firebase.firestore.QuerySnapshot

/**
 * Created by Lionel JOFFRAY - on 19/11/2019.
 *
 * A classic contract for class/fragment with all functions.
 */

interface NotificationsContract {

    interface NotificationsPresenterInterface {

        fun getNotifications()
        fun deleteNotification(notifId: String)
    }

    interface NotificationsViewInterface : BaseView {
        fun updateUI(mutableList: QuerySnapshot?)
        fun refreshView()
        fun launchChatIntent(userId: String)
        fun launchEventDetail(eventId: String)
        fun launchProfile()
        fun configureRecyclerView()
    }
}