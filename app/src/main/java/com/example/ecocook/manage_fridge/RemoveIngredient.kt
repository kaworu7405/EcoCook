package com.example.ecocook.manage_fridge

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.*
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
import com.squareup.okhttp.Dispatcher
import kotlinx.android.synthetic.main.activity_my_fridge.*
import kotlinx.android.synthetic.main.activity_remove_ingredient.*
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule
import kotlin.coroutines.CoroutineContext

class RemoveIngredient : AppCompatActivity() {
    var ingredient = 0      //총 재료
    var icount = 0      //현재 만들어진 재료
    var lcount = 0      //현재 만들어진 줄 개수
    var check_array=ArrayList<Boolean>()
    val user = Firebase.auth.currentUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remove_ingredient)
        var pref = getSharedPreferences("pref", Context.MODE_PRIVATE)
        ingredient = pref.getInt("ingredient",0)            //ingredient값 가져오기
        println(ingredient)
        val db = Firebase.firestore
        val f = db.collection(user?.uid.toString())
        f.addSnapshotListener { querySnapshot: QuerySnapshot?, _: FirebaseFirestoreException? ->
            if(icount<ingredient){      //삭제후 추가생성방지
                if (querySnapshot != null) {
                    // 반복문으로 모든 음식에 접근합니다.(document가 음식)
                    // UserFridge.kt에 각 필드 설명 적혀있습니다!
                    for (document in querySnapshot.documents) {
                        val obj = document.toObject<UserFridge>()
                        if (obj != null) {
                            if(icount==lcount*3){
                                AddTablerow()
                                lcount+=1
                            }
                            AddLinear()
                            AddIngredient(obj.name.toString(),obj.category.toString(),obj.expiryDate.toString())
                            icount+=1
                        }
                    }
                }
            }
        }
        select_btn.setOnClickListener(View.OnClickListener {            //취소하면 그냥 돌아감
            onStop()
        })
        select_remove_btn.setOnClickListener(View.OnClickListener {     //삭제 버튼

            runBlocking {
                val a=launch {
                    var deleteindex=ArrayList<Int>()
                    for(i in icount-1 downTo 0){
                        if(check_array[i]==true){
                            deleteindex.add(i+1)
                        }
                    }
                    delete(deleteindex)
                }
                println("aaa")
                a.join()
                println("삭제끝")
                //onStop()
            }

        })

    }
    suspend fun delete(removelist:ArrayList<Int>){
        runBlocking {
            val db = Firebase.firestore
            val f = db.collection(user?.uid.toString())
            var sorti=1

            f.orderBy("id").get().addOnSuccessListener { result ->
                for (food in result) {
                    val obj = food.toObject<UserFridge>()
                    if (obj != null && !(removelist.contains(obj.id))) {
                        print("objid:")
                        println(obj.id)
                        println(obj.name)
                        print("sorti")
                        println(sorti)
                        f.document(sorti.toString()).set(obj)
                        f.document(sorti.toString()).update("id",sorti)
                        sorti += 1
                    }
                }
                for(i in 0..removelist.size-1) {
                    println(i)
                    println(ingredient)
                    f.document(ingredient.toString()).delete()
                    ingredient -= 1
                }
                println("완료")
            }
        }
    }

    override fun onBackPressed() {
        onStop()
    }

    override fun onStop() {
        super.onStop()
        var pref = getSharedPreferences("pref",Context.MODE_PRIVATE)   //앱종료되도 값 유지
        pref.edit().putInt("ingredient",ingredient).apply()         //ingredient값 저장
        startActivity(Intent(this, MyFridge::class.java))
        finish()
    }
    fun AddTablerow(){        //한 줄 추가
        val LL= TableRow(this)
        LL.setBackgroundColor(Color.parseColor("#330669FF"))
        LL.id=(60000+lcount)                    //id부여
        table2.addView(LL, TableRow.LayoutParams.MATCH_PARENT, changeDP(110))   //110을 changeDP로 dp값으로 변경해서 입력
    }
    @SuppressLint("ResourceType")       //이건 뭔지 잘 모르겠다, id=(int값)할 때 뜨는 에러때문에 추가
    fun AddLinear(){        //한 줄 추가
        val LL1= LinearLayout(this)      //한 음식재료를 위한 layout공간
        LL1.id=(61000+icount)                   //id부여
        LL1.gravity= Gravity.CENTER             //gravity를 center로
        LL1.orientation= LinearLayout.HORIZONTAL
        findViewById<TableRow>(60000+lcount-1).addView(LL1)     //60000+lcount-1의 tablerow에 layout추가
        (findViewById<LinearLayout>(61000+icount).layoutParams as LinearLayout.LayoutParams).setMargins(changeDP(20),changeDP(5),0,0)
        //현재 만든 layout에 마진값 추가

        val LL2= LinearLayout(this)     //한 음식재료를 보여주는 layout공간에서 재료와 체크박스를 나누기 위한 공간(재료부분)
        LL2.id=(62000+icount)
        LL2.gravity= Gravity.CENTER
        LL2.orientation= LinearLayout.VERTICAL
        findViewById<LinearLayout>(61000+icount).addView(LL2,
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)

        val LL3= LinearLayout(this)     //한 음식재료를 보여주는 layout공간에서 재료와 체크박스를 나누기 위한 공간(체크박스부분)
        LL3.id=(63000+icount)
        findViewById<LinearLayout>(61000+icount).addView(LL3,
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT)

    }
    @SuppressLint("ResourceType")   //이건 뭔지 잘 모르겠다, id=(int값)할 때 뜨는 에러때문에 추가
    fun AddIngredient(a : String,cate:String, b : String){    //재료 추가
        val img= ImageView(this)            //이미지
        img.scaleType= ImageView.ScaleType.CENTER_CROP      //scaleType을 center_crop으로
        img.id=(64000+icount)
        img.layoutParams = LinearLayout.LayoutParams(
            changeDP(80),
            changeDP(60)
        )
        findViewById<LinearLayout>(62000+icount).addView(img)
        setimg(img,cate)               //이미지넣기
        val textname = TextView(this)           //재료이름
        textname.text = a
        textname.gravity= Gravity.CENTER
        textname.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            changeDP(30)
        )
        textname.id=(65000+icount)
        findViewById<LinearLayout>(62000+icount).addView(textname)

        val textdate = TextView(this)       //유통기한
        val dday=calculateDday(b)
        if(dday<0)
            textdate.text = "D+"+(dday*-1).toString()
        else
            textdate.text = "D-"+dday.toString()
        textdate.gravity= Gravity.CENTER
        textdate.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            changeDP(20)
        )
        textdate.setBackgroundColor(Color.LTGRAY)
        textdate.id=(66000+icount)
        findViewById<LinearLayout>(62000+icount).addView(textdate)

        val checkbox = CheckBox(this)       //체크박스
        checkbox.scaleX= 1.5F                      //스케일조정
        checkbox.scaleY= 1.5F                       //스케일조정
        checkbox.layoutParams = LinearLayout.LayoutParams(
            changeDP(30), changeDP(30)
        )
        checkbox.id=(67000+icount)
        check_array.add(false)                  //기본은 체크가 안되있는 상태
        findViewById<LinearLayout>(63000+icount).addView(checkbox)
        checkbox.setOnCheckedChangeListener { buttonView, a ->  //a는 checkbox.isChecked로 체크되있는지 안되있는지 true,false로 알려줌
            check_array[checkbox.id-67000]=a   //인덱스 0부터 체크됐는지 저장
        }

    }
    private fun changeDP(value : Int) : Int{        //int값을 dp값으로 변환
        var displayMetrics = resources.displayMetrics
        var dp = Math.round(value * displayMetrics.density)
        return dp
    }
    fun calculateDday(date : String): Long {   //D-day계산
        //val today = Calendar.getInstance()
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
    fun setimg(img : ImageView,cate:String){
        when(cate){
            "과일"->img.setImageResource(R.drawable.ingredient2)
            "견과"->img.setImageResource(R.drawable.ingredient1)
            "기타"->img.setImageResource(R.drawable.ingredient3)
            "김치/반찬"->img.setImageResource(R.drawable.ingredient4)
            "쌀/곡물"->img.setImageResource(R.drawable.ingredient5)
            "유제품"->img.setImageResource(R.drawable.ingredient6)
            "음료"->img.setImageResource(R.drawable.ingredient7)
            "정육/계란"->img.setImageResource(R.drawable.ingredient8)
            "채소"->img.setImageResource(R.drawable.ingredient9)
        }
    }
}
