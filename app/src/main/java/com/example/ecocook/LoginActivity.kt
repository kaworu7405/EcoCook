package com.example.ecocook

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    var TAG="SignUp"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth


        LoginButton.setOnClickListener{
            login()
        }
        SignUpButton.setOnClickListener{
            //Login Activity로 이동
            val nextIntent = Intent(this, SignUpActivity::class.java)
            startActivity(nextIntent)
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
    }

    public fun login() {
        var email=emailEditText.text.toString()
        var password=passwordEditText.text.toString()

        if(email.isNotEmpty() && password.isNotEmpty()) //이메일과 패스워드를 모두 입력
        {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        if (FirebaseAuth.getInstance().currentUser!!.isEmailVerified)
                        {
                            val user = auth.currentUser
                            Toast.makeText(baseContext, "로그인에 성공하였습니다. 환영합니다.",
                                Toast.LENGTH_SHORT).show()
                            val nextIntent = Intent(this, MainMenuActivity::class.java)
                            startActivity(nextIntent)
                        }
                        else {
                            Toast.makeText(baseContext, "이메일 인증이 완료되지 않았습니다.",
                                Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(baseContext, "가입되지 않은 이메일입니다.",
                            Toast.LENGTH_SHORT).show()
                    }
                }

        }
        else{ //이메일, 패스워드를 입력하지 않았음
            Toast.makeText(baseContext, "이메일 또는 비밀번호를 입력해주세요.",
                Toast.LENGTH_SHORT).show()
        }
    }


}