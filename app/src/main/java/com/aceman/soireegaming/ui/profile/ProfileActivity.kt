package com.aceman.soireegaming.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.transition.TransitionManager
import com.aceman.soireegaming.R
import com.aceman.soireegaming.data.models.User
import com.aceman.soireegaming.ui.about.AboutActivity
import com.aceman.soireegaming.utils.Utils
import com.aceman.soireegaming.utils.base.BaseActivity
import com.aceman.soireegaming.utils.base.BaseView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_profile.*


class ProfileActivity(override val activityLayout: Int = R.layout.activity_profile) : BaseActivity(), BaseView, ProfileContract.ProfileViewInterface {

    private val mPresenter: ProfilePresenter = ProfilePresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter.attachView(this)
        setSupportActionBar(profile_tb)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        mPresenter.getUserDataFromFirestore()
        chipTest()
        profile_edit_btn.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }
    }

    override fun updateUI(currentUser: User) {
        Glide.with(this)
            .load(currentUser.urlPicture)
            .apply(RequestOptions.circleCropTransform())
            .into(profile_picture_iv)
        profile_name_tv.text = currentUser.name
        profile_city_tv.text = currentUser.userLocation.city
        if(currentUser.userInfos.showAge)
            profile_age_tv.text =  resources.getStringArray(R.array.age)[currentUser.userInfos.age]
        if(currentUser.userInfos.showGender)
            profile_gender_tv.text = resources.getStringArray(R.array.gender)[currentUser.userInfos.gender]
        profile_rating_bar.rating = 2.0f
    }

    fun chipTest() {
        profile_add_console_bt.setOnClickListener {
            console_ac.visibility = View.VISIBLE

            val console = Utils.ListOfString.listOfConsole()
            // Initialize a new array adapter object
            val consoleAdapter = ArrayAdapter<String>(
                applicationContext, // Context
                android.R.layout.simple_dropdown_item_1line, // Layout
                console // Array
            )
            console_ac.setAdapter(consoleAdapter)
            console_ac.threshold = 1
            console_ac.onItemClickListener =
                AdapterView.OnItemClickListener { parent, view, position, id ->
                    val selectedItem = parent.getItemAtPosition(position).toString()
                    Toast.makeText(applicationContext, "Ajout $selectedItem", Toast.LENGTH_SHORT).show()
                    addChip(selectedItem,"Console")
                }

            // Set a focus change listener for auto complete text view
            console_ac.onFocusChangeListener = View.OnFocusChangeListener { view, b ->
                if (b) {
                    // Display the suggestion dropdown on focus
                    console_ac.showDropDown()
                }
            }
        }

        profile_add_style_bt.setOnClickListener {
            style_ac.visibility = View.VISIBLE

            val style = Utils.ListOfString.listOfStyle()

            val styleAdapter = ArrayAdapter<String>(
                applicationContext, // Context
                android.R.layout.simple_dropdown_item_1line, // Layout
                style // Array
            )
            style_ac.setAdapter(styleAdapter)
            style_ac.threshold = 1
            style_ac.onItemClickListener =
                AdapterView.OnItemClickListener { parent, view, position, id ->
                    val selectedItem = parent.getItemAtPosition(position).toString()
                    Toast.makeText(applicationContext, "Ajout $selectedItem", Toast.LENGTH_SHORT).show()
                    addChip(selectedItem,"Style")
                }

            // Set a focus change listener for auto complete text view
            style_ac.onFocusChangeListener = View.OnFocusChangeListener { view, b ->
                if (b) {
                    // Display the suggestion dropdown on focus
                    style_ac.showDropDown()
                }
            }
        }

    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            hideKeyboard(console_ac)
            hideKeyboard(style_ac)
            clearAutocomplete()
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun clearAutocomplete() {
        console_ac.clearFocus()
        console_ac.text.clear()
        console_ac.visibility = View.GONE
        style_ac.clearFocus()
        style_ac.text.clear()
        style_ac.visibility = View.GONE
    }

    fun addChip(chipName: String,group:String){
        // Initialize a new chip instance
        val chip = Chip(this)
        chip.text = chipName
        chip.tag = group

        // Set the chip icon
        chip.chipIcon = ContextCompat.getDrawable(this,R.drawable.ic_add_pic)
        when(chip.text){
            "PS4","PS3" -> { chip.setChipBackgroundColorResource(R.color.playstation)}
            "Xbox 360", "Xbox One" -> {chip.setChipBackgroundColorResource(R.color.xbox)}
            "PS2", "Xbox" -> {chip.setChipBackgroundColorResource(android.R.color.black)}
            "Dreamcast" -> {chip.setChipBackgroundColorResource(R.color.dreamcast)}
            "Gamecube" -> {chip.setChipBackgroundColorResource(R.color.gamecube)}
            "Wii", "Wii U", "3DS", "Switch" -> {chip.setChipBackgroundColorResource(R.color.wii)}
            "Android" -> {chip.setChipBackgroundColorResource(R.color.android)}
            "PC" -> {chip.setChipBackgroundColorResource(R.color.pc)}
            "Autre" -> {chip.setChipBackgroundColorResource(R.color.autre)}
        }
        chip.setTextColor(resources.getColor(R.color.primaryTextColor))

        chip.isClickable = true
        chip.isCheckable = false

        chip.isCloseIconVisible = true

        if (chip.tag == "Console"){
        chip.setOnCloseIconClickListener{
            // Smoothly remove chip from chip group
            TransitionManager.beginDelayedTransition(profile_console_chipgroup)
            profile_console_chipgroup.removeView(chip)
        }
        // Finally, add the chip to chip group
        profile_console_chipgroup.addView(chip)
        clearAutocomplete()
        hideKeyboard(console_ac)
        }else{
            chip.setOnCloseIconClickListener{
            // Smoothly remove chip from chip group
            TransitionManager.beginDelayedTransition(style_chipgroup)
                style_chipgroup.removeView(chip)
        }
            // Finally, add the chip to chip group
            style_chipgroup.addView(chip)
            clearAutocomplete()
            hideKeyboard(style_ac)

        }
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
                mPresenter.openAppNotifications(this, packageName)
                true
            }
            R.id.profile_tb_about -> {
                intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.profile_tb_logout -> {
                signOutUserFromFirebase()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

}
