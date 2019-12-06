package com.aceman.soireegaming.data.models

/**
 * Created by Lionel JOFFRAY - on 02/12/2019.
 * Used to store the 2 id of interaction with user.
 * "id X" send message to "id Y".
 */

data class ObjectId(var type: String, var senderId: String, var creatorId: String){

    constructor(): this("","","")
}