package com.example.ecocook

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_password_reset.*

class passwordResetActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_reset)

        auth = Firebase.auth


        passwordResetEmailSendButton.setOnClickListener{
            send()
        }

    }

    public fun send() {
        var email=emailEditText.text.toString()


        if(email.isNotEmpty()) //이메일 입력
        {
            auth.sendPasswordResetEmail(email).addOnCompleteListener(this){
                if(it.isSuccessful){
                    Toast.makeText(baseContext, "비밀번호 재설정 이메일을 보냈습니다.",
                        Toast.LENGTH_SHORT).show()

                    finish()
                }
            }
        }
        else{ //이메일, 패스워드를 입력하지 않았음
            Toast.makeText(baseContext, "이메일을 입력해주세요.",
                Toast.LENGTH_SHORT).show()
        }
    }


}