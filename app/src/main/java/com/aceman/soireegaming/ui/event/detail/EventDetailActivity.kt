package com.aceman.soireegaming.ui.event.detail

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aceman.soireegaming.R
import com.aceman.soireegaming.data.firebase.FirestoreOperations
import com.aceman.soireegaming.data.models.EventInfos
import com.aceman.soireegaming.data.models.User
import com.aceman.soireegaming.ui.adapters.eventdetail.EventDetailAdapter
import com.aceman.soireegaming.ui.home.main.MainActivity
import com.aceman.soireegaming.utils.Utils
import com.aceman.soireegaming.utils.base.BaseActivity
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_event_detail.*
import timber.log.Timber
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.math.absoluteValue

class EventDetailActivity(override val activityLayout : Int = R.layout.activity_event_detail) : BaseActivity(),
    EventDetailActivityContract.EventDetailActivityViewInterface, OnMapReadyCallback {
    private val mPresenter: EventDetailActivityPresenter =
        EventDetailActivityPresenter()
    private var mEid: String = ""
    private lateinit var mUser: User
    private lateinit var mEvent: EventInfos
    lateinit var mRecyclerView: RecyclerView
    lateinit var mWaitingRecyclerView: RecyclerView
    private var userList = mutableListOf<User>()
    private var waitingUserList = mutableListOf<User>()
    private lateinit var mMap: GoogleMap
    lateinit var mMarker: Marker
    var isOwner = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter.attachView(this)
        configureMaps()
        getIntentId()
        participate_btn.setOnClickListener {
            onClickParticipate()
        }
    }

    private fun getUserType() {
        mPresenter.typeOfUser(mEid)
    }

    override fun setUserType(type: String) {
        when (type) {
            "Waiting" -> {
                participate_btn.text = "En attente de l'organisateur"
                participate_btn.alpha = 0.5f
                participate_btn.isClickable = false
            }
            "Present" -> {
                if (mUser.uid == mEvent.uid) {
                    isEventOwner()
                } else
                    isParticipant()
            }
        }
    }

    private fun isEventOwner() {
        participate_btn.visibility = View.INVISIBLE
        isOwner = true
        configureWaitingRecyclerView()
    }

    private fun isParticipant() {
        participate_btn.text = "Ne plus participer"
        participate_btn.alpha = 1.0f
       participate_btn.setOnClickListener { mPresenter.removeEventParticipation(mEid, mUser.uid)
           event_detail_rv.visibility = View.INVISIBLE
               Utils.snackBarPreset(findViewById(android.R.id.content),
                   "Participation retirée !")
           Executors.newSingleThreadScheduledExecutor().schedule({
               val intent = Intent(baseContext, MainActivity::class.java)
               startActivity(intent)
               overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
           }, 2, TimeUnit.SECONDS)}

    }

    private fun onClickParticipate() {
        participate_btn.text = "En attente de l'organisateur"
        mPresenter.createEventDemand(mEid)
        participate_btn.isClickable = false
        participate_btn.alpha = 0.5f
    }

    private fun getIntentId() {
        mEid = intent.getStringExtra("eid")!!
        mPresenter.getUserDataFromFirestore()
    }

    override fun updateUI(currentUser: User) {
        mUser = currentUser
        mPresenter.getEventInfos(mEid)
        mPresenter.getEventDemandInfos(mEid)
    }

    override fun updateEventsDemands(userDemand: MutableList<String>) {
        mPresenter.getUserDemandList(userDemand)
    }

    override fun updateEvents(eventDetails: EventInfos, userPresent: MutableList<String>) {
        mPresenter.getUserPresentList(userPresent)
        mEvent = eventDetails
        getUserType()
        mMarker.position = LatLng(mEvent.location.latitude,mEvent.location.longitude)
        mMarker.title = mEvent.location.city
        mMap.addMarker(MarkerOptions().position(mMarker.position)
            .title(mMarker.title))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mMarker.position))
        mMap.cameraPosition.zoom.absoluteValue
        detail_event_title_tv.text = mEvent.title
        detail_event_username_tv.text = mEvent.name
        detail_event_description_tv.text = mEvent.description
        detail_event_location.text = mEvent.location.city
        detail_event_begin_date.text = mEvent.dateList[0]
        detail_event_end_date.text = mEvent.dateList[1]
        detail_event_begin_hour.text = mEvent.dateList[2]
        detail_event_end_hour.text = mEvent.dateList[3]

        Glide.with(this)
            .load(mEvent.picture)
            .circleCrop()
            .into(detail_event_profile_pic)
        detail_private_event_tv.text = resources.getStringArray(R.array.private_event)[mEvent.eventMisc.private]
        detail_gender_event_tv.text= resources.getStringArray(R.array.gender_alt)[mEvent.eventMisc.gender]
        detail_eat_event_tv.text= resources.getStringArray(R.array.eat)[mEvent.eventMisc.eat]
        detail_sleep_event_tv.text= resources.getStringArray(R.array.sleep)[mEvent.eventMisc.sleep]
        for ((i, item) in mEvent.chipList.withIndex()) {
            if (mEvent.chipList[i].check) {
                addChip(mEvent.chipList[i].name, mEvent.chipList[i].group)
            }
        }

        FirestoreOperations.getEngagedUsers(eventDetails.eid).addOnSuccessListener {
            for (item in it.id) {
                Timber.tag("testDetail").i("$it.data ${it.id}")
            }
        }
    }
    /**
     * Set the map.
     */
    fun configureMaps() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.address_map_detail) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }
    /**
     * Called when the map is ready to add all markers and objects to the map.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isMapToolbarEnabled = false
        val Paris = LatLng( 48.864716, 2.349014)
        mMarker = mMap.addMarker(
            MarkerOptions().position(Paris)
                .title("Paris"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Paris))
        mMap.cameraPosition.zoom.absoluteValue
    }
    override fun setUserList(userList: MutableList<User>) {
        this.userList = userList
        configureRecyclerView()
    }

    override fun setDemandList(userDemandList: MutableList<User>) {
        waitingUserList = userDemandList
        configureWaitingRecyclerView()
        getUserType()
    }
    /**
     * Initialize the recyclerview for picture preview.
     */
    fun configureRecyclerView() {
        mRecyclerView = event_detail_rv
        mRecyclerView.addItemDecoration(
            DividerItemDecoration(
                applicationContext,
                DividerItemDecoration.HORIZONTAL
            )
        )
        mRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mRecyclerView.adapter = EventDetailAdapter(userList, isOwner, false) { s: String, s1: String ->
            Timber.tag("Event Detail RV click").i("$s $s1")
            when(s){
                "remove" -> {mPresenter.removeEventParticipation(mEid,s1)
                    Utils.snackBarPreset(findViewById(android.R.id.content),
                        "Participant retiré  :( ")
                refreshView()}
            }

        }
    }

    fun refreshView(){
        userList.clear()
        waitingUserList.clear()
        detail_console_chipgroup.removeAllViews()
        detail_style_chipgroup.removeAllViews()
        mPresenter.getEventInfos(mEid)
        mPresenter.getEventDemandInfos(mEid)
    }
        /**
         * Initialize the recyclerview for picture preview.
         */
        fun configureWaitingRecyclerView() {
            mWaitingRecyclerView = event_detail_waiting_rv
            mWaitingRecyclerView.addItemDecoration(
                DividerItemDecoration(
                    applicationContext,
                    DividerItemDecoration.HORIZONTAL
                )
            )
            mWaitingRecyclerView.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            mWaitingRecyclerView.adapter = EventDetailAdapter(waitingUserList, isOwner,true) { s: String, s1: String ->
                Timber.tag("Event Waiting RV click").i("$s $s1")
                when(s){
                    "add" -> {mPresenter.acceptEventDemand(mEid,s1)
                        mPresenter.removeEventDemand(mEid,s1)
                        Utils.snackBarPreset(findViewById(android.R.id.content),
                            "Participant Ajouté !")
                        refreshView()}
                    "remove" -> {mPresenter.removeEventDemand(mEid,s1)
                        Utils.snackBarPreset(findViewById(android.R.id.content),
                            "Participant refusé  :( ")
                        refreshView()}
                }
                mPresenter.getEventDemandInfos(mEid)
            }
        }

        fun addChip(chipName: String, group: String) {
            // Initialize a new chip instance
            val chip = Chip(this)
            chip.text = chipName
            val color = Utils.chipColor(chip)
            chip.setChipBackgroundColorResource(color)
            chip.setTextColor(Color.WHITE)
            if (group == "Console")
                detail_console_chipgroup.addView(chip)
            else
                detail_style_chipgroup.addView(chip)
        }

}
