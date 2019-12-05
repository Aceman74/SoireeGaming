package com.aceman.soireegaming.ui.profile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.aceman.soireegaming.R
import com.aceman.soireegaming.data.models.OpinionAndRating
import com.aceman.soireegaming.data.models.User
import com.aceman.soireegaming.data.models.UserChip
import com.aceman.soireegaming.ui.about.AboutActivity
import com.aceman.soireegaming.ui.adapters.profile.ProfileAdapter
import com.aceman.soireegaming.ui.bottomnavigation.messages.chat.ChatLogActivity
import com.aceman.soireegaming.ui.profile.edit.EditProfileActivity
import com.aceman.soireegaming.utils.Utils
import com.aceman.soireegaming.utils.base.BaseActivity
import com.aceman.soireegaming.utils.base.BaseView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.chip.Chip
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.activity_profile.*
import timber.log.Timber


class ProfileActivity(override val activityLayout: Int = R.layout.activity_profile) :
    BaseActivity(), BaseView, ProfileContract.ProfileViewInterface, RatingBar.OnRatingBarChangeListener {

    private val mPresenter: ProfilePresenter = ProfilePresenter()
    lateinit var chipList: MutableList<UserChip>
    var itemPos: Int = -1
    lateinit var user: User
    private var mIntent: String? = ""
    var canRate = true
    var mVisitorId = FirebaseAuth.getInstance().currentUser!!.uid
    var ratingList = mutableListOf<OpinionAndRating>()
    var mRating = 2
    lateinit var mRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter.attachView(this)
       mIntent = intent.getStringExtra("uid")
        userOrIntent()
        setSupportActionBar(profile_tb)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun userOrIntent() {
        if(mIntent == null || mIntent == mVisitorId){
            mPresenter.getUserDataFromFirestore()
            mPresenter.getRating(mVisitorId)
            chipSetting()
            profile_rating_bar.setIsIndicator(true)
            profile_edit_btn.setOnClickListener {
                val intent = Intent(this, EditProfileActivity::class.java)
                startActivity(intent)
            }
        }else{
            mPresenter.getIntentUserDataFromFirestore(mIntent!!)
            mPresenter.getRating(mIntent!!)
            console_ac.visibility = View.GONE
            style_ac.visibility = View.GONE
            profile_edit_btn.text = "Envoyer un message"
            profile_edit_btn.setOnClickListener {
                val intent = Intent(this, ChatLogActivity::class.java)
                intent.putExtra("uid",mIntent)
                startActivity(intent) }
            profile_rating_bar.onRatingBarChangeListener = this
            }
    }

    override fun updateUI(currentUser: User) {
        user = currentUser
        Glide.with(this)
            .load(currentUser.urlPicture)
            .apply(RequestOptions.circleCropTransform())
            .into(profile_picture_iv)
        profile_name_tv.text = currentUser.name
        profile_city_tv.text = currentUser.userLocation.city
        if (currentUser.userInfos.showAge)
            profile_age_tv.text = resources.getStringArray(R.array.age)[currentUser.userInfos.age]
        else
            profile_age_tv.visibility = View.GONE
        if (currentUser.userInfos.showGender)
            profile_gender_tv.text =
                resources.getStringArray(R.array.gender)[currentUser.userInfos.gender]
        else
            profile_gender_tv.visibility = View.GONE
        profile_rating_bar.rating = 2.0f
        mPresenter.getChipList(user.uid)
    }

    override fun updateList(currentUser: User) {
        chipList = currentUser.chipList as MutableList<UserChip>
        for (item in chipList) {
            if (item.check)
                addChip(item.name, item.group)
        }
    }

    fun chipSetting() {

        val console = Utils.ListOfString.listOfConsole()
        val consoleAdapter = ArrayAdapter<String>(applicationContext,
            android.R.layout.simple_dropdown_item_1line, console
        )
        console_ac.setAdapter(consoleAdapter)
        console_ac.threshold = 1
        console_ac.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val selectedItem = parent.getItemAtPosition(position).toString()
                var i = 0
                if (profile_console_chipgroup.childCount == 0) {
                    Toast.makeText(applicationContext, "Ajout $selectedItem", Toast.LENGTH_SHORT)
                        .show()
                    addChip(selectedItem, "Console")
                } else {
                    while (i < profile_console_chipgroup.childCount) {
                        val chip: Chip = profile_console_chipgroup.getChildAt(i) as Chip
                        if (chip.text == selectedItem) {
                            Toast.makeText(
                                applicationContext, "Console $selectedItem déjà ajoutée !",
                                Toast.LENGTH_SHORT
                            ).show()
                            clearAutocomplete()
                            break
                        }
                        if (i == profile_console_chipgroup.childCount - 1) {
                            Toast.makeText(
                                applicationContext, "Ajout $selectedItem", Toast.LENGTH_SHORT
                            ).show()
                            itemPos = i
                            addChip(selectedItem, "Console")
                            break
                        }
                        i++
                    }
                }
            }
        console_ac.onFocusChangeListener = View.OnFocusChangeListener { view, b ->
            if (b) {
                console_ac.showDropDown()
            }
        }

        val style = Utils.ListOfString.listOfStyle()
        val styleAdapter = ArrayAdapter<String>(
            applicationContext,
            android.R.layout.simple_dropdown_item_1line,
            style
        )
        style_ac.setAdapter(styleAdapter)
        style_ac.threshold = 1
        style_ac.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val selectedItem = parent.getItemAtPosition(position).toString()
                var j = 0
                if (style_chipgroup.childCount == 0) {
                    Toast.makeText(applicationContext, "Ajout $selectedItem", Toast.LENGTH_SHORT)
                        .show()
                    addChip(selectedItem, "Style")
                } else {
                    while (j < style_chipgroup.childCount) {
                        val chip: Chip = style_chipgroup.getChildAt(j) as Chip
                        if (chip.text == selectedItem) {
                            Toast.makeText(
                                applicationContext, "Style $selectedItem déjà ajouté !",
                                Toast.LENGTH_SHORT
                            ).show()
                            clearAutocomplete()
                            break
                        }
                        if (j == style_chipgroup.childCount - 1) {
                            Toast.makeText(applicationContext, "Ajout $selectedItem",
                                Toast.LENGTH_SHORT
                            ).show()
                            addChip(selectedItem, "Style")
                            break
                        }
                        j++
                    }
                }
            }
        style_ac.onFocusChangeListener = View.OnFocusChangeListener { view, b ->
            if (b) {
                style_ac.showDropDown()
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
        console_ac.hint = "Rechercher"
        style_ac.clearFocus()
        style_ac.text.clear()
        style_ac.hint = "Rechercher"
    }

    fun addChip(chipName: String, group: String) {
        // Initialize a new chip instance
        val chip = Chip(this)
        chip.text = chipName
        chip.tag = group
        chip.setChipBackgroundColorResource(Utils.chipColor(chip))
        chip.setTextColor(resources.getColor(R.color.primaryTextColor))
        if(mIntent == null){
        chip.isClickable = true
        chip.isCheckable = false
        chip.isCloseIconVisible = true
        }else{
            chip.isClickable = false
            chip.isCheckable = false
            chip.isCloseIconVisible = false
        }
        updateAndSaveChips(chip)
    }

    override fun updateAndSaveChips(chip: Chip) {
        if (chip.tag == "Console") {
            chip.setOnCloseIconClickListener {
                TransitionManager.beginDelayedTransition(profile_console_chipgroup)
                profile_console_chipgroup.removeView(chip)
                for (item in chipList) {
                    if (item.name == chip.text){
                        chipList[chipList.indexOf(item)].check = false
                        break
                    }
                }
                if(mIntent == null)
                mPresenter.updateChip(chipList)
            }
            profile_console_chipgroup.addView(chip)
            for (item in chipList) {
                if (item.name == chip.text){
                    chipList[chipList.indexOf(item)].check = true
                    break
                }
            }
            if(mIntent == null){
            mPresenter.updateChip(chipList)
            clearAutocomplete()
            hideKeyboard(console_ac)
            }
        }

        if (chip.tag == "Style") {
            chip.setOnCloseIconClickListener {
                TransitionManager.beginDelayedTransition(style_chipgroup)
                style_chipgroup.removeView(chip)
                for (item in chipList) {
                    if (item.name == chip.text){
                        chipList[chipList.indexOf(item)].check = false
                        break
                    }
                }
                if(mIntent == null)
                mPresenter.updateChip(chipList)
            }
            style_chipgroup.addView(chip)
            for (item in chipList) {
                if (item.name == chip.text){
                    chipList[chipList.indexOf(item)].check = true
                    break
                }
            }
            if(mIntent == null) {
                mPresenter.updateChip(chipList)
                clearAutocomplete()
                hideKeyboard(style_ac)
            }
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

    override fun setRating(mutableList: QuerySnapshot?) {
        if(mutableList != null && mutableList.size()> 0){
            mRating = 0
        for(item in mutableList)
            ratingList.add(item.toObject(OpinionAndRating::class.java))
            for (rate in ratingList){
                mRating += rate.rating
                if(rate.ratingId == mVisitorId)
                    canRate = false
                profile_rating_bar.setIsIndicator(true)
            }
            mRating /= ratingList.size
            profile_rating_bar.rating = mRating.toFloat()
            configureRecyclerView()
        }
    }

    override fun configureRecyclerView() {
        mRecyclerView = profile_rating_rv
        mRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mRecyclerView.adapter = ProfileAdapter(ratingList) { s: String, s1: String ->
            Timber.tag("Event Waiting RV click").i("$s $s1")
        }
    }

    override fun onRatingChanged(p0: RatingBar?, p1: Float, p2: Boolean) {
        if(p2 && canRate){
        var edittext = EditText(this)
        edittext.maxLines = 1
            edittext.maxWidth = 100
        var dialog = AlertDialog.Builder(this)
        dialog.setTitle("Laissez un avis pour valider la note")
        dialog.setMessage("Vous devez laisser un avis écrit pour valider votre note!")
        dialog.setView(edittext)
        dialog.setPositiveButton("Valider") { _, i ->
            profile_rating_bar.setIsIndicator(true)
            canRate = false
            mPresenter.rateUser(OpinionAndRating(mIntent!!,mVisitorId,edittext.text.toString(),p1.toInt()))
        }
        dialog.setNegativeButton("Annuler") { _, i ->

        }
        dialog.show()
        }
    }

}
