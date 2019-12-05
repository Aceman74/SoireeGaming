package com.aceman.soireegaming.data.models

/**
 * Created by Lionel JOFFRAY - on 05/12/2019.
 */
data class OpinionAndRating(var ratedId: String, var ratingId: String, var opinion: String ,var rating: Int){
    constructor(): this("","","",2)
}