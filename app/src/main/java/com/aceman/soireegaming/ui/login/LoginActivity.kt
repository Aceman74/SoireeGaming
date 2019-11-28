package com.aceman.soireegaming.ui.login

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.aceman.soireegaming.R
import com.aceman.soireegaming.data.models.User
import com.aceman.soireegaming.data.models.UserChip
import com.aceman.soireegaming.data.models.UserInfos
import com.aceman.soireegaming.data.models.UserLocation
import com.aceman.soireegaming.ui.home.main.MainActivity
import com.aceman.soireegaming.utils.ChipsManager
import com.aceman.soireegaming.utils.base.BaseActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.multi.DialogOnAnyDeniedMultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_login.*

@Suppress("DEPRECATED_IDENTITY_EQUALS")
class LoginActivity(override val activityLayout: Int = R.layout.activity_login) : BaseActivity(),
    LoginContract.LoginViewInterface {

    private val mPresenter: LoginPresenter = LoginPresenter()
    private val RC_SIGN_IN = 111
    val chipList:MutableList<UserChip> = mutableListOf<UserChip>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter.attachView(this)
        checkPermission()
        isLoggedUser()
        ChipsManager.initListOfChip(chipList)
    }

    override fun isLoggedUser() {
        when (mPresenter.getCurrentUser()?.uid) {
            null -> {
                main_login_bt.text = "Créer un compte / Se connecter"
                main_login_known_user_tv.visibility = View.INVISIBLE
                main_login_bt.setOnClickListener {
                    startSignInActivity()
                }
            }
            else -> {
                main_login_bt.text = "Continuer"
                main_login_known_user_tv.text = "Créer un nouveau compte ?"
                main_login_known_user_tv.setOnClickListener {
                    startSignInActivity()
                }
                main_login_bt.setOnClickListener {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    /**
     * Check if user already grant permission/
     */
    /**
     * Check if user already grant permission.
     */
    override fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) !== PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            !== PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            !== PackageManager.PERMISSION_GRANTED
        ) {
            askPermission()
        }
    }

    /**
     * Ask user for location and call permission.
     *
     * @see Dexter
     */
    override fun askPermission() {
        val alertDialog = AlertDialog.Builder(this@LoginActivity)
        alertDialog.setTitle("Soirée Gaming")
        alertDialog.setMessage("For fully working features, this application needs some permissions from you.")
        alertDialog.setPositiveButton(
            android.R.string.yes
        ) { dialog, _ ->
            dialog.dismiss()
            dexterInit()
        }
        alertDialog.show()
    }

    /**
     * Launch the sign in Activity for result.
     */
    override fun startSignInActivity() {

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                // .setTheme(R.style.LoginTheme)
                .setAvailableProviders(
                    listOf(
                        AuthUI.IdpConfig.EmailBuilder().build(),
                        AuthUI.IdpConfig.GoogleBuilder().build()
                        //,AuthUI.IdpConfig.FacebookBuilder().build(),
                        //AuthUI.IdpConfig.TwitterBuilder().build()
                    )
                )
                .setIsSmartLockEnabled(false, true)
                // .setLogo(R.drawable.logo)
                .build(),
            RC_SIGN_IN
        )   // Sign In
    }

    /**
     * Dexter library used for permissions.
     */
    override fun dexterInit() {
        val dialogMultiplePermissionsListener = DialogOnAnyDeniedMultiplePermissionsListener.Builder
            .withContext(this)
            .withTitle("Permissions denied")
            .withMessage("Unfortunately, you cannot run the application without these permissions.")
            .withButtonText(android.R.string.ok)
            .build()
        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE

            ).withListener(dialogMultiplePermissionsListener)
            .check()
    }

    /**
     * Handle the response after Signed in.
     *
     * @param requestCode requestCode
     * @param resultCode  resultCode
     * @param data        data
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val user = mPresenter.getCurrentUser()
                if (user != null ){
                    var photoUrl = user.photoUrl.toString()
                    if(user.photoUrl == null)
                         photoUrl = "https://firebasestorage.googleapis.com/v0/b/soireegaming-ccde4.appspot.com/o/profile_default%2Flogo_SG.png?alt=media&token=9e9c6564-a5c6-4759-91a0-04922e8f8840"
                    mPresenter.saveUserToFirebase(
                        User(
                            user.uid,
                            user.displayName!!,
                            user.email!!,
                            photoUrl,
                            UserLocation(),
                            UserInfos(),
                            chipList,
                            mutableListOf()
                        )
                    )
                    mPresenter.saveDate(user)
                }
                isLoggedUser()
                main_login_bt.text = "Continuer"

            } else { // ERRORS
                if (response == null) {
                    signOutUserFromFirebase()
                    isLoggedUser()
                    Toast.makeText(this, "Annulation", Toast.LENGTH_SHORT).show()

                }
            }
        }
    }
}
