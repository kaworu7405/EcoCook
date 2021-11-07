package com.example.ecocook.manage_fridge

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.ecocook.MainMenuActivity
import com.example.ecocook.R
import com.example.ecocook.UserFridge
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_my_fridge.*
import kotlinx.android.synthetic.main.check_ingredient.*
import java.text.SimpleDateFormat
import java.util.*

class MyFridge : AppCompatActivity() {
    var ingredient = 0      //나의 총 재료 개수
    var icount=0            //현재 만들어진 재료
    var lcount=0            //현재 만들어진 줄개수
    val user = Firebase.auth.currentUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_fridge)
        add_btn.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, AddIngredient::class.java))    //원래는 여기로 넘어가야댐
            finish()
        })
        remove_btn.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, RemoveIngredient::class.java))
            finish()
        })

        check_btn.setOnClickListener(View.OnClickListener {
            val db = Firebase.firestore
            val f = db.collection(user?.uid.toString())
            f.addSnapshotListener { querySnapshot: QuerySnapshot?, _: FirebaseFirestoreException? ->
                if (querySnapshot != null) {
                    // 반복문으로 모든 음식에 접근합니다.(document가 음식)
                    // UserFridge.kt에 각 필드 설명 적혀있습니다!
                    for (document in querySnapshot.documents) {
                        val obj = document.toObject<UserFridge>()
                        System.out.println(obj)
                        if (obj != null) {
                            Log.d("TAG", "이 음식은 " + obj.category.toString())
                            Log.d("TAG", obj.name.toString() + "입니다")
                            Log.d("TAG", obj.expiryDate.toString() + "까지 먹어야돼요!!")
                        }
                    }
                }
            }
        })
    }

    override fun onStart() {
        super.onStart()
        var pref = getSharedPreferences("pref",Context.MODE_PRIVATE)
        ingredient = pref.getInt("ingredient",0)    //ingredient가져오기 값없으면 0을 가져옴
        //
        val db = Firebase.firestore
        val f = db.collection(user?.uid.toString())
        f.addSnapshotListener { querySnapshot: QuerySnapshot?, _: FirebaseFirestoreException? ->
            if (querySnapshot != null) {
                // 반복문으로 모든 음식에 접근합니다.(document가 음식)
                // UserFridge.kt에 각 필드 설명 적혀있습니다!
                for (document in querySnapshot.documents) {
                    val obj = document.toObject<UserFridge>()
                    System.out.println(obj)
                    if (obj != null) {
                        if(icount==lcount*4){
                            AddTablerow()
                            lcount+=1
                        }
                        AddLinear()
                        AddIngredient(obj.name.toString(),obj.expiryDate.toString())
                        icount+=1
                        Log.d("TAG", "이 음식은 " + obj.category.toString())
                        Log.d("TAG", obj.name.toString() + "입니다")
                        Log.d("TAG", obj.expiryDate.toString() + "까지 먹어야돼요!!")
                    }
                }
            }
        }
        //
    }

    override fun onStop() {
        super.onStop()
        var pref = getSharedPreferences("pref",Context.MODE_PRIVATE)
        pref.edit().putInt("ingredient",icount).apply()             //화면종료? 이동시? ingredient저장
    }
    override fun onBackPressed() {
        startActivity(Intent(this, MainMenuActivity::class.java))
        finish()
    }
    fun AddTablerow(){        //한 줄 추가
        val LL= TableRow(this)
        LL.setBackgroundColor(Color.GREEN)
        LL.id=(50000+lcount)
        table1.addView(LL,TableRow.LayoutParams.MATCH_PARENT, changeDP(110))


    }
    @SuppressLint("ResourceType")       //이건 뭔지 잘 모르겠다, id=(int값)할 때 뜨는 에러때문에 추가
    fun AddLinear(){        //한 줄 추가
        //val idArray = resources.obtainTypedArray(R.array.id_group_1)      //res에 있는 id.xml 아이디사용  일단은 다른 방법으로 해서 안씀
        //val id = idArray.getResourceId(ingredient,-1)                     //위와 동일
        val LL= LinearLayout(this)
        LL.setBackgroundColor(Color.GREEN)
        //LL.id=id                      //rse에 있는 아이디사용
        LL.id=(10000+icount)
        LL.gravity=Gravity.CENTER
        LL.orientation=LinearLayout.VERTICAL
        LL.setOnClickListener {
            detailIngredient(icount)
        }
        findViewById<TableRow>(50000+lcount-1).addView(LL,LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)


    }
    @SuppressLint("ResourceType")   //이건 뭔지 잘 모르겠다, id=(int값)할 때 뜨는 에러때문에 추가
    fun AddIngredient(a : String, b : String){    //재료 추가

        val img= ImageView(this)            //이미지
        img.setImageResource(R.drawable.testaa)     //이미지설정
        img.scaleType=ImageView.ScaleType.CENTER_CROP       //scaletype설정
        img.id=(20000+icount)
        img.layoutParams = LinearLayout.LayoutParams(
            changeDP(100),
            changeDP(60)
        )
        findViewById<LinearLayout>(10000+icount).addView(img)

        val textname = TextView(this)
        textname.text = a                    //재료이름
        textname.gravity=Gravity.CENTER         //gravity설정
        textname.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            changeDP(30)
        )
        textname.id=(30000+icount)
        textname.setBackgroundColor(Color.GRAY)
        findViewById<LinearLayout>(10000+icount).addView(textname)

        val textdate = TextView(this)           //유통기한 설정
        textdate.text = calculateDday(b).toString()
        textdate.gravity=Gravity.CENTER                 //gravity설정
        textdate.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            changeDP(20)
        )
        textdate.setBackgroundColor(Color.WHITE)
        textdate.id=(40000+icount)
        findViewById<LinearLayout>(10000+icount).addView(textdate)
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
    fun detailIngredient(i : Int){
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        val v1 = layoutInflater.inflate(R.layout.check_ingredient, null)
        val check_i1 = v1.findViewById<ImageView>(R.id.check_i1)
        val check_i2 = v1.findViewById<TextView>(R.id.check_i2)
        check_i2.text="감자"
        val check_i3 = v1.findViewById<TextView>(R.id.check_i3)
        check_i3.text="구매일자 "
        val check_i4 = v1.findViewById<TextView>(R.id.check_i4)
        check_i4.text="유통기한 "
        val check_i5 = v1.findViewById<TextView>(R.id.check_i5)
        check_i5.text="잔여수량 x개"
        builder.setView(v1)


        builder.setPositiveButton(
            "확인",
            { dialogInterface: DialogInterface?, i: Int ->
                //원하는 명령어
            })

        builder.show()
    }

}