package com.example.ecocook

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_member_init.*

class MemberInitActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    var backKeyPressedTime:Long=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_init)

        UserInfoSendButton.setOnClickListener {
            profileUpdate()
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
    }

    public fun profileUpdate() {
        var name=NameEditText.text.toString()
        var phoneNumber=PhoneNumberEditText.text.toString()
        var birthday=BirthdayEditText.text.toString()
        var address=AddressEditText.text.toString()


        if(name.isEmpty()||name.length>8){
            Toast.makeText(baseContext, "이름을 1~8자 사이로 입력해주세요.",
                Toast.LENGTH_SHORT).show()
        }
        else if(name.isNotEmpty() && phoneNumber.length>9 && birthday.length>5 && address.isNotEmpty()) //이메일과 패스워드를 모두 입력
        {
            val user = Firebase.auth.currentUser
            val db = Firebase.firestore

            val memberInfo = MemberInfo(name, phoneNumber, birthday,address, null, "[일반]")

            if(user!=null)
            {
                db.collection("users").document(user!!.uid).set(memberInfo)
                    .addOnSuccessListener {
                        Toast.makeText(baseContext, "회원정보 등록을 성공하였습니다.",
                        Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener {Toast.makeText(baseContext, "회원정보 등록에 실패하였습니다.",
                        Toast.LENGTH_SHORT).show()}
            }
        }
        else{ //입력하지 않았음
            Toast.makeText(baseContext, "올바른 값을 입력해주세요.",
                Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        if(System.currentTimeMillis()-backKeyPressedTime<2000){
            finishAffinity()
        }
        backKeyPressedTime=System.currentTimeMillis()
    }
}