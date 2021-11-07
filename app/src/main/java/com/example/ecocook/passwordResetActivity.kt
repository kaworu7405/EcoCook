package com.example.ecocook

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_password_reset.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_sign_up.passwordChangeText

class passwordResetActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_reset)

        auth = Firebase.auth

        passwordResetButton.setOnClickListener {
            if (passwordTextView.text.isNotEmpty()&&passwordCheckTextView.text.isNotEmpty()) {
                if (passwordTextView.text.toString().equals(passwordCheckTextView.text.toString())) {
                    if (passwordTextView.text.toString().length >= 6) {
                        changePassword(passwordTextView.text.toString())
                    } else {
                        Toast.makeText(this, "비밀번호를 6자리 이상으로 설정해주세요.", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this, "비밀번호와 확인 비밀번호가 맞지 않습니다.", Toast.LENGTH_LONG).show()
                }
            }
            else {
                Toast.makeText(this, "새로운 비밀번호를 입력해주세요.", Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun changePassword(password:String){
        FirebaseAuth.getInstance().currentUser!!.updatePassword(password).addOnCompleteListener { task ->
            if(task.isSuccessful){
                Toast.makeText(this, "비밀번호가 변경되었습니다.", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this, task.exception.toString(), Toast.LENGTH_LONG).show()

            }
        }
    }


}