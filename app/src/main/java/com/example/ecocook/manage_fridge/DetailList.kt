package com.example.ecocook.manage_fridge

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.*
import com.example.ecocook.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_detail_list.*
import kotlinx.android.synthetic.main.activity_my_fridge.*
import kotlinx.android.synthetic.main.activity_my_fridge.table1
import java.text.SimpleDateFormat
import java.util.*

class DetailList : AppCompatActivity() {
    var ingredient = 0      //나의 총 재료 개수
    var icount=0            //현재 만들어진 재료
    var lcount=0            //현재 만들어진 줄개수
    val user = Firebase.auth.currentUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_list)
    }
    fun AddTablerow(){        //한 줄 추가
        val LL= TableRow(this)
        LL.id=(70000+lcount)
        detailtable.addView(LL, TableRow.LayoutParams.MATCH_PARENT, changeDP(110))


    }
    @SuppressLint("ResourceType")       //이건 뭔지 잘 모르겠다, id=(int값)할 때 뜨는 에러때문에 추가
    fun AddLinear(){        //한 줄 추가
        //val idArray = resources.obtainTypedArray(R.array.id_group_1)      //res에 있는 id.xml 아이디사용  일단은 다른 방법으로 해서 안씀
        //val id = idArray.getResourceId(ingredient,-1)                     //위와 동일
        val LL= LinearLayout(this)
        LL.setBackgroundColor(Color.GREEN)
        //LL.id=id                      //rse에 있는 아이디사용
        LL.id=(10000+icount)
        LL.gravity= Gravity.CENTER
        LL.orientation= LinearLayout.HORIZONTAL
        findViewById<TableRow>(70000+lcount-1).addView(LL,
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)


    }
    @SuppressLint("ResourceType")   //이건 뭔지 잘 모르겠다, id=(int값)할 때 뜨는 에러때문에 추가
    fun AddIngredient(a : String, b : String){    //재료 추가

        val textname = TextView(this)
        textname.text = "감자"                    //재료이름
        textname.gravity= Gravity.CENTER         //gravity설정
        textname.layoutParams = LinearLayout.LayoutParams(
            changeDP(100), changeDP(50)
        )
        textname.id=(20000+icount)
        findViewById<LinearLayout>(10000+icount).addView(textname)

        val textcategory = TextView(this)
        textcategory.text = "채소"                    //카테고리이름
        textcategory.gravity= Gravity.CENTER         //gravity설정
        textcategory.layoutParams = LinearLayout.LayoutParams(
            changeDP(80), changeDP(50)
        )
        textcategory.id=(30000+icount)
        findViewById<LinearLayout>(10000+icount).addView(textcategory)

        val textdate = TextView(this)           //유통기한 설정
        textdate.text = calculateDday(b).toString()
        textdate.gravity= Gravity.CENTER                 //gravity설정
        textdate.layoutParams = LinearLayout.LayoutParams(
            changeDP(80), changeDP(50)
        )
        textdate.id=(40000+icount)
        findViewById<LinearLayout>(10000+icount).addView(textdate)

        val textnum = TextView(this)           //개수 설정
        textnum.text = calculateDday(b).toString()
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
}