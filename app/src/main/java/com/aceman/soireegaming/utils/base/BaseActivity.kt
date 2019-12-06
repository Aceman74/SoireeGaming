/*
 * *
 *  * Created by Lionel Joffray on 23/09/19 21:08
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 23/09/19 21:08
 *
 */

package com.aceman.soireegaming.utils.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aceman.soireegaming.R
import com.aceman.soireegaming.ui.login.LoginActivity
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
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
     * Sign out user from firebase method.
     */
    open fun signOutUserFromFirebase() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnSuccessListener(this, this.updateUIAfterRESTRequestsCompleted(SIGN_OUT_TASK))
                .addOnFailureListener(onFailureListener())
    }


    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
    /**
     * Update UI.
     */
    fun updateUIAfterRESTRequestsCompleted(origin: Int): OnSuccessListener<Void> {
        return OnSuccessListener {
            if (origin == SIGN_OUT_TASK) {
                finishAffinity()
                val start = Intent(applicationContext, LoginActivity::class.java)
                startActivity(start)
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
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


