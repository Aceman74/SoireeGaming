/*
 * *
 *  * Created by Lionel Joffray on 19/09/19 21:47
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 19/09/19 21:47
 *
 */

package com.aceman.soireegaming.ui.adapters.eventdetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aceman.soireegaming.R
import com.aceman.soireegaming.data.models.User
import com.aceman.soireegaming.utils.Utils

/**
 * Created by Lionel JOFFRAY - on 15/08/2019.
 *
 * Adapter who shows the User who wainting to join, or are already joining the Event.
 */
class EventDetailAdapter(var userList: MutableList<User>, var isOwner: Boolean, var waitingList : Boolean, val listener: (String, String) -> Unit) : RecyclerView.Adapter<EventDetailViewHolder>() {

    /**
     * Create the ViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventDetailViewHolder {
        val v: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.contact_list_item, parent, false)
        return EventDetailViewHolder(v)
    }

    /**
     * Bind the user to the view, add animation.
     */
    override fun onBindViewHolder(holder: EventDetailViewHolder, position: Int) {
        holder.updateWithItem(this.userList[position], position, listener, isOwner, waitingList)
        Utils.setFadeAnimation(holder.itemView, holder.itemView.context)
    }

    /**
     * ItemCount.
     */
    override fun getItemCount(): Int {
        return this.userList.size
    }
}
