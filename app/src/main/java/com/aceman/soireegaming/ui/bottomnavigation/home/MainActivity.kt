package com.aceman.soireegaming.ui.bottomnavigation.home

import android.os.Bundle
import com.aceman.soireegaming.R
import com.aceman.soireegaming.data.extensions.showFragment
import com.aceman.soireegaming.ui.bottomnavigation.explore.ExploreFragment
import com.aceman.soireegaming.ui.bottomnavigation.messages.MessagesFragment
import com.aceman.soireegaming.ui.bottomnavigation.notifications.NotificationsFragment
import com.aceman.soireegaming.ui.login.MainContract
import com.aceman.soireegaming.ui.login.MainPresenter
import com.aceman.soireegaming.utils.base.BaseActivity
import com.aceman.soireegaming.utils.base.BaseView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import timber.log.Timber

/**
 * Created by Lionel JOFFRAY - on 13/11/2019.
 */

class MainActivity(override val activityLayout: Int = R.layout.activity_main) :BaseActivity(), BaseView, MainContract.MainViewInterface {
    private val mPresenter: MainPresenter = MainPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("Main Activity onCreate()")
        mPresenter.attachView(this)
        setSupportActionBar(main_tb)
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        bottomNavigationView.selectedItemId = R.id.bot_accueil
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when(item.title.toString()){
            "Accueil" -> showFragment(R.id.main_container, HomeFragment.newInstance())
            "Explorer" -> showFragment(R.id.main_container, ExploreFragment.newInstance())
            "Notifications" -> showFragment(R.id.main_container, NotificationsFragment.newInstance())
            "Messages" -> showFragment(R.id.main_container, MessagesFragment.newInstance())
            else -> false
        }
    }
}