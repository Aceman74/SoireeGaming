package com.aceman.soireegaming.data.models

/**
 * Created by Lionel JOFFRAY - on 05/12/2019.
 *
 * Object for Notification, saved on firebase.
 */
data class FirestoreNotification(var date: String, var id: String, var type: String, var title: String, var body: String
                                 , var otherId: String){

    constructor(): this("","","","","","")
}