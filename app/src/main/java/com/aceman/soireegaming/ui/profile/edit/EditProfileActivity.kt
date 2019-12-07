package com.aceman.soireegaming.ui.profile.edit

import android.app.Activity
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.net.toUri
import com.aceman.soireegaming.BuildConfig
import com.aceman.soireegaming.R
import com.aceman.soireegaming.data.models.User
import com.aceman.soireegaming.data.models.UserInfos
import com.aceman.soireegaming.data.models.UserLocation
import com.aceman.soireegaming.ui.profile.ProfileActivity
import com.aceman.soireegaming.utils.Utils
import com.aceman.soireegaming.utils.base.BaseActivity
import com.aceman.soireegaming.utils.base.BaseView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_edit_profile.*
import timber.log.Timber
import java.io.File
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * Created by Lionel JOFFRAY - on 19/11/2019.
 *
 * Edit profile is where user can edit some informations.
 */
class EditProfileActivity(override val activityLayout: Int = R.layout.activity_edit_profile) :
    BaseActivity(), BaseView,
    EditProfileContract.EditProfileViewInterface {
    private val mPresenter: EditProfilePresenter =
        EditProfilePresenter()
    private var file = File("")
    private var changedPic = false
    private var AUTOCOMPLETE_REQUEST_CODE = 100
    var fields: List<Place.Field> =
        listOf(Place.Field.NAME, Place.Field.LAT_LNG)
    lateinit var placesClient: PlacesClient
    var newLatLng = Location("new")
    var mIntent: String? = null
    var mUser = User()
    /**
     * Attach presenter, initialise Places, check for intent, set a double check for user deleting
     * account, set profile picture change listener and change address listener.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(edit_profile_tb)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        mPresenter.attachView(this)
        Places.initialize(applicationContext, BuildConfig.GOOGLE_MAPS_API)
        placesClient = Places.createClient(this)
        mPresenter.getUserDataFromFirestore()
        intentCheck()
        deleteCheckBox()
        changeProfilePicture()
        onClickaddress()
    }

    /**
     * Check if user is on his profile or come from a click on a recyclerview.
     */
    override fun intentCheck() {
        mIntent = intent.getStringExtra("loc")
        if (mIntent != null)
            click_here_location.visibility = View.VISIBLE
    }

    /**
     * If click address change, launch Maps autocomplete intent.
     */
    override fun onClickaddress() {
        edit_profile_address_tv.setOnClickListener {
            val intent = Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY, fields
            )
                .setCountry("FR")
                .setTypeFilter(TypeFilter.CITIES)
                .build(this)
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
        }
    }

    /**
     * Change profile picture on click.
     */
    override fun changeProfilePicture() {
        edit_profile_picture_iv.setOnClickListener {
            imagePicker()
        }
        edit_profile_picture_btn.setOnClickListener {
            imagePicker()
        }
    }

    /**
     * Double check for deletion.
     */
    override fun deleteCheckBox() {
        edit_profile_checkbox.setOnClickListener {
            if (!edit_profile_checkbox.isChecked)
                edit_profile_delete_btn.setBackgroundResource(R.color.greyTextColor)
            else
                edit_profile_delete_btn.setBackgroundResource(R.color.errorTextColor)
        }

        edit_profile_delete_btn.setOnClickListener {
            if (!edit_profile_checkbox.isChecked)
                Toast.makeText(this, "Il faut cocher", Toast.LENGTH_SHORT).show()
            else
                Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * This method start an image picker library for the user. Opens gallery.
     */
    override fun imagePicker() {
        ImagePicker.with(this)
            .galleryOnly()
            .start()
    }

    /**
     * get result of imagepicker or maps.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            when (resultCode) {
                RESULT_OK -> {
                    val place = Autocomplete.getPlaceFromIntent(data!!)
                    Timber.i("%s%s", "%s, ", "Place: ${place.name} , Address: ${place.latLng}")
                    edit_profile_address_tv.text = place.name
                    newLatLng.latitude = place.latLng!!.latitude
                    newLatLng.longitude = place.latLng!!.longitude
                    newLatLng.provider = place.name

                }
                AutocompleteActivity.RESULT_ERROR -> {

                    val status = Autocomplete.getStatusFromIntent(data!!)
                    Timber.i(status.statusMessage)
                }
                RESULT_CANCELED -> {
                    // The user canceled the operation.
                }
            }
        } else {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    //Image Uri will not be null for RESULT_OK
                    val fileUri = data!!.data!!
                    changedPic = true

                    //You can get File object from intent
                    file = ImagePicker.getFile(data)!!

                    Glide.with(this)
                        .load(file)
                        .circleCrop()
                        .into(edit_profile_picture_iv)
                    //You can also get File Path from intent
                    val filePath: String = ImagePicker.getFilePath(data)!!
                }
                ImagePicker.RESULT_ERROR -> {
                    Timber.tag("PICKER ERROR :").e(ImagePicker.getError(data))
                }

                else -> {
                    Toast.makeText(this, "Annulation", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /**
     * Save picture to Firebase.
     */
    override fun saveToStorage(fileUri: Uri) {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val spaceRef = storageRef.child("$uid/profile_picture.jpg")
        spaceRef.putFile(fileUri)
        spaceRef.downloadUrl.addOnCompleteListener {
            mPresenter.updatePictureOnFirestore((it.result.toString()))
        }
    }

    /**
     * Load user informations.
     */
    override fun loadUserInfos(currentUser: User) {
        mUser = currentUser
        Glide.with(this)
            .load(currentUser.urlPicture)
            .apply(RequestOptions.circleCropTransform())
            .into(edit_profile_picture_iv)
        edit_profile_name_et.setText(currentUser.name)
        edit_profile_email_et.setText(currentUser.email)
        edit_member_since_tv.text = currentUser.date
        edit_profile_address_tv.text = currentUser.userLocation.city
        if (currentUser.userInfos.showAge) {
            edit_profile_age_switch.isChecked = true
            if (currentUser.userInfos.age != -1) {
                edit_profile_age_spinner.setSelection(currentUser.userInfos.age)
            }
        }
        if (currentUser.userInfos.showGender) {
            edit_profile_gender_switch.isChecked = true

            if (currentUser.userInfos.gender != -1) {
                edit_profile_gender_spinner.setSelection(currentUser.userInfos.gender)
            }
        }
    }

    /**
     * Inflate the menu; this adds items to the action bar if it is present.
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.edit_profile_menu_tb, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.edit_profile_tb_validate -> {
                mPresenter.saveUserInfosToFirebase(
                    UserInfos(
                        edit_profile_age_spinner.selectedItemPosition,
                        edit_profile_gender_spinner.selectedItemPosition,
                        edit_profile_age_switch.isChecked,
                        edit_profile_gender_switch.isChecked
                    )
                )
                mPresenter.updateNameOnFirestore(edit_profile_name_et.text.toString())
                mPresenter.updateEmailOnFirestore(edit_profile_email_et.text.toString())
                if (edit_profile_address_tv.text != mUser.userLocation.city)
                    mPresenter.updateLocationOnFirestore(
                        UserLocation(
                            newLatLng.latitude,
                            newLatLng.longitude,
                            newLatLng.provider
                        )
                    )
                if (changedPic)
                    saveToStorage(file.toUri())
                Utils.snackBarPreset(findViewById(android.R.id.content), "Profil sauvegardé !")
                Executors.newSingleThreadScheduledExecutor().schedule({
                    intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                }, 2, TimeUnit.SECONDS)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

}