/*
 * *
 *  * Created by Lionel Joffray on 19/09/19 21:47
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 19/09/19 19:07
 *
 */

package com.aceman.soireegaming.ui.adapters.mainlist

import android.graphics.Color
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.aceman.soireegaming.R
import com.aceman.soireegaming.data.models.EventInfos
import com.aceman.soireegaming.utils.Utils
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_create_event.*
import kotlinx.android.synthetic.main.event_item.view.*


/**
 * Created by Lionel JOFFRAY - on 15/08/2019.
 *
 * The viewHolder for Estate in MainActivity and Search.
 */
class MainListViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
    var title = ""
    var description = ""
    var picture = ""
    var date = ""
    var itemPos = 0



    /**
     * Update the view with the picture, and handle the click on it who opens DetailActivity.
     */
    fun updateWithItem(eventList: EventInfos, position: Int, listener: (Int) -> Unit) {
        var i = 0

        itemView.event_main_title.text = eventList.title
        itemView.event_main_date.text = eventList.dateList[0]
        itemView.event_main_desc.text = eventList.description
        itemView.event_main_location.text = eventList.location.city
        Glide.with(itemView)
            .load(eventList.picture)
            .circleCrop()
            .into(itemView.event_main_pic)
        for(item in eventList.chipList){
            if(eventList.chipList[i].check){
            setChips(eventList.chipList[i].name)
            }
            i++
        }


    }

    fun setChips(item: String){
        var i = 0
        if (itemView.misc_event_item.childCount == 0) {

            addChip(item)
        } else {
                if (i == itemView.misc_event_item.childCount - 1) {
                    itemPos = i
                    addChip(item)
            }
        }
    }

    fun addChip(chipName: String) {
        // Initialize a new chip instance
        val chip = Chip(itemView.context)
        chip.text = chipName
        chip.setChipBackgroundColorResource(Utils.chipColor(chip))
        chip.setTextColor(Color.WHITE)
        itemView.misc_event_item.addView(chip)
    }


    override fun onClick(view: View) {
    }
}
