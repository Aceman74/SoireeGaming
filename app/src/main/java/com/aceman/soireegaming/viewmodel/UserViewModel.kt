package com.aceman.soireegaming.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aceman.soireegaming.model.User

/**
 * Created by Lionel JOFFRAY - on 13/11/2019.
 */

class UserViewModel : ViewModel() {
    private val user = MutableLiveData<User>()

    init {
        Log.i("UserViewModel","init()")
    }

    fun getUser( userId: Int) : LiveData<User>{
        loadUser(userId)
        return user
    }

    private fun loadUser(userId: Int) {
        user.value = User(userId, "Bob", userId + 20)
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("UserViewModel", "onCleared()")
    }
}