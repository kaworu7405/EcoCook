package com.example.ecocook

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.LoginButton
import kotlinx.android.synthetic.main.activity_mainmenu.*
import kotlinx.android.synthetic.main.activity_sign_up.*

class MainMenuActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mainmenu)
    }
}