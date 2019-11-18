package com.aceman.soireegaming.ui.login

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.aceman.soireegaming.R
import com.aceman.soireegaming.data.models.User
import com.aceman.soireegaming.ui.home.MainActivity
import com.aceman.soireegaming.ui.viewmodel.LoginViewModel
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.multi.DialogOnAnyDeniedMultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_login.*

@Suppress("DEPRECATED_IDENTITY_EQUALS")
class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel
    private val RC_SIGN_IN = 111

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        checkPermission()
        viewModel = ViewModelProviders.of(this)
            .get(LoginViewModel::class.java)
        isLoggedUser()

        main_login_bt.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        main_login_known_user_tv.setOnClickListener {
            startSignInActivity()
        }
    }

    private fun isLoggedUser() {
        when (viewModel.isCurrentlyLogged()) {
            false -> {
                main_login_bt.text = "Continuer"
                main_login_known_user_tv.text = "Créer un nouveau compte ?"
            }
            true -> {
                main_login_bt.text = "Se connecter"
                main_login_known_user_tv.visibility = View.INVISIBLE
            }
        }
    }

    /**
     * Check if user already grant permission/
     */
    /**
     * Check if user already grant permission.
     */
    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) !== PackageManager.PERMISSION_GRANTED &&
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
    fun askPermission() {
        val alertDialog = AlertDialog.Builder(this@LoginActivity)
        alertDialog.setTitle("Soirée Gaming")
        alertDialog.setMessage("For fully working features, this application needs some permissions from you.")
        alertDialog.setPositiveButton(
            android.R.string.yes
        ) { dialog, which ->
            dialog.dismiss()
            dexterInit()
        }
        alertDialog.show()

    }

    /**
     * Launch the sign in Activity for result.
     */
    fun startSignInActivity() {

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
     * Sign out user from firebase method.
     */
    fun signOutUserFromFirebase() {
        AuthUI.getInstance()
            .signOut(this)
        val start = Intent(applicationContext, LoginActivity::class.java)
        startActivity(start)
        isLoggedUser()
    }

    /**
     * Dexter library used for permissions.
     */
    private fun dexterInit() {
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
                Manifest.permission.ACCESS_COARSE_LOCATION
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
                val user = FirebaseAuth.getInstance().currentUser
                if (user != null)
                    viewModel.saveUserToFirebase(
                        User(
                            user.uid,
                            user.displayName!!,
                            user.email!!,
                            user.photoUrl.toString()
                        )
                    )
                main_login_bt.text = "Continuer"

            } else { // ERRORS
                if (response == null) {
                    signOutUserFromFirebase()
                    Toast.makeText(this, "Annulation", Toast.LENGTH_SHORT).show()

                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("LoginActivity", "onDestroy()")
    }
}
