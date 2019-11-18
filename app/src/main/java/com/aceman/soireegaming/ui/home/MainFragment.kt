package com.aceman.soireegaming.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.aceman.soireegaming.ui.viewmodel.LoginViewModel

/**
 * Created by Lionel JOFFRAY - on 13/11/2019.
 */

class MainFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("MainFragment","onCreate()")
        super.onCreate(savedInstanceState)
/*
        viewModel = ViewModelProviders.of(activity!!)
            .get(LoginViewModel::class.java)
            */
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MainFragment","onDestroy()")
    }
}