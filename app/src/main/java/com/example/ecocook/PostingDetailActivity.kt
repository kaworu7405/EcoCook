package com.example.ecocook

import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.okhttp.internal.Internal.instance
import kotlinx.android.synthetic.main.activity_my_page.*
import kotlinx.android.synthetic.main.activity_posting_detail.*
import kotlinx.android.synthetic.main.comment_view.*
import kotlinx.android.synthetic.main.post_view.*

class PostingDetailActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posting_detail)

        postingContentText.movementMethod = ScrollingMovementMethod()
        setValues()

        commentInputBtn.setOnClickListener {
            val postingInfo=intent.getSerializableExtra("postingInfo") as Posting

            var arr=ArrayList<Map<String, String>>()
            var comments=postingInfo.comments
            if (comments != null) {
                for( comment in comments){
                    arr.add(comment)
                }
            }
            val user = Firebase.auth.currentUser
            if (user != null) {
                arr.add(mapOf(user.uid.toString() to commentInputText.text.toString()))
            }

            postingInfo.comments=arr

            val docRef = Firebase.firestore.collection("Posting").document(postingInfo.id.toString())
            docRef.set(postingInfo)

            val commentAdapter= CommentsAdapter(this, R.layout.comment_view, arr)
            commentListView.adapter=commentAdapter

            commentInputText.setText("")
        }
    }
/*
val comments : List<Map<String, String>>?=null, //댓글
 */
    fun setValues(){
        val postingInfo=intent.getSerializableExtra("postingInfo") as Posting
        getFireBaseProfileImage(postingInfo.userId.toString())
        getFireBaseFoodImage(postingInfo.id.toString())
        buyDateText.text=postingInfo.buyDate.toString()
        expiryDateText.text=postingInfo.expiryDate.toString()
        postingContentText.text=postingInfo.postingContent.toString()
        if(postingInfo.price=="0"){
            priceTextView.text="나눔"
        }else{
            priceTextView.text=postingInfo.price.toString()+"원"
        }

        if(!postingInfo.area1.toString().equals(postingInfo.area2.toString()))
        {
            areaText.text=postingInfo.area1.toString()+" "+postingInfo.area2.toString()
        }else {
            areaText.text="전체 지역"
        }
        val docRef = Firebase.firestore.collection("users").document(postingInfo.userId.toString())
        docRef.get()
        .addOnSuccessListener{document->
            postingUser.text= document.get("name").toString()
        }

        val commentAdapter= postingInfo.comments?.let {
        CommentsAdapter(this, R.layout.comment_view,
            it
        )
    }
    commentListView.adapter=commentAdapter
    }

    fun getFireBaseProfileImage(uid:String){ //profile 사진을 ImageView에 설정해주는 함수
        var fileName="profile_"+uid+".jpg"

        val storageRef=Firebase.storage.reference.child("profile_img/"+fileName)
        storageRef.downloadUrl.addOnSuccessListener { uri->
            Glide.with(this).load(uri).into(userImageView)
        }.addOnFailureListener {
            // Handle any errors
        }
    }

    fun getFireBaseFoodImage(id:String){
        var fileName="posting_"+id+".jpg"

        val storageRef=Firebase.storage.reference.child("posting_img/"+fileName)
        storageRef.downloadUrl.addOnSuccessListener { uri->
            Glide.with(this).load(uri).into(postingFoodImg)
        }.addOnFailureListener {
            // Handle any errors
        }
    }
}

/*
/*val f = db.collection(user?.uid.toString())
            //UserFridge.kt의 class를 object화 하여 db에 저장해줍니다.
            ////////////////////////////////////////////////////////////////////
            //유저의 냉장고에 새로운 음식을 추가하는 코드입니다.
            f.get()
                .addOnSuccessListener { result ->
                    var foodNum=result.size()+1 //현재 냉장고에 저장되어있는 음식의 수에다가 1 더해준 값
                    val data = hashMapOf(
                        "category" to "과일",
                        "name" to "메론",
                        //iconId 설명 : 제가 이후에 음식 종류에 따른 이미지 url가르쳐드릴테니 야채인지 과일인지에 따라 그 url을 여기에 넣어주시면 됩니다!
                        "iconId" to "null",
                        "buyDate" to 20211029,
                        "expiryDate" to 20211105,
                        "num" to 7,
                        "id" to foodNum
                    )
                    f.document(foodNum.toString()).set(data) // 현재 냉장고에 저장되어있는 음식의 수에다가 1더해준 값을 문서의 이름과 id로 정해준다.
                    Log.d("TAG", foodNum.toString())
                }
                .addOnFailureListener { exception ->

                }
            /////////////////////////////////////////////////////////////////////

 */
 */