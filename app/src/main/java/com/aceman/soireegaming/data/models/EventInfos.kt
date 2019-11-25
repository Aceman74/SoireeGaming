package com.aceman.soireegaming.data.models

import android.location.Location

/**
 * Created by Lionel JOFFRAY - on 25/11/2019.
 */

data class EventInfos(var uid: String, var dateCreated: String, var dateBegin: String,
                      var dateEnd: String, var title: String, var description: String,
                      var picture: String, var location: Location, val chipList: List<UserChip>,
                      var private: Int, var gender: Int, var eat: Int, var sleep: Int)