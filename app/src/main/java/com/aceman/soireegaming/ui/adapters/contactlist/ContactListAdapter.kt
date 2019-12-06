/*
 * *
 *  * Created by Lionel Joffray on 19/09/19 21:47
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 19/09/19 21:47
 *
 */

package com.aceman.soireegaming.ui.adapters.contactlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aceman.soireegaming.R
import com.aceman.soireegaming.data.models.DateStamp
import com.aceman.soireegaming.data.models.User
import com.aceman.soireegaming.utils.Utils

/**
 * Created by Lionel JOFFRAY - on 15/08/2019.
 *
 * Adapter who shows the contact on MessagesActivity.
 */
class ContactListAdapter(var userList: MutableList<User>, var dateList: MutableList<DateStamp>, val listener: (String) -> Unit) : RecyclerView.Adapter<ContactListViewHolder>() {

    /**
     * Create the ViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactListViewHolder {
        val v: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.contact_list_item, parent, false)
        return ContactListViewHolder(v)
    }

    /**
     * Bind the estate to the view, add animation.
     */
    override fun onBindViewHolder(holder: ContactListViewHolder, position: Int) {
        holder.updateWithItem(this.userList[position], dateList, position, listener)
        Utils.setFadeAnimation(holder.itemView, holder.itemView.context)
    }

    /**
     * ItemCount.
     */
    override fun getItemCount(): Int {
        return this.userList.size
    }
}
