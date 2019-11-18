package com.aceman.soireegaming.ui.home

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.aceman.soireegaming.R
import com.aceman.soireegaming.data.extensions.showFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import timber.log.Timber

/**
 * Created by Lionel JOFFRAY - on 13/11/2019.
 */

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("Main Activity onCreate()")
        setContentView(R.layout.activity_main)
        setSupportActionBar(main_tb)
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        bottomNavigationView.selectedItemId = R.id.bot_accueil
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when(item.title.toString()){
            "Accueil" -> showFragment(R.id.main_container, "Home")
            "Explorer" -> showFragment(R.id.main_container, "Explore")
            else -> showFragment(R.id.main_container, item.title.toString())
        }
    }
}