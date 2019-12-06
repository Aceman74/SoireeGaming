package com.aceman.soireegaming.data.models

/**
 * Created by Lionel JOFFRAY - on 20/11/2019.
 *
 * Additionnal player informations.
 */
data class UserInfos(val age: Int, val gender: Int ,val showAge: Boolean, val showGender: Boolean) {

    constructor() : this(-1,-1,false,false)

}


