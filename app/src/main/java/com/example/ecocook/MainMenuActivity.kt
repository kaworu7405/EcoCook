package com.example.ecocook

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ecocook.manage_fridge.MyFridge
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_mainmenu.*

class MainMenuActivity: AppCompatActivity(), View.OnClickListener {
    var backKeyPressedTime:Long=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mainmenu)

        val user = Firebase.auth.currentUser

        if (user != null) { //null이 아니면
            val db = Firebase.firestore
            val docRef = db.collection("users").document(user.uid)
            docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        if(document.exists()){
                            var str= document.getData()?.get("name").toString() +"님 환영합니다!"
                            Toast.makeText(baseContext, str,
                                Toast.LENGTH_SHORT).show()
                        }
                        else {
                            val nextIntent = Intent(this, MemberInitActivity::class.java)
                            startActivity(nextIntent)
                        }
                    } else {
                        val nextIntent = Intent(this, MemberInitActivity::class.java)
                        startActivity(nextIntent)
                    }
                }
                .addOnFailureListener { exception ->
                    //Log.d(TAG, "get failed with ", exception)
                }


        } else { //user가 null이면 login 화면으로
            val nextIntent = Intent(this, LoginActivity::class.java)
            startActivity(nextIntent)
        }

        TempLogoutButton.setOnClickListener{
            //Login Activity로 이동
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java))
        }
        ManageFridgeButton.setOnClickListener(this)         //냉장고 관리로 클릭
    }

    override fun onBackPressed() {                          //이거 뒤로가기 누르는거 연속으로 눌러야 꺼지거나 그런건가?
        if(System.currentTimeMillis()-backKeyPressedTime<2000){
            finishAffinity()
        }
        backKeyPressedTime=System.currentTimeMillis()
    }
    fun manageFridge(){             //냉장고 관리로 넘어감
        startActivity(Intent(this, MyFridge::class.java))
        finish()
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.ManageFridgeButton -> {    //냉장고 관리 버튼을 눌렀을 때
                manageFridge()
            }
        }
    }
}