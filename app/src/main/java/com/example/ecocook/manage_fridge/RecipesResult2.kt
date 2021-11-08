package com.example.ecocook.manage_fridge

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.example.ecocook.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_recipes_result.*

class RecipesResult2 : AppCompatActivity() {
    var rcount = 0      //현재 레시피 수
    var ingredient_array=ArrayList<String>()
    var recipss_array=ArrayList<String>()
    var uri_array=ArrayList<String>()
    val user = Firebase.auth.currentUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipes_result2)
    }
    override fun onStart() {
        super.onStart()
        println("여긴가")
        if(intent.hasExtra("recipss_array")){
            recipss_array= intent.getStringArrayListExtra("recipss_array")!!
        }
        if(intent.hasExtra("uri_array")){
            uri_array= intent.getStringArrayListExtra("uri_array")!!
        }
        if(!(recipss_array.isEmpty())){
            for(i in 0..recipss_array.size-1){
                AddLinear()
                Addresult(recipss_array[i],uri_array[i])
            }
        }
    }
    override fun onBackPressed() {
        startActivity(Intent(this, RecipesSearch::class.java))
        finish()
    }
    fun AddLinear(){        //한 줄 추가
        val LL1= LinearLayout(this)      //한 음식재료를 위한 layout공간
        LL1.setBackgroundColor(Color.BLUE)     //임시 배경색
        LL1.id=(10000+rcount)                   //id부여
        LL1.gravity= Gravity.CENTER             //gravity를 center로
        LL1.orientation= LinearLayout.HORIZONTAL
        result.addView(LL1)     //60000+lcount-1의 tablerow에 layout추가
        //(findViewById<LinearLayout>(10000+rcount).layoutParams as LinearLayout.LayoutParams).setMargins(changeDP(20),changeDP(5),0,0)
        //현재 만든 layout에 마진값 추가

    }
    fun Addresult(a : String, b : String){    //재료 추가

        val textname = TextView(this)           //재료이름
        textname.text = a                           //글자 알아서 내려감
        textname.gravity= Gravity.CENTER
        textname.layoutParams = LinearLayout.LayoutParams(
            changeDP(200), LinearLayout.LayoutParams.WRAP_CONTENT
        )
        textname.id=(20000+rcount)
        textname.setTextSize(20f)
        textname.setTextColor(Color.WHITE)
        findViewById<LinearLayout>(10000+rcount).addView(textname)


        val youtubebtn = Button(this)           //개수 설정
        youtubebtn.gravity= Gravity.CENTER                 //gravity설정
        youtubebtn.layoutParams = LinearLayout.LayoutParams(
            changeDP(60), changeDP(50)
        )
        youtubebtn.id=(30000+rcount)
        findViewById<LinearLayout>(10000+rcount).addView(youtubebtn)
        youtubebtn.setOnClickListener() {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/results?search_query="+a)))
            finish()
        }

        val sitebtn = Button(this)           //개수 설정
        sitebtn.gravity= Gravity.CENTER                 //gravity설정
        sitebtn.layoutParams = LinearLayout.LayoutParams(
            changeDP(60), changeDP(50)
        )
        sitebtn.id=(40000+rcount)
        findViewById<LinearLayout>(10000+rcount).addView(sitebtn)
        sitebtn.setOnClickListener() {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(b)))
            finish()
        }
        val likebtn = Button(this)           //개수 설정
        likebtn.gravity= Gravity.CENTER                 //gravity설정
        likebtn.layoutParams = LinearLayout.LayoutParams(
            changeDP(60), changeDP(50)
        )
        likebtn.id=(50000+rcount)
        findViewById<LinearLayout>(10000+rcount).addView(likebtn)
        likebtn.setOnClickListener() {
            if (user != null) {
                val db = Firebase.firestore
                val docRef = db.collection("users").document(user.uid)
                docRef.get().addOnSuccessListener { documentSnapshot ->
                    val obj = documentSnapshot.toObject<MemberInfo>()
                    if (obj != null) {
                        if (obj.myRecipes==null) {
                            var arr=ArrayList<String>()
                            arr.add(a+"]"+b)
                            obj.myRecipes=arr
                            docRef.set(obj)
                        }
                        else{
                            if(!(obj.myRecipes!!.contains((a+"]"+b)))){
                                obj.myRecipes!!.add(a+"]"+b)
                                docRef.set(obj)
                            }
                        }
                    }
                }
            }
        }

    }
    private fun changeDP(value : Int) : Int{        //int값을 dp값으로 변환
        var displayMetrics = resources.displayMetrics
        var dp = Math.round(value * displayMetrics.density)
        return dp
    }
}