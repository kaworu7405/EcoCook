package com.example.ecocook

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_my_page.*

class MyPageActivity: AppCompatActivity() {
    var user=Firebase.auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)
        user=Firebase.auth.currentUser
        val db = Firebase.firestore

        val docRef = db.collection("users").document(user!!.uid)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    if(document.exists()){
                        var str= document.getData()?.get("name").toString()
                        UserNameText.setText(str)
                    }
                } else {
                    finish()
                }
            }
            .addOnFailureListener { exception ->
                //Log.d(TAG, "get failed with ", exception)
            }


        ProfileEditButton.setOnClickListener{
            startActivity(Intent(this, ChangeProfileActivity::class.java))
        }

        getFireBaseProfileImage()//프로필이미지 설정
    }

    fun getFireBaseProfileImage(){ //profile 사진을 ImageView에 설정해주는 함수
        var fileName="profile_"+user!!.uid.toString()+".jpg"

        val storageRef=Firebase.storage.reference.child("profile_img/"+fileName)
        storageRef.downloadUrl.addOnSuccessListener { uri->
            Glide.with(this).load(uri).into(UserProfileImage)
        }.addOnFailureListener {
            // Handle any errors
        }
    }


}