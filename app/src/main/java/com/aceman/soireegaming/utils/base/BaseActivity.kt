/*
 * *
 *  * Created by Lionel Joffray on 23/09/19 21:08
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 23/09/19 21:08
 *
 */

package com.aceman.soireegaming.utils.base

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aceman.soireegaming.R
import com.aceman.soireegaming.ui.login.LoginActivity
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import timber.log.Timber

/**
 * Created by Lionel JOFFRAY - on 02/05/2019.
 *
 *
 * Base Class for all Activity.
 *
 */
abstract class BaseActivity : AppCompatActivity() {
    val SIGN_OUT_TASK = 112
    /**
     * Load the theme from SharedPreferences.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(this.activityLayout)
    }

    /**
     * Get Layout.
     *
     * @return layout
     */
    abstract val activityLayout: Int

    /**
     * Get current user on Firebase Auth.
     *
     * @return current user
     */
    private val currentUser: FirebaseUser?
        get() = FirebaseAuth.getInstance().currentUser

    /**
     * Check if user is logged.
     *
     * @return log state
     */
    protected val isCurrentUserLogged: Boolean?
        get() = this.currentUser != null

    /**
     * Sign out user from firebase method.
     */
    open fun signOutUserFromFirebase() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnSuccessListener(this, this.updateUIAfterRESTRequestsCompleted(SIGN_OUT_TASK))
                .addOnFailureListener(onFailureListener())
        val start = Intent(applicationContext, LoginActivity::class.java)
        startActivity(start)
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    /**
     * Update UI.
     */
    fun updateUIAfterRESTRequestsCompleted(origin: Int): OnSuccessListener<Void> {
        return OnSuccessListener {
            if (origin == SIGN_OUT_TASK) {
                finish()
            }
        }
    }
    /**
     * On Failure listener for Firebase REST.
     *
     * @return failure
     */
    protected fun onFailureListener(): OnFailureListener {
        return OnFailureListener { e ->
            Toast.makeText(applicationContext, "Erreur inconnue...", Toast.LENGTH_LONG).show()
            Timber.tag("Firebase ERROR").e(e)
        }
    }
}


