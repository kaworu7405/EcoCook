package com.example.ecocook

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_function.*

class functionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_function)

        LogoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut() //로그아웃
            startActivity(Intent(this, LoginActivity::class.java))//Login Activity로 이동
            finish()
        }

        changePasswordBtn.setOnClickListener {
            startActivity(Intent(this, passwordResetActivity::class.java))//Login Activity로 이동
            finish()
        }

        sendEmailButton.setOnClickListener {
            val email = Intent(Intent.ACTION_SEND)
            email.type = "plain/text"
            val address = arrayOf("201804232@o.cnu.ac.kr")
            email.putExtra(Intent.EXTRA_EMAIL, address)
            email.putExtra(Intent.EXTRA_SUBJECT, "ECOCOOK BUG REPORT")
            startActivity(email)
        }
    }
}