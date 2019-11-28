package com.aceman.soireegaming.ui.home.main

import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.View
import com.aceman.soireegaming.R
import com.aceman.soireegaming.data.extensions.showFragment
import com.aceman.soireegaming.data.models.UserLocation
import com.aceman.soireegaming.ui.bottomnavigation.explore.ExploreFragment
import com.aceman.soireegaming.ui.bottomnavigation.messages.MessagesFragment
import com.aceman.soireegaming.ui.bottomnavigation.notifications.NotificationsFragment
import com.aceman.soireegaming.ui.home.HomeFragment
import com.aceman.soireegaming.ui.login.MainContract
import com.aceman.soireegaming.utils.base.BaseActivity
import com.aceman.soireegaming.utils.base.BaseView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import timber.log.Timber
import java.util.*

/**
 * Created by Lionel JOFFRAY - on 13/11/2019.
 */

class MainActivity(override val activityLayout: Int = R.layout.activity_main) : BaseActivity(),
    BaseView, MainContract.MainViewInterface {
    private val mPresenter: MainPresenter =
        MainPresenter()
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("Main Activity onCreate()")
        mPresenter.attachView(this)
        setSupportActionBar(main_tb)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        bottomNavigationView.selectedItemId = R.id.bot_accueil
        pb_layout_include.visibility = View.VISIBLE
        getLocation()
    }


    fun getLocation() {
        var latitude: Double = -1.0
        var longitude: Double = -1.0
        var city = "-1"
        val geocoder: Geocoder = Geocoder(this, Locale.getDefault())
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location ->
                latitude = location.latitude
                longitude = location.longitude
                city = mPresenter.getCity(latitude, longitude, geocoder)
                Timber.tag("CITY: ").i(city)
                pb_layout_include.visibility = View.INVISIBLE
                val userLoc = UserLocation(latitude, longitude, city)
                mPresenter.saveUserLocationToFirebase(userLoc)
            }
    }

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.title.toString()) {
                "Accueil" -> showFragment(R.id.main_container,
                    HomeFragment.newInstance()
                )
                "Explorer" -> showFragment(R.id.main_container, ExploreFragment.newInstance())
                "Notifications" -> showFragment(
                    R.id.main_container,
                    NotificationsFragment.newInstance()
                )
                "Messages" -> showFragment(R.id.main_container, MessagesFragment.newInstance())
                else -> false
            }
        }
}