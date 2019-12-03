package com.aceman.soireegaming.ui.profile.edit

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.net.toUri
import com.aceman.soireegaming.R
import com.aceman.soireegaming.data.models.User
import com.aceman.soireegaming.data.models.UserInfos
import com.aceman.soireegaming.utils.base.BaseActivity
import com.aceman.soireegaming.utils.base.BaseView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_edit_profile.*
import timber.log.Timber
import java.io.File

class EditProfileActivity(override val activityLayout: Int = R.layout.activity_edit_profile) : BaseActivity(), BaseView,
    EditProfileContract.EditProfileViewInterface {
    private val mPresenter: EditProfilePresenter =
        EditProfilePresenter()
    private var file = File("")
    private var changedPic = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(edit_profile_tb)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        mPresenter.attachView(this)
        mPresenter.getUserDataFromFirestore()
        deleteCheckBox()
        changeProfilePicture()
    }

    private fun changeProfilePicture() {

        edit_profile_picture_iv.setOnClickListener {
            imagePicker()
        }
        edit_profile_picture_btn.setOnClickListener {
            imagePicker()
        }
    }


    override fun deleteCheckBox() {
        edit_profile_checkbox.setOnClickListener {
            if(!edit_profile_checkbox.isChecked)
                edit_profile_delete_btn.setBackgroundResource(R.color.greyTextColor)
            else
                edit_profile_delete_btn.setBackgroundResource(R.color.errorTextColor)}

            edit_profile_delete_btn.setOnClickListener {
                if(!edit_profile_checkbox.isChecked)
                Toast.makeText(this,"Il faut cocher",Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(this,"OK",Toast.LENGTH_SHORT).show()
            }
    }

    /**
     * This method start an image picker library for the user. Opens gallery.
     */
    fun imagePicker() {
        ImagePicker.with(this)
            .galleryOnly()
            .start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
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
                val filePath:String = ImagePicker.getFilePath(data)!!
            }
            ImagePicker.RESULT_ERROR -> {
                Timber.tag("PICKER ERROR :").e(ImagePicker.getError(data))
            }
            else -> {
                Toast.makeText(this, "Annulation", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveToStorage(fileUri: Uri) {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val spaceRef = storageRef.child("$uid/profile_picture.jpg")
        spaceRef.putFile(fileUri)
        spaceRef.downloadUrl.addOnCompleteListener {
            mPresenter.updatePictureOnFirestore((it.result.toString()))
        }
    }

    override fun loadUserInfos(currentUser: User) {
        Glide.with(this)
            .load(currentUser.urlPicture)
            .apply(RequestOptions.circleCropTransform())
            .into(edit_profile_picture_iv)
        edit_profile_name_et.setText(currentUser.name)
        edit_profile_email_et.setText(currentUser.email)
        edit_member_since_tv.text = currentUser.Date
            if(currentUser.userInfos.showAge){
            edit_profile_age_switch.isChecked = true
            if(currentUser.userInfos.age != -1){
                edit_profile_age_spinner.setSelection(currentUser.userInfos.age)
            }
        }
        if(currentUser.userInfos.showGender){
            edit_profile_gender_switch.isChecked = true

            if(currentUser.userInfos.gender != -1){
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
                    UserInfos(edit_profile_age_spinner.selectedItemPosition,
                    edit_profile_gender_spinner.selectedItemPosition,
                    edit_profile_age_switch.isChecked,
                    edit_profile_gender_switch.isChecked))
                mPresenter.updateNameOnFirestore(edit_profile_name_et.text.toString())
                mPresenter.updateEmailOnFirestore(edit_profile_email_et.text.toString())
                if(changedPic)
                saveToStorage(file.toUri())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

}
