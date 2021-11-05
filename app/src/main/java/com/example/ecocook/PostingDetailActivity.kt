package com.example.ecocook

import android.graphics.Point
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_posting_detail.*
import kotlinx.android.synthetic.main.comment_view.*
import kotlinx.android.synthetic.main.post_view.*

class PostingDetailActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posting_detail)

        postingContentText.movementMethod = ScrollingMovementMethod()
        setValues()
    }
/*
val comments : List<Map<String, String>>?=null, //댓글
 */
    fun setValues(){
        val postingInfo=intent.getSerializableExtra("postingInfo") as Posting

        buyDateText.text=postingInfo.buyDate.toString()
        expiryDateText.text=postingInfo.expiryDate.toString()
        postingContentText.text=postingInfo.postingContent.toString()
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