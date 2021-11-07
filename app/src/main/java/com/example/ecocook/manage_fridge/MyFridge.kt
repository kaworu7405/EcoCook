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
import com.bumptech.glide.Glide
import com.example.ecocook.MainMenuActivity
import com.example.ecocook.R
import com.example.ecocook.UserFridge
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_add_ingredient.*
import kotlinx.android.synthetic.main.activity_my_fridge.*
import kotlinx.android.synthetic.main.check_ingredient.*
import java.text.SimpleDateFormat
import java.util.*

class MyFridge : AppCompatActivity() {
    val sortlist0= listOf<String>("입력순","재료명 순","유통기한 임박 순")
    val sortlist1= listOf<String>("재료명 순","유통기한 임박 순","입력순")
    val sortlist2= listOf<String>("유통기한 임박 순","입력순","재료명 순")
    val sortlist= listOf<List<String>>(sortlist0,sortlist1,sortlist2)
    var sortindex= 0
    val objlist= ArrayList<UserFridge>()
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
            startActivity(Intent(this, RecipesCheck::class.java))
            finish()
        })
        icon2list.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, DetailList::class.java))
            finish()
        })
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
                        objlist.add(obj)    //정렬
                    }
                }
                if(sortindex==0){
                    for(obj in objlist){
                        if(icount==lcount*4){
                            AddTablerow()
                            lcount+=1
                        }
                        AddLinear()
                        AddIngredient(obj.name.toString(),obj.category.toString(),obj.expiryDate.toString())
                        icount+=1
                    }
                }
                else if(sortindex==1){
                    for(obj in objlist.sortedWith(compareBy ({ it.name },{ it.expiryDate}))){
                        if(icount==lcount*4){
                            AddTablerow()
                            lcount+=1
                        }
                        AddLinear()
                        AddIngredient(obj.name.toString(),obj.category.toString(),obj.expiryDate.toString())
                        icount+=1
                    }
                }
                else if(sortindex==2){
                    for(obj in objlist.sortedWith(compareBy ({ it.expiryDate },{ it.name}))){
                        if(icount==lcount*4){
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

    fun Restart() {
        startActivity(Intent(this, MyFridge::class.java))
        finish()
    }
    override fun onStop() {
        super.onStop()
        var pref = getSharedPreferences("pref",Context.MODE_PRIVATE)
        pref.edit().putInt("ingredient",icount).apply()             //화면종료? 이동시? ingredient저장
        pref.edit().putInt("sortindex",sortindex).apply()
    }
    override fun onBackPressed() {
        startActivity(Intent(this, MainMenuActivity::class.java))
        finish()
    }
    fun AddTablerow(){        //한 줄 추가
        val LL= TableRow(this)
        LL.id=(50000+lcount)
        table1.addView(LL,TableRow.LayoutParams.MATCH_PARENT, changeDP(110))


    }
    @SuppressLint("ResourceType")       //이건 뭔지 잘 모르겠다, id=(int값)할 때 뜨는 에러때문에 추가
    fun AddLinear(){        //한 줄 추가
        //val idArray = resources.obtainTypedArray(R.array.id_group_1)      //res에 있는 id.xml 아이디사용  일단은 다른 방법으로 해서 안씀
        //val id = idArray.getResourceId(ingredient,-1)                     //위와 동일
        val LL= LinearLayout(this)
        //LL.id=id                      //rse에 있는 아이디사용
        LL.id=(10000+icount)
        LL.gravity=Gravity.CENTER
        LL.orientation=LinearLayout.VERTICAL
        LL.setOnClickListener {
            detailIngredient(LL.id+1-10000)
        }
        findViewById<TableRow>(50000+lcount-1).addView(LL,LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)


    }
    @SuppressLint("ResourceType")   //이건 뭔지 잘 모르겠다, id=(int값)할 때 뜨는 에러때문에 추가
    fun AddIngredient(a : String, cate:String, b : String){    //재료 추가
        val img= ImageView(this)            //이미지
        img.scaleType=ImageView.ScaleType.CENTER_CROP       //scaletype설정
        img.id=(20000+icount)
        img.layoutParams = LinearLayout.LayoutParams(
            changeDP(100),
            changeDP(60)
        )
        setimg(img,cate)
        findViewById<LinearLayout>(10000+icount).addView(img)
        //getFireBaseProfileImage(cate,img)               //이미지넣기
        val textname = TextView(this)
        textname.text = a                    //재료이름
        textname.gravity=Gravity.CENTER         //gravity설정
        textname.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            changeDP(30)
        )
        textname.id=(30000+icount)
        findViewById<LinearLayout>(10000+icount).addView(textname)

        val textdate = TextView(this)           //유통기한 설정
        textdate.text = "D-"+calculateDday(b).toString()
        textdate.gravity=Gravity.CENTER                 //gravity설정
        textdate.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            changeDP(20)
        )
        textdate.setBackgroundColor(Color.LTGRAY)
        textdate.id=(40000+icount)
        findViewById<LinearLayout>(10000+icount).addView(textdate)
    }
    fun setspinner(){
        fridSpinner.adapter=ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,sortlist[sortindex])
        fridSpinner.setSelection(0,false)
        fridSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
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
    fun detailIngredient(i : Int){
        val db = Firebase.firestore
        val f = db.collection(user?.uid.toString())
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        val v1 = layoutInflater.inflate(R.layout.check_ingredient, null)
        val check_i1 = v1.findViewById<ImageView>(R.id.check_i1)
        val check_i2 = v1.findViewById<TextView>(R.id.check_i2)
        val check_i3 = v1.findViewById<TextView>(R.id.check_i3)
        val check_i4 = v1.findViewById<TextView>(R.id.check_i4)
        val check_i5 = v1.findViewById<TextView>(R.id.check_i5)
        f.orderBy("id").addSnapshotListener { querySnapshot: QuerySnapshot?, _: FirebaseFirestoreException? ->
            if (querySnapshot != null) {
                // 반복문으로 모든 음식에 접근합니다.(document가 음식)
                for (document in querySnapshot.documents) {
                    val obj = document.toObject<UserFridge>()
                    if (obj != null && obj.id==i) {
                        setimg(check_i1,obj.category.toString())
                        check_i2.text=obj.name.toString()
                        check_i3.text="구매일자 "+obj.buyDate.toString()
                        check_i4.text="유통기한 "+obj.expiryDate.toString()
                        check_i5.text="잔여수량 "+obj.num.toString()
                    }
                }
            }
        }
        builder.setView(v1)

        builder.setPositiveButton(
            "확인",
            { dialogInterface: DialogInterface?, i: Int ->
                //원하는 명령어
            })

        builder.show()
    }

}