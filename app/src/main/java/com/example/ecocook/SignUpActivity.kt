package com.example.ecocook

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    var TAG = "SignUp"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = Firebase.auth


        signUpButton.setOnClickListener {
            signUp()
        }
        ReturnButton.setOnClickListener {
            finish()
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        //val currentUser = auth.currentUser
    }

    public fun signUp() {
        var email = passwordChangeText.text.toString()
        var password = passwordEditText.text.toString()
        var passwordCheck = passwordCheckEditText.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty() && passwordCheck.isNotEmpty()) //이메일과 패스워드를 모두 입력
        {
            if (password == passwordCheck) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {      //인증 회원가입 성공했을 경우 이메일로 인증 메일 발송
                            auth.currentUser?.sendEmailVerification()       //회원가입 성공한 사용자 정보를 가져와서(auth.currentUser) snedEmail~ 로 인증메일 보내기
                                ?.addOnCompleteListener { sendTask ->          //addOnCompleteListener로 성공여부 체크
                                    if (sendTask.isSuccessful) {              //성공했을 경우 메일에서 인증하면됨
                                        Toast.makeText(
                                            baseContext, "입력하신 이메일로 인증 메일이 전송되었습니다.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        finish()
                                    } else {
                                        Toast.makeText(
                                            baseContext, "이메일 보내기에 실패하였습니다.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                }
                        } else {                      //회원 가입 실패
                            Toast.makeText(
                                baseContext, "회원가입에 실패하였습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }
            } else { //패스워드 두개가 일치하지 않으면 수행
                Toast.makeText(
                    baseContext, "비밀번호가 일치하지 않습니다.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else { //이메일, 패스워드를 입력하지 않았음
            Toast.makeText(
                baseContext, "이메일 또는 비밀번호를 입력해주세요.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


}