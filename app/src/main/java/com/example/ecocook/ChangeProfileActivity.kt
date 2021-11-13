package com.example.ecocook

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_edit_profile.*

class ChangeProfileActivity: AppCompatActivity() {

    var user=Firebase.auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        //val user = Firebase.auth.currentUser
        val db = Firebase.firestore
        user=Firebase.auth.currentUser
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1
        )

        val docRef = db.collection("users").document(user!!.uid)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    if (document.exists()) {
                        var str = document.getData()?.get("name").toString()
                        EditUserName.setText(str)

                        str = document.getData()?.get("phoneNumber").toString()
                        EditUserPhoneNumber.setText(str)

                        str = document.getData()?.get("birthDay").toString()
                        EditUserBirthDay.setText(str)

                        str = document.getData()?.get("address").toString()
                        EditUserAddress.setText(str)

                        if(document.getData()?.get("hasImage").toString()=="true")
                        {
                            getFireBaseProfileImage() //프로필이미지 설정
                        }
                    }
                } else {
                    finish()
                }
            }
            .addOnFailureListener { exception ->
                //Log.d(TAG, "get failed with ", exception)
            }


        RequestEditProfile.setOnClickListener {
            EditProfile()
        }

        DontRequestEditProfile.setOnClickListener {
            finish()
        }

        ChangeUserPhotoButton.setOnClickListener {
            openGallery()
            val docRef = db.collection("users").document(user!!.uid)
            docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        if (document.exists()) {
                            var obj = document.toObject<MemberInfo>()
                            if (obj != null) {
                                obj.hasImage = "true"
                                db.collection("users").document(user!!.uid).set(obj)
                            }
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    //Log.d(TAG, "get failed with ", exception)
                }
        }



    }
    fun openGallery(){
        val intent=Intent(Intent.ACTION_PICK)
        intent.type= MediaStore.Images.Media.CONTENT_TYPE
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode==RESULT_OK){ 
            when(requestCode){
                1 -> { //갤러리에 접근하고자 할 때
                    data?.data?.let{ uri->
                        UserImageView.setImageURI(uri)

                        var fileName="profile_"+user!!.uid.toString()+".jpg"

                        val desertRef=Firebase.storage.reference.child("profile_img/"+fileName)
                        desertRef.delete() //기존 프로필 사진 삭제

                        val storageRef = Firebase.storage.reference.child("profile_img/"+fileName)
                        var uploadTask=storageRef.putFile(uri)

                        uploadTask.addOnFailureListener{
                            Toast.makeText(baseContext, "프로필 사진을 변경하지못하였습니다.",
                                Toast.LENGTH_SHORT).show()
                        }.addOnSuccessListener {
                            Toast.makeText(baseContext, "프로필 사진을 변경하였습니다.",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    public fun EditProfile() {
        var name=EditUserName.text.toString()
        var phoneNumber=EditUserPhoneNumber.text.toString()
        var birthday=EditUserBirthDay.text.toString()
        var address=EditUserAddress.text.toString()


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
                        Toast.makeText(baseContext, "회원정보를 변경하였습니다.",
                            Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(baseContext, "회원정보를 변경하지 못했습니다.",
                        Toast.LENGTH_SHORT).show()}
            }
        }
        else{ // 입력하지 않았음
            Toast.makeText(baseContext, "올바른 값을 입력해주세요.",
                Toast.LENGTH_SHORT).show()
        }
    }

    fun getFireBaseProfileImage(){ //profile 사진을 ImageView에 설정해주는 함수
        var fileName="profile_img/profile_"+user!!.uid.toString()+".jpg"
        if(this.isFinishing){
            return
        }
        val glide=Glide.with(this)
        var storageRef=Firebase.storage.reference
        storageRef.child(fileName).downloadUrl.addOnSuccessListener { uri->
            glide.load(uri).into(UserImageView)
        }.addOnFailureListener {
            // Handle any errors
        }
    }
}