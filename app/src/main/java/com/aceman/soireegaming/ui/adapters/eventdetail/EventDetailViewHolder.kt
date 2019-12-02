/*
 * *
 *  * Created by Lionel Joffray on 19/09/19 21:47
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 19/09/19 19:07
 *
 */

package com.aceman.soireegaming.ui.adapters.eventdetail

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.aceman.soireegaming.data.models.User
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.contact_list_item.view.*


/**
 * Created by Lionel JOFFRAY - on 15/08/2019.
 *
 * The viewHolder for Estate in MainActivity and Search.
 */
class EventDetailViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
    var title = ""
    var description = ""
    var picture = ""
    var date = ""
    var itemPos = 0


    /**
     * Update the view with the picture, and handle the click on it who opens DetailActivity.
     */
    fun updateWithItem(user: User, position: Int, listener: (String, String) -> Unit, isowner: Boolean) {
        var i = 0

if(isowner){
    itemView.contact_add.visibility = View.VISIBLE
    itemView.contact_remove.visibility = View.VISIBLE
    itemView.contact_add.setOnClickListener {
        listener("add",user.uid)
    }
    itemView.contact_remove.setOnClickListener {
        listener("remove",user.uid)
    }
}
        itemView.message_list_title.text = user.name
        itemView.message_list_date.text = "29/11/1990"
        Glide.with(itemView)
            .load(user.urlPicture)
            .circleCrop()
            .into(itemView.message_list_picture)
        itemView.setOnClickListener {
            listener(user.uid, position.toString())
        }

    }

    override fun onClick(view: View) {
    }
}
