package com.aceman.soireegaming.data.models

/**
 * Created by Lionel JOFFRAY - on 20/11/2019.
 */

data class UserLocation(var latitude: Double, var longitude: Double, var city: String){


    constructor() : this(-1.0, -1.0, "SG")
}