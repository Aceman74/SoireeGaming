package com.aceman.soireegaming.data.models

/**
 * Created by Lionel JOFFRAY - on 20/11/2019.
 */

data class UserLocation(val latitude: Double, val longitude: Double, val city: String){


    constructor() : this(-1.0, -1.0, "SG")
}