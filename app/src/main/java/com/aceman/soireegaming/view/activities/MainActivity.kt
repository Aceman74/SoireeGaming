package com.aceman.soireegaming.view.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.aceman.soireegaming.PagerAdapter
import com.aceman.soireegaming.PassedEventsFragment
import com.aceman.soireegaming.R
import com.aceman.soireegaming.extensions.showFragment
import com.aceman.soireegaming.view.fragments.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * Created by Lionel JOFFRAY - on 13/11/2019.
 */

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("MainActivity", "onCreate()")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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