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
import kotlinx.android.synthetic.main.activity_my_fridge.*

class MyFridge : AppCompatActivity() {
    var ingredient = 0      //나의 총 재료 개수
    var icount=0            //현재 만들어진 재료
    var lcount=0            //현재 만들어진 줄개수
    var backKeyPressedTime:Long=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_fridge)
        add_btn.setOnClickListener(View.OnClickListener {
            //startActivity(Intent(this, AddIngredient::class.java))    //원래는 여기로 넘어가야댐
            //finish()
            //임시로 추가하는거 보기 위해 해놈
            if(icount==lcount*4){   //한 줄에 4개씩 보여주기 위함 4개가 이미 있으면 tablerow추가 생성
                AddTablerow()
                lcount+=1
            }
            AddLinear()
            AddIngredient()
            icount+=1
        })
        remove_btn.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, RemoveIngredient::class.java))
            finish()
        })
    }

    override fun onStart() {
        super.onStart()
        var pref = getSharedPreferences("pref",Context.MODE_PRIVATE)
        ingredient = pref.getInt("ingredient",0)    //ingredient가져오기 값없으면 0을 가져옴
        for(i in 0..ingredient-1){      //현재 있는 재료들 모두 생성
            if(icount==lcount*4){
                AddTablerow()
                lcount+=1
            }
            AddLinear()
            AddIngredient()
            icount+=1
        }
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
        findViewById<TableRow>(50000+lcount-1).addView(LL,LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)


    }
    @SuppressLint("ResourceType")   //이건 뭔지 잘 모르겠다, id=(int값)할 때 뜨는 에러때문에 추가
    fun AddIngredient(){    //재료 추가

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
        textname.text = "감자"                    //재료이름
        textname.gravity=Gravity.CENTER         //gravity설정
        textname.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            changeDP(30)
        )
        textname.id=(30000+icount)
        textname.setBackgroundColor(Color.GRAY)
        findViewById<LinearLayout>(10000+icount).addView(textname)

        val textdate = TextView(this)           //유통기한 설정
        textdate.text = "D-3"
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


}