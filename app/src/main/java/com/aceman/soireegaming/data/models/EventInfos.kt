package com.aceman.soireegaming.data.models

/**
 * Created by Lionel JOFFRAY - on 25/11/2019.
 *
 * Main event object, contains all necessary informations.
 */
data class EventInfos(var uid: String, var name: String, var eid: String, var dateCreated: String, var title: String,
                      var description: String, var picture: String, var location: UserLocation,
                      val chipList: List<UserChip>, var dateList: List<String>,
                      var eventPlayers: MutableList<String> , var eventMisc: EventMisc){

    constructor(): this("","","","","","","",
        UserLocation(-1.0,-1.0,"city"), mutableListOf(), mutableListOf(),
        mutableListOf(), EventMisc()
    )

}