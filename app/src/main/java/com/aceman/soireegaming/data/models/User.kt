package com.aceman.soireegaming.data.models

/**
 * Created by Lionel JOFFRAY - on 13/11/2019.
 */

data class User(val uId: String, val name: String, val email: String, val urlPicture: String?)
{
    constructor() : this("", "", "", "")
}