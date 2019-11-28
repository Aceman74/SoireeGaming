package com.aceman.soireegaming.ui.event.detail

import android.graphics.Color
import android.os.Bundle
import com.aceman.soireegaming.BuildConfig
import com.aceman.soireegaming.R
import com.aceman.soireegaming.data.models.EventInfos
import com.aceman.soireegaming.data.models.User
import com.aceman.soireegaming.utils.Utils
import com.aceman.soireegaming.utils.base.BaseActivity
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_event_detail.*

class EventDetailActivity(override val activityLayout : Int = R.layout.activity_event_detail) : BaseActivity(),
    EventDetailActivityContract.EventDetailActivityViewInterface {
    private val mPresenter: EventDetailActivityPresenter =
        EventDetailActivityPresenter()
    private var mEid: String = ""
    private lateinit var mUser : User
    private lateinit var mEvent : EventInfos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter.attachView(this)
        getIntentId()

    }

    private fun getIntentId() {
        mEid = intent.getStringExtra("eid")!!
        mPresenter.getUserDataFromFirestore()
    }

    override fun updateUI(currentUser: User) {
        mUser = currentUser
        mPresenter.getEventInfos(mEid)
    }

    override fun updateEvents(eventDetails: EventInfos) {
        mEvent = eventDetails
        BuildConfig.GOOGLE_MAPS_API
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
        /*
        detail_private_event_tv.text = resources.getStringArray(R.array.private_event)[mEvent.eventMisc.private]
        detail_gender_event_tv.text= resources.getStringArray(R.array.gender_alt)[mEvent.eventMisc.gender]
        detail_eat_event_tv.text= resources.getStringArray(R.array.eat)[mEvent.eventMisc.eat]
        detail_sleep_event_tv.text= resources.getStringArray(R.array.sleep)[mEvent.eventMisc.sleep]*/
        detail_console_chipgroup
        detail_style_chipgroup
        var i = 0
        for(item in mEvent.chipList){
            if(mEvent.chipList[i].check){
                addChip(mEvent.chipList[i].name, mEvent.chipList[i].group)
            }
            i++
        }
    }

    fun addChip(chipName: String, group: String) {
        // Initialize a new chip instance
        val chip = Chip(this)
        chip.text = chipName
        val color = Utils.chipColor(chip)
        chip.setChipBackgroundColorResource(color)
        chip.setTextColor(Color.WHITE)
        if(group == "Console")
        detail_console_chipgroup.addView(chip)
        else
            detail_style_chipgroup.addView(chip)
    }
}
