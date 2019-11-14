package com.aceman.soireegaming.view.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.aceman.soireegaming.R
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        setSupportActionBar(profile_tb)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    /**
     * Inflate the menu; this adds items to the action bar if it is present.
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.profile_menu_tb, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.profile_tb_notifications -> {
                //GO TO PUSH NOTIFICATIONS
                true
            }
            R.id.profile_tb_about -> {
                intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.profile_tb_logout -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
}
