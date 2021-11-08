package com.example.ecocook.manage_fridge

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.example.ecocook.R
import com.example.ecocook.UserFridge
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_detail_look.*
import kotlinx.android.synthetic.main.activity_my_fridge.*
import java.text.SimpleDateFormat
import java.util.*

class DetailLook : AppCompatActivity() {
    var detailindex=0
    var detailname=""
    val user = Firebase.auth.currentUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_look)
    }
    override fun onStart() {
        super.onStart()
        var pref = getSharedPreferences("pref", Context.MODE_PRIVATE)
        detailindex = pref.getInt("detailindex",0)    //ingredient가져오기 값없으면 0을 가져옴
        val db = Firebase.firestore
        val f = db.collection(user?.uid.toString())
        f.addSnapshotListener { querySnapshot: QuerySnapshot?, _: FirebaseFirestoreException? ->
            if (querySnapshot != null) {
                // 반복문으로 모든 음식에 접근합니다.(document가 음식)
                // UserFridge.kt에 각 필드 설명 적혀있습니다!
                for (document in querySnapshot.documents) {
                    val obj = document.toObject<UserFridge>()
                    if (obj != null && obj.id==detailindex) {
                        detailname=obj.name.toString()
                        setimg(detailimg,obj.category.toString())
                        detailtext.text=obj.name
                        detailtext1.text="D-"+calculateDday(obj.expiryDate.toString())
                        detailtext2.text="카테고리\n"+obj.category
                        detailtext3.text="구매일자\n"+obj.buyDate.toString()
                        detailtext4.text="유통기한\n"+obj.expiryDate.toString()
                        detailtext5.text="수량\n"+obj.num.toString()
                    }
                }
            }
        }
        backbtn.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, DetailList::class.java))
            finish()
        })
        editbtn.setOnClickListener(View.OnClickListener {
            val nextIntent = Intent(this, EditIngredient::class.java)
            nextIntent.putExtra("detailindex",detailindex)    //detailindex 값 전달
            nextIntent.putExtra("detailname",detailname)    //detailname 값 전달
            startActivity(nextIntent)
            finish()
        })
    }
    override fun onBackPressed() {
        startActivity(Intent(this, DetailList::class.java))
        finish()
    }
    fun calculateDday(date : String): Long {   //D-day계산
        val dateFormat = SimpleDateFormat("yyyyMMdd")
        val endDate = dateFormat.parse(date).time
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time.time
        return (endDate - today -1) / (24 * 60 * 60 * 1000)
    }
    fun setimg(img : LinearLayout,cate:String){
        when(cate){
            "과일"->img.setBackgroundResource(R.drawable.ingredient2)
            "견과"->img.setBackgroundResource(R.drawable.ingredient1)
            "기타"->img.setBackgroundResource(R.drawable.ingredient3)
            "김치/반찬"->img.setBackgroundResource(R.drawable.ingredient4)
            "쌀/곡물"->img.setBackgroundResource(R.drawable.ingredient5)
            "유제품"->img.setBackgroundResource(R.drawable.ingredient6)
            "음료"->img.setBackgroundResource(R.drawable.ingredient7)
            "정육/계란"->img.setBackgroundResource(R.drawable.ingredient8)
            "채소"->img.setBackgroundResource(R.drawable.ingredient9)
        }
    }
}