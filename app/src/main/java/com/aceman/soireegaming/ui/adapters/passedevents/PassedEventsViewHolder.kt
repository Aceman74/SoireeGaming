/*
 * *
 *  * Created by Lionel Joffray on 19/09/19 21:47
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 19/09/19 19:07
 *
 */

package com.aceman.soireegaming.ui.adapters.passedevents

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.aceman.soireegaming.data.models.EventInfos
import com.aceman.soireegaming.utils.Utils
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.event_item_horizontal.view.*


/**
 * Created by Lionel JOFFRAY - on 15/08/2019.
 *
 * The viewHolder for Estate in MainActivity and Search.
 */
class PassedEventsViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
    var title = ""
    var description = ""
    var picture = ""
    var date = ""
    var itemPos = 0



    /**
     * Update the view with the picture, and handle the click on it who opens DetailActivity.
     */
    fun updateWithItem(eventList: EventInfos, position: Int, listener: (String) -> Unit) {
        var i = 0

        itemView.event_main_title_hor_tv.text = eventList.title
        itemView.event_main_date_hor_tv.text = eventList.dateList[0]
        itemView.event_main_desc_hor_tv.text = eventList.description
        itemView.event_main_location_hor_tv.text = eventList.location.city
        itemView.event_user_hor_tv.text = eventList.name
        Glide.with(itemView)
            .load(eventList.picture)
            .circleCrop()
            .into(itemView.event_main_pic_hor)
        for(item in eventList.chipList){
            if(eventList.chipList[i].check){
            addChip(eventList.chipList[i].name)
            }
            i++
        }
        itemView.setOnClickListener {
            listener(eventList.eid)
        }
        itemView.event_main_pic_hor.setOnClickListener {
            listener(eventList.uid)
        }
    }

    fun addChip(chipName: String) {
        if (itemView.misc_event_item_hor.childCount < 2) {
            val chip = Chip(itemView.context)
            chip.text = chipName
            val color = Utils.chipColor(chip)
            chip.setChipBackgroundColorResource(color)
            chip.setTextColor(Color.WHITE)
            itemView.misc_event_item_hor.addView(chip)
        }
    }


    override fun onClick(view: View) {
    }
}
