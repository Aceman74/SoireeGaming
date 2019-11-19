package com.aceman.soireegaming.ui.profile

import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.aceman.soireegaming.R
import com.aceman.soireegaming.data.models.User
import com.aceman.soireegaming.ui.about.AboutActivity
import com.aceman.soireegaming.utils.base.BaseActivity
import com.aceman.soireegaming.utils.base.BaseView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_profile.*


class ProfileActivity(override val activityLayout: Int = R.layout.activity_profile) : BaseActivity(), BaseView, ProfileContract.ProfileViewInterface {

    private val mPresenter: ProfilePresenter = ProfilePresenter()
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter.attachView(this)
        setSupportActionBar(profile_tb)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        mPresenter.getUserDataFromFirestore()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        profile_edit_btn.setOnClickListener {
            clickLocate()
/*            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)*/
        }
    }

    override fun updateUI(currentUser: User) {
        Glide.with(this)
            .load(currentUser.urlPicture)
            .apply(RequestOptions.circleCropTransform())
            .into(profile_picture_iv)
        profile_name_tv.text = currentUser.name

    }

    fun clickLocate(){
        var latitude : Double = -1.0
        var longitude: Double = -1.0
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location ->
                latitude =  location.latitude
                longitude = location.longitude
            }
        val city: String = mPresenter.hereLocation(latitude,longitude,this)
        Toast.makeText(this,city,Toast.LENGTH_LONG).show()
    }

    override fun signOutUserFromFirebase(){
    }
    /**
     * Inflate the menu; this adds items to the action bar if it is present.
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.profile_menu_tb, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.profile_tb_notifications -> {
                mPresenter.openAppNotifications(this, packageName)
                true
            }
            R.id.profile_tb_about -> {
                intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.profile_tb_logout -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

}
