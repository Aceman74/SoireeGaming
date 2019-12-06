package com.aceman.soireegaming.ui.bottomnavigation.notifications


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aceman.soireegaming.R
import com.aceman.soireegaming.data.models.FirestoreNotification
import com.aceman.soireegaming.ui.adapters.notifications.NotificationsAdapter
import com.aceman.soireegaming.ui.bottomnavigation.messages.chat.ChatLogActivity
import com.aceman.soireegaming.ui.event.detail.EventDetailActivity
import com.aceman.soireegaming.ui.profile.ProfileActivity
import com.aceman.soireegaming.utils.base.BaseView
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.empty_list.*
import kotlinx.android.synthetic.main.fragment_notifications.*
import timber.log.Timber

/**
 * Created by Lionel JOFFRAY - on 05/12/2019.
 *
 * Notification Fragment view shows all user notifications received by FCM. *
 */
class NotificationsFragment : Fragment(), BaseView,
    NotificationsContract.NotificationsViewInterface {
    private val mPresenter: NotificationsPresenter = NotificationsPresenter()
    lateinit var mRecyclerView: RecyclerView
    var notificationList = mutableListOf<FirestoreNotification>()

    companion object {
        fun newInstance(): NotificationsFragment {
            return NotificationsFragment()
        }
    }

    /**
     * Attach presenter.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(false)
        (activity as AppCompatActivity).setSupportActionBar(notifications_tb)
        val mView = inflater.inflate(R.layout.fragment_notifications, container, false)
        mPresenter.attachView(this)
        return mView
    }

    /**
     * Get all notification on created.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter.getNotifications()
    }

    /**
     * Update UI, treat notification list.
     */
    override fun updateUI(mutableList: QuerySnapshot?) {
        notificationList.clear()
        for (item in mutableList!!)
            notificationList.add(item.toObject(FirestoreNotification::class.java))
        configureRecyclerView()
        pb_layout_include.visibility = View.GONE
    }

    /**
     * Initialize the recyclerview for picture preview.
     */
    override fun configureRecyclerView() {
        mRecyclerView = notifications_rv
        mRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        mRecyclerView.adapter = NotificationsAdapter(notificationList) { s: String, s1: String ->
            Timber.tag("Event Waiting RV click").i("$s $s1")
            when (s) {
                "Delete" -> {
                    mPresenter.deleteNotification(s1)
                    pb_layout_include.visibility = View.VISIBLE
                }
                "Chat" -> {
                    launchChatIntent(s1)
                }
                "Accept", "Demand" -> {
                    launchEventDetail(s1)
                }
                "Rating" -> {
                    launchProfile()
                }
            }
        }
        if(notificationList.isEmpty()){
            notification_event_empty_list.visibility = View.VISIBLE
            empty_list_tv.text = getString(R.string.not_yet_notifi)
        }
        else
            notification_event_empty_list.visibility = View.GONE
    }

    /**
     * Launch chat Intent on click chat notification.
     */
    override fun launchChatIntent(userId: String) {
        val chatIntent = Intent(requireContext(), ChatLogActivity::class.java)
        chatIntent.putExtra("uid", userId)
        startActivity(chatIntent)
    }

    /**
     * Launch event Intent on click chat event.
     */
    override fun launchEventDetail(eventId: String) {
        val eventIntent = Intent(requireContext(), EventDetailActivity::class.java)
        eventIntent.putExtra("eid", eventId)
        startActivity(eventIntent)
    }

    /**
     * Launch profile Intent on click chat rating.
     */
    override fun launchProfile() {
        val intent = Intent(requireContext(), ProfileActivity::class.java)
        startActivity(intent)
    }

    /**
     * Refresh the view.
     */
    override fun refreshView() {
        mPresenter.getNotifications()
    }

}
