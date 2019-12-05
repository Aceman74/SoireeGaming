/*
 * *
 *  * Created by Lionel Joffray on 19/09/19 21:47
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 19/09/19 19:07
 *
 */

package com.aceman.soireegaming.ui.adapters.notifications

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.aceman.soireegaming.R
import com.aceman.soireegaming.data.models.FirestoreNotification
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.notifications_item.view.*


/**
 * Created by Lionel JOFFRAY - on 15/08/2019.
 *
 * The viewHolder for Estate in MainActivity and Search.
 */
class NotificationsViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

    /**
     * Update the view with the picture, and handle the click on it who opens DetailActivity.
     */
    fun updateWithItem(
        notif: FirestoreNotification,
        position: Int,
        listener: (String, String) -> Unit
    ) {
        itemView.not_title_tv.text = notif.title
        itemView.not_body_tv.text = notif.body
        itemView.not_date_tv.text = notif.date
        if (notif.type != "Chat")
            Glide.with(itemView)
                .load(R.drawable.event_logo)
                .centerInside()
                .into(itemView.not_picture_iv)
        if(notif.type == "Rating")
            Glide.with(itemView)
                .load(R.drawable.rating_logo)
                .centerInside()
                .into(itemView.not_picture_iv)

        itemView.not_delete_btn.setOnClickListener {
            listener("Delete",notif.id)
        }
        itemView.setOnClickListener {
            listener(notif.type,notif.otherId)
        }
    }
    override fun onClick(view: View) {
    }
}
