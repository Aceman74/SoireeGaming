/*
 * *
 *  * Created by Lionel Joffray on 19/09/19 21:47
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 19/09/19 19:07
 *
 */

package com.aceman.soireegaming.ui.adapters.contactlist

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.aceman.soireegaming.data.models.DateStamp
import com.aceman.soireegaming.data.models.User
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.contact_list_item.view.*


/**
 * Created by Lionel JOFFRAY - on 15/08/2019.
 *
 * The viewHolder for users in MessagesActivity.
 */
class ContactListViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
    var title = ""
    var description = ""
    var picture = ""
    var date = ""
    var itemPos = 0



    /**
     * Update the view with the picture, and handle the click on it who opens ChatLogActivity.
     */
    fun updateWithItem(user: User, date: MutableList<DateStamp>, position: Int, listener: (String) -> Unit) {
        var i = 0

        itemView.message_list_title.text = user.name
        if(date.size > 0)
        itemView.message_list_date.text = "${date[position].date} ${date[position].hour}"
        Glide.with(itemView)
            .load(user.urlPicture)
            .circleCrop()
            .into(itemView.message_list_picture)
        itemView.setOnClickListener {
            listener(user.uid)
        }
    }

    override fun onClick(view: View) {
    }
}
