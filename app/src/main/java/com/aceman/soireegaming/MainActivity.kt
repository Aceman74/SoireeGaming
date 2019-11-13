package com.aceman.soireegaming

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by Lionel JOFFRAY - on 13/11/2019.
 */

class MainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("LoginActivity", "onCreate()")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = PagerAdapter(supportFragmentManager)
        adapter.addFragment(AllEventsFragment.newInstance(), "Tous")
        adapter.addFragment(ComingEventsFragment.newInstance(), "J'y vais")
        adapter.addFragment(FollowedEventsFragment.newInstance(), "Marquées")
        adapter.addFragment(PassedEventsFragment.newInstance(), "Passées")
        main_vp.adapter = adapter
        main_tab_ly.setupWithViewPager(main_vp)
    }

}