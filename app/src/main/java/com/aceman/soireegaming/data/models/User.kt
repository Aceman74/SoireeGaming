package com.aceman.soireegaming.data.models

/**
 * Created by Lionel JOFFRAY - on 13/11/2019.
 *
 * The main user class, used for storing informations on firebase.
 */

data class User(val uid: String, val date: String, val name: String, val email: String, val urlPicture: String?,
                val userLocation: UserLocation, val userInfos: UserInfos, val chipList: List<UserChip>,
                val eventList: MutableList<String>)
{
    constructor() : this("-1","", "Doe", "noEmail", "noUrl", UserLocation(), UserInfos(), listOf<UserChip>(), mutableListOf<String>())
}