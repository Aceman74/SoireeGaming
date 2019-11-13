package com.aceman.soireegaming

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("LoginActivity","onCreate()")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportFragmentManager.beginTransaction()
            .add(android.R.id.content,MainFragment())
            .commit()

        viewModel = ViewModelProviders.of(this)
            .get(UserViewModel::class.java)

        viewModel.getUser(1).observe(this, Observer {
            Log.i("LoginActivity", "User = ${it.name}, age = ${it.age}, ID = ${it.id}")
        })
        main_login_bt.setOnClickListener {
           var intent = Intent(this,MainActivity::class.java)
            startActivity(intent)

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("LoginActivity","onDestroy()")
    }
}
