package com.example.ecocook

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import com.example.ecocook.manage_fridge.DetailList
import com.example.ecocook.manage_fridge.RecipesResult
import com.example.ecocook.manage_fridge.RecipesResult2
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_my_fridge.*
import kotlinx.android.synthetic.main.activity_my_fridge.icon2list
import kotlinx.android.synthetic.main.activity_recipes_search.*

class RecipesSearch : AppCompatActivity() {
    var ingredient = 0      //총 재료
    var rcount = 0      //현재 레시피 수
    var ingredient_array=ArrayList<String>()
    var recipss_array=ArrayList<String>()
    var uri_array=ArrayList<String>()
    val user = Firebase.auth.currentUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipes_search)
        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {  //검색 눌렀을 때
                if(query!=null)
                    searchrecipes(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean { //검색창 수정될 때
                return true
            }

        })
        ll1.setOnClickListener(View.OnClickListener {
            nocate()
        })
        ll2.setOnClickListener(View.OnClickListener {
            selectcate("한식")
        })
        ll3.setOnClickListener(View.OnClickListener {
            selectcate("중식")
        })
        ll4.setOnClickListener(View.OnClickListener {
            selectcate("일식")
        })
        ll5.setOnClickListener(View.OnClickListener {
            selectcate("양식")
        })
        ll6.setOnClickListener(View.OnClickListener {
            selectcate("기타")
        })
        
    }

    override fun onStart() {
        super.onStart()
        var pref = getSharedPreferences("pref", Context.MODE_PRIVATE)
        ingredient = pref.getInt("ingredient",0)            //ingredient값 가져오기
        if(intent.hasExtra("ingredient_array")){
            ingredient_array= intent.getStringArrayListExtra("ingredient_array")!!
        }
        val db = Firebase.firestore
        val f = db.collection(user?.uid.toString())
        f.addSnapshotListener { querySnapshot: QuerySnapshot?, _: FirebaseFirestoreException? ->    //내 전체 재료 가져오기
            if (querySnapshot != null) {
                // 반복문으로 모든 음식에 접근합니다.(document가 음식)
                for (document in querySnapshot.documents) {
                    val obj = document.toObject<UserFridge>()
                    if (obj != null) {
                        ingredient_array.add(obj.name.toString())
                    }
                }
            }
        }

    }
    override fun onBackPressed() {
        startActivity(Intent(this, MainMenuActivity::class.java))
        finish()
    }
    fun nocate(){       //전체 레시피에서 추천
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
                            val ms = ingredient_array.toSet().subtract(setOf(""))   //선택한 재료 셋
                            val ms2 = obj.ingredients!!.toSet()                     //레시피 재료 셋
                            if(ms.subtract(ms2).size<ms.size){
                                recipss_array.add(obj.name.toString())
                                uri_array.add(obj.recipesUrl.toString())
                                rcount+=1
                            }
                            if(rcount==5){
                                break
                            }
                        }
                    }
                    goresult()
                }
            }
    }
    fun selectcate(cate:String){    //특정 카테고리에서 추천
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
                            //category 출력하기(양식인지 중식인지 등..)
                            if(obj.category.toString().equals(cate)){
                                val ms = ingredient_array.toSet().subtract(setOf(""))   //선택한 재료 셋
                                val ms2 = obj.ingredients!!.toSet()                     //레시피 재료 셋
                                if(ms.subtract(ms2).size<ms.size){
                                    recipss_array.add(obj.name.toString())
                                    uri_array.add(obj.recipesUrl.toString())
                                    rcount+=1
                                }
                                if(rcount==5){
                                    break
                                }
                            }
                        }
                    }
                    goresult()
                }
            }
    }
    fun searchrecipes(s : String){
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
                            if(obj.name.toString().equals(s)){
                                recipss_array.add(obj.name.toString())
                                uri_array.add(obj.recipesUrl.toString())
                            }
                        }
                    }
                    goresult()
                }
            }
    }
    fun goresult(){
        val nextIntent = Intent(this, RecipesResult2::class.java)
        nextIntent.putExtra("recipss_array",recipss_array)    //ingredient_array 값 전달
        nextIntent.putExtra("uri_array",uri_array)
        startActivity(nextIntent)
        finish()
    }
}