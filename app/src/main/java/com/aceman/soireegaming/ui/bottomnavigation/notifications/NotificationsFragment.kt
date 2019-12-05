package com.aceman.soireegaming.ui.bottomnavigation.notifications


import android.content.Intent
import android.os.Bundle
import android.view.*
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
import kotlinx.android.synthetic.main.fragment_notifications.*
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 */
class NotificationsFragment : Fragment(), BaseView, NotificationsContract.NotificationsViewInterface {
    private val mPresenter: NotificationsPresenter = NotificationsPresenter()
    lateinit var mRecyclerView: RecyclerView
    var notificationList = mutableListOf<FirestoreNotification>()
    companion object {
        fun newInstance(): NotificationsFragment {
            return NotificationsFragment()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(notifications_tb)
        val mView = inflater.inflate(R.layout.fragment_notifications, container, false)
        mPresenter.attachView(this)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter.getNotifications()
    }

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
    fun configureRecyclerView() {
        mRecyclerView = notifications_rv
        mRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        mRecyclerView.adapter = NotificationsAdapter(notificationList) { s: String, s1: String ->
            Timber.tag("Event Waiting RV click").i("$s $s1")
            when(s){
                "Delete" -> { mPresenter.deleteNotification(s1)
                    pb_layout_include.visibility = View.VISIBLE
                }
                "Chat" -> { launchChatIntent(s1) }
                "Accept", "Demand" -> {launchEventDetail(s1) }
                "Rating" ->{launchProfile()}
            }
        }
    }

    override fun launchChatIntent(userId: String) {
        val chatIntent = Intent(requireContext(),ChatLogActivity::class.java)
        chatIntent.putExtra("uid",userId)
        startActivity(chatIntent)
    }

    override fun launchEventDetail(eventId: String) {
        val eventIntent = Intent(requireContext(),EventDetailActivity::class.java)
        eventIntent.putExtra("eid",eventId)
        startActivity(eventIntent)
    }

    override fun launchProfile() {
        val intent = Intent(requireContext(),ProfileActivity::class.java)
        startActivity(intent)
    }
    override fun refreshView() {
        mPresenter.getNotifications()
    }

    /**
     * Create option menu to actionbar.
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.notifications_menu_tb, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    /**
     * Configure the toolbar click on the profile picture.
     */
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.notification_menu_edit -> {
            mPresenter.openAppNotifications(requireContext(), activity!!.packageName)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

}
