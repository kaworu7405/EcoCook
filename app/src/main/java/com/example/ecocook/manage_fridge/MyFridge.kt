package com.example.ecocook.manage_fridge

import android.annotation.SuppressLint
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
    var ingredient = 0

    var backKeyPressedTime:Long=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_fridge)
        println("id")
        println(tr1.id)
        val add_btn = findViewById<Button>(R.id.ingredient_add) as Button
        add_btn.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, AddIngredient::class.java))
            finish()
            /*
            AddLinear()
            AddIngredient()
            ingredient+=1
            */
        })
    }
    override fun onBackPressed() {
        /*if(System.currentTimeMillis()-backKeyPressedTime<2000){
            finishAffinity()
        }
        backKeyPressedTime=System.currentTimeMillis()*/
        startActivity(Intent(this, MainMenuActivity::class.java))
        finish()
    }
    @SuppressLint("ResourceType")       //이건 뭔지 잘 모르겠다, id=(int값)할 때 뜨는 에러때문에 추가
    fun AddLinear(){
        //val idArray = resources.obtainTypedArray(R.array.id_group_1)      //res에 있는 아이디사용
        //val id = idArray.getResourceId(ingredient,-1)
        val LL= LinearLayout(this)
        LL.setBackgroundColor(Color.GREEN)
        //LL.id=id                      //rse에 있는 아이디사용
        LL.id=(10000+ingredient)
        LL.orientation=LinearLayout.VERTICAL
        tr1.addView(LL,LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        //(findViewById<LinearLayout>(10000+ingredient).layoutParams as LinearLayout.LayoutParams).weight=1f    weight설정
        println(tr1.id)
        println(tr2.id)
        println(tr3.id)

    }
    @SuppressLint("ResourceType")   //이건 뭔지 잘 모르겠다, id=(int값)할 때 뜨는 에러때문에 추가
    fun AddIngredient(){
        //val idArray = resources.obtainTypedArray(R.array.id_group_1)  //rse에 있는 아이디사용
        //val id = idArray.getResourceId(ingredient,-1)

        val img= ImageView(this)
        img.setImageResource(R.drawable.testaa)
        img.scaleType=ImageView.ScaleType.CENTER_CROP
        img.id=(20000+ingredient)
        img.layoutParams = LinearLayout.LayoutParams(
            changeDP(100),
            changeDP(60)
        )
        //findViewById<LinearLayout>(id).addView(img)
        findViewById<LinearLayout>(10000+ingredient).addView(img)

        val textname = TextView(this)
        textname.text = "감자"
        textname.gravity=Gravity.CENTER
        textname.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            changeDP(30)
        )
        textname.id=(30000+ingredient)
        textname.setBackgroundColor(Color.GRAY)
        //findViewById<LinearLayout>(id).addView(textname)
        findViewById<LinearLayout>(10000+ingredient).addView(textname)

        val textdate = TextView(this)
        textdate.text = "D-3"
        textdate.gravity=Gravity.CENTER
        textdate.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            changeDP(20)
        )
        textdate.setBackgroundColor(Color.WHITE)
        textdate.id=(40000+ingredient)
        //findViewById<LinearLayout>(id).addView(textdate)
        findViewById<LinearLayout>(10000+ingredient).addView(textdate)
    }
    private fun changeDP(value : Int) : Int{        //int값을 dp값으로 변환
        var displayMetrics = resources.displayMetrics
        var dp = Math.round(value * displayMetrics.density)
        return dp
    }
}