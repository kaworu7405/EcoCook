package com.example.ecocook.manage_fridge

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.*
import com.example.ecocook.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_recipes_result.*
import kotlinx.android.synthetic.main.activity_remove_ingredient.*

class RecipesResult : AppCompatActivity() {
    var setting =true
    var ingredient = 0      //총 재료
    var rcount = 0      //현재 레시피 수
    var ingredient_array=ArrayList<String>()
    val user = Firebase.auth.currentUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipes_result)
    }
    override fun onStart() {
        super.onStart()
        var pref = getSharedPreferences("pref", Context.MODE_PRIVATE)
        ingredient = pref.getInt("ingredient",0)            //ingredient값 가져오기
        if(intent.hasExtra("ingredient_array")){
            ingredient_array= intent.getStringArrayListExtra("ingredient_array")!!
        }
        val ms = ingredient_array.toSet().subtract(setOf(""))   //선택한 재료 셋
        var title=""
        var i=1
        var j=ms.size
        println(i)
        println(j)
        for(s in ms){
            if(i==j){
                title+=s+" 레시피 결과"
            }
            else{
                title+=s+", "
                i++
            }
        }
        result_title.text=title
        val db = Firebase.firestore
        db.collection("recipes")
            .addSnapshotListener { querySnapshot: QuerySnapshot?, e: FirebaseFirestoreException? ->
                if (querySnapshot != null) {
                    // 반복문으로 모든 레시피에 접근합니다.(document가 레시피)
                    // Recipes.kt 클래스에 보시면 각 필드 설명적혀있습니다
                    for (document in querySnapshot.documents) {
                        //obj로 각 필드에 접근할 수 있습니다 지금은 db에 간장 파스타밖에 없어서ㅠㅠ..
                        //반복문을 돌려봤자 지금은 간장 파스타만 obj에 저장됩니다.
                        val obj = document.toObject<Recipes>()
                        if (obj != null) {
                            val ms2 = obj.ingredients!!.toSet()                     //레시피 재료 셋
                            if(ms.subtract(ms2).isEmpty()){
                                if(setting==true){
                                    resultlayout.setBackgroundColor(Color.parseColor("#330669FF"))
                                    setting=false
                                }
                                AddLinear()
                                Addresult(obj.name.toString(),obj.recipesUrl.toString())
                                rcount+=1
                            }
                        }
                    }
                }
            }
    }
    override fun onBackPressed() {
        startActivity(Intent(this, MyFridge::class.java))
        finish()
    }
    fun AddLinear(){        //한 줄 추가
        val LL1= LinearLayout(this)      //한 음식재료를 위한 layout공간
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
        youtubebtn.setBackgroundResource(R.drawable.youtube)
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
        sitebtn.setBackgroundResource(R.drawable.recipe)
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
        likebtn.setBackgroundResource(R.drawable.heart)
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