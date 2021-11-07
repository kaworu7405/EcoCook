package com.example.ecocook.manage_fridge

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import com.example.ecocook.MainMenuActivity
import com.example.ecocook.R
import com.example.ecocook.UserFridge
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_detail_list.*
import kotlinx.android.synthetic.main.activity_my_fridge.*
import kotlinx.android.synthetic.main.activity_my_fridge.table1
import java.text.SimpleDateFormat
import java.util.*

class DetailList : AppCompatActivity() {
    val sortlist0= listOf<String>("입력순","재료명 순","유통기한 임박 순")
    val sortlist1= listOf<String>("재료명 순","유통기한 임박 순","입력순")
    val sortlist2= listOf<String>("유통기한 임박 순","입력순","재료명 순")
    val sortlist= listOf<List<String>>(sortlist0,sortlist1,sortlist2)
    var sortindex= 0
    val objlist= ArrayList<UserFridge>()
    var ingredient = 0      //나의 총 재료 개수
    var icount=0            //현재 만들어진 재료
    val user = Firebase.auth.currentUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_list)

    }
    override fun onStart() {
        super.onStart()
        val db = Firebase.firestore
        val f = db.collection(user?.uid.toString())
        f.addSnapshotListener { querySnapshot: QuerySnapshot?, _: FirebaseFirestoreException? ->
            var pref = getSharedPreferences("pref",Context.MODE_PRIVATE)
            ingredient = pref.getInt("ingredient",0)    //ingredient가져오기 값없으면 0을 가져옴
            sortindex = pref.getInt("sortindex",0)
            setspinner()
            if (querySnapshot != null) {
                // 반복문으로 모든 음식에 접근합니다.(document가 음식)
                // UserFridge.kt에 각 필드 설명 적혀있습니다!
                for (document in querySnapshot.documents) {
                    val obj = document.toObject<UserFridge>()
                    if (obj != null) {
                        objlist.add(obj)
                    }
                }
                if(sortindex==0){
                    for(obj in objlist){
                        AddTablerow()
                        AddLinear()
                        AddIngredient(obj)
                        icount+=1
                    }
                }
                else if(sortindex==1){
                    for(obj in objlist.sortedWith(compareBy ({ it.name },{ it.expiryDate}))){
                        AddTablerow()
                        AddLinear()
                        AddIngredient(obj)
                        icount+=1
                    }
                }
                else if(sortindex==2){
                    for(obj in objlist.sortedWith(compareBy ({ it.expiryDate },{ it.name}))){
                        AddTablerow()
                        AddLinear()
                        AddIngredient(obj)
                        icount+=1
                    }
                }
            }
        }
    }
    fun Restart() {
        startActivity(Intent(this, DetailList::class.java))
        finish()
    }
    override fun onStop() {
        super.onStop()
        var pref = getSharedPreferences("pref", Context.MODE_PRIVATE)
        pref.edit().putInt("ingredient",icount).apply()             //화면종료? 이동시? ingredient저장
    }
    override fun onBackPressed() {
        startActivity(Intent(this, MyFridge::class.java))
        finish()
    }
    fun AddTablerow(){        //한 줄 추가
        val LL= TableRow(this)
        LL.id=(70000+icount)
        detailtable.addView(LL, TableRow.LayoutParams.MATCH_PARENT, changeDP(110))


    }
    @SuppressLint("ResourceType")       //이건 뭔지 잘 모르겠다, id=(int값)할 때 뜨는 에러때문에 추가
    fun AddLinear(){        //한 줄 추가
        //val idArray = resources.obtainTypedArray(R.array.id_group_1)      //res에 있는 id.xml 아이디사용  일단은 다른 방법으로 해서 안씀
        //val id = idArray.getResourceId(ingredient,-1)                     //위와 동일
        val LL= LinearLayout(this)
        //LL.id=id                      //rse에 있는 아이디사용
        LL.id=(10000+icount)
        LL.gravity= Gravity.CENTER
        LL.orientation= LinearLayout.HORIZONTAL
        findViewById<TableRow>(70000+icount).addView(LL,
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)


    }
    @SuppressLint("ResourceType")   //이건 뭔지 잘 모르겠다, id=(int값)할 때 뜨는 에러때문에 추가
    fun AddIngredient(obj : UserFridge){    //재료 추가

        val textname = TextView(this)
        textname.text = obj.name                    //재료이름
        textname.gravity= Gravity.CENTER         //gravity설정
        textname.layoutParams = LinearLayout.LayoutParams(
            changeDP(100), changeDP(50)
        )
        textname.id=(20000+icount)
        findViewById<LinearLayout>(10000+icount).addView(textname)

        val textcategory = TextView(this)
        textcategory.text = obj.category                    //카테고리이름
        textcategory.gravity= Gravity.CENTER         //gravity설정
        textcategory.layoutParams = LinearLayout.LayoutParams(
            changeDP(80), changeDP(50)
        )
        textcategory.id=(30000+icount)
        findViewById<LinearLayout>(10000+icount).addView(textcategory)

        val textdate = TextView(this)           //유통기한 설정
        textdate.text = "D - "+calculateDday(obj.expiryDate.toString()).toString()
        textdate.gravity= Gravity.CENTER                 //gravity설정
        textdate.layoutParams = LinearLayout.LayoutParams(
            changeDP(80), changeDP(50)
        )
        textdate.id=(40000+icount)
        findViewById<LinearLayout>(10000+icount).addView(textdate)

        val textnum = TextView(this)           //개수 설정
        textnum.text = obj.num.toString()
        textnum.gravity= Gravity.CENTER                 //gravity설정
        textnum.layoutParams = LinearLayout.LayoutParams(
            changeDP(80), changeDP(50)
        )
        textnum.id=(50000+icount)
        findViewById<LinearLayout>(10000+icount).addView(textnum)

        val detailbtn = Button(this)           //개수 설정
        detailbtn.text = "상세"
        detailbtn.gravity= Gravity.CENTER                 //gravity설정
        detailbtn.layoutParams = LinearLayout.LayoutParams(
            changeDP(60), changeDP(50)
        )
        detailbtn.id=(60000+icount)
        findViewById<LinearLayout>(10000+icount).addView(detailbtn)
        detailbtn.setOnClickListener() {
            var pref = getSharedPreferences("pref", Context.MODE_PRIVATE)
            pref.edit().putInt("detailindex",detailbtn.id+1-60000).apply()
            startActivity(Intent(this, DetailLook::class.java))
            finish()
        }
    }
    fun setspinner(){
        spinner.adapter=ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,sortlist[sortindex])
        spinner.setSelection(0,false)
        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var pref = getSharedPreferences("pref",Context.MODE_PRIVATE)
                val positionname=sortlist[sortindex][position]
                if(positionname.equals("입력순"))
                    sortindex=0
                else if(positionname.equals("재료명 순"))
                    sortindex=1
                else if(positionname.equals("유통기한 임박 순"))
                    sortindex=2
                pref.edit().putInt("sortindex",sortindex).apply()
                Restart()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }
    private fun changeDP(value : Int) : Int{        //int값을 dp값으로 변환
        var displayMetrics = resources.displayMetrics
        var dp = Math.round(value * displayMetrics.density)
        return dp
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
}