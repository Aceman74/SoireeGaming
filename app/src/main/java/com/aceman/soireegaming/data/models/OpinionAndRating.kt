package com.aceman.soireegaming.data.models

/**
 * Created by Lionel JOFFRAY - on 05/12/2019.
 *
 * Object who contains the rating and opinion posted on a user.
 */
data class OpinionAndRating(var ratedId: String, var ratingId: String, var opinion: String ,var rating: Int){
    constructor(): this("","","",2)
}