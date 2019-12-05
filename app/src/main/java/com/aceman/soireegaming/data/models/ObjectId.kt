package com.aceman.soireegaming.data.models

/**
 * Created by Lionel JOFFRAY - on 02/12/2019.
 */

data class ObjectId(var type: String, var senderId: String, var creatorId: String){

    constructor(): this("","","")
}