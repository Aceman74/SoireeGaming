/*
 * *
 *  * Created by Lionel Joffray on 19/09/19 21:47
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 19/09/19 19:07
 *
 */

package com.aceman.soireegaming.ui.adapters.profile

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.aceman.soireegaming.data.models.OpinionAndRating
import kotlinx.android.synthetic.main.rating_item.view.*


/**
 * Created by Lionel JOFFRAY - on 15/08/2019.
 *
 * The viewHolder for Estate in MainActivity and Search.
 */
class ProfileViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

    /**
     * Update the view with the picture, and handle the click on it who opens DetailActivity.
     */
    fun updateWithItem(
        opinionAndRating: OpinionAndRating,
        position: Int,
        listener: (String, String) -> Unit
    ) {
        itemView.rating_title_tv.text = opinionAndRating.opinion
        itemView.rating_rating_bar.rating = opinionAndRating.rating.toFloat()

    }
    override fun onClick(view: View) {
    }
}
