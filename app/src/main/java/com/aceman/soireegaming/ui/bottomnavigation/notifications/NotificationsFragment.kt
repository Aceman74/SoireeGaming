package com.aceman.soireegaming.ui.bottomnavigation.notifications


import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

import com.aceman.soireegaming.R
import com.aceman.soireegaming.utils.base.BaseView
import kotlinx.android.synthetic.main.fragment_notifications.*

/**
 * A simple [Fragment] subclass.
 */
class NotificationsFragment : Fragment(), BaseView, NotificationsContract.NotificationsViewInterface {
    private val mPresenter: NotificationsPresenter = NotificationsPresenter()
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
            //TODO
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

}
