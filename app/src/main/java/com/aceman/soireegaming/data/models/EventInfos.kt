package com.aceman.soireegaming.data.models

import android.location.Location

/**
 * Created by Lionel JOFFRAY - on 25/11/2019.
 */

data class EventInfos(var uid: String, var eid: String, var dateCreated: String, var title: String,
                      var description: String, var picture: String, var location: UserLocation,
                      val chipList: List<UserChip>, var dateList: List<String>, var private: Int,
                      var gender: Int, var eat: Int, var sleep: Int){

    constructor(): this("","","","","","",
        UserLocation(-1.0,-1.0,"city"), mutableListOf(), mutableListOf(),
        -1,-1,-1,-1)

}