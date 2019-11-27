/*
 * *
 *  * Created by Lionel Joffray on 19/09/19 21:47
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 19/09/19 21:47
 *
 */

package com.aceman.soireegaming.ui.adapters.comingevents

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aceman.soireegaming.R
import com.aceman.soireegaming.data.models.EventInfos
import com.aceman.soireegaming.utils.Utils

/**
 * Created by Lionel JOFFRAY - on 15/08/2019.
 *
 * Adapter who shows the Estate on FragmentList in MainActivity and Search.
 */
class ComingEventsAdapter(var eventList: List<EventInfos>, val listener: (Int) -> Unit) : RecyclerView.Adapter<ComingEventsViewHolder>() {

    /**
     * Create the ViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComingEventsViewHolder {
        val v: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.event_item_horizontal, parent, false)
        return ComingEventsViewHolder(v)
    }

    /**
     * Bind the estate to the view, add animation.
     */
    override fun onBindViewHolder(holder: ComingEventsViewHolder, position: Int) {
        holder.updateWithItem(this.eventList[position], position, listener)
        Utils.setFadeAnimation(holder.itemView, holder.itemView.context)
    }

    /**
     * ItemCount.
     */
    override fun getItemCount(): Int {
        return this.eventList.size
    }
}
