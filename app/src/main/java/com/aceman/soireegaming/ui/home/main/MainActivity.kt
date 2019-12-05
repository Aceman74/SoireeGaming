package com.aceman.soireegaming.ui.home.main

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.aceman.soireegaming.R
import com.aceman.soireegaming.data.extensions.showFragment
import com.aceman.soireegaming.ui.bottomnavigation.explore.ExploreFragment
import com.aceman.soireegaming.ui.bottomnavigation.messages.MessagesFragment
import com.aceman.soireegaming.ui.bottomnavigation.notifications.NotificationsFragment
import com.aceman.soireegaming.ui.home.HomeFragment
import com.aceman.soireegaming.ui.profile.edit.EditProfileActivity
import com.aceman.soireegaming.utils.base.BaseActivity
import com.aceman.soireegaming.utils.base.BaseView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import timber.log.Timber


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
        checkLocation()
        getTokenFCM()
    }

    private fun checkLocation() {
        mPresenter.getLocation {
            if (it) {
                val mDialog = AlertDialog.Builder(this)
                mDialog.setTitle("Localisation")
                mDialog.setMessage("Veux-tu renseigner ta ville dans le profil pour une utilisation optimal de Soirée Gaming?")
                mDialog.setPositiveButton("Oui ! ") { _: DialogInterface, i: Int ->
                    var intent = Intent(this, EditProfileActivity::class.java)
                    intent.putExtra("loc","loc")
                    startActivity(intent)
                }
                mDialog.setNegativeButton("Plus tard", null)
                mDialog.show()
            }

        }
        pb_layout_include.visibility = View.GONE
    }

    private fun getTokenFCM() {
        var token : String = ""
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }
                // Get new Instance ID token
                 token = task.result?.token!!
                mPresenter.updateToken(token)
                var tokenMap = mutableMapOf<String,String>()
                var user = FirebaseAuth.getInstance().currentUser!!.uid
                tokenMap[user] = token
                mPresenter.updateOrCreateTokenList(token,tokenMap, user) {
                }
                // Log and toast
                val msg = "FCM $token"
                Timber.d("Token $msg")
            })

    }

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.title.toString()) {
                "Accueil" -> showFragment(
                    R.id.main_container,
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