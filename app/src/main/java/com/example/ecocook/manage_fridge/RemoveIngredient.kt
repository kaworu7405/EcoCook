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
import com.example.ecocook.R
import kotlinx.android.synthetic.main.activity_my_fridge.*
import kotlinx.android.synthetic.main.activity_remove_ingredient.*

class RemoveIngredient : AppCompatActivity() {
    var ingredient = 0
    var icount = 0
    var lcount = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remove_ingredient)
        select_btn.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, MyFridge::class.java))
            finish()
        })
        select_remove_btn.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, MyFridge::class.java))
            finish()
        })
    }
    override fun onBackPressed() {
        startActivity(Intent(this, MyFridge::class.java))
        finish()
    }
    override fun onStart() {
        super.onStart()
        var pref = getSharedPreferences("pref", Context.MODE_PRIVATE)
        ingredient = pref.getInt("ingredient",0)
        for(i in 0..ingredient-1){
            if(icount==lcount*4){
                AddTablerow()
                lcount+=1
            }
            AddLinear()
            AddIngredient()
            icount+=1
        }
    }
    fun AddTablerow(){        //한 줄 추가
        val LL= TableRow(this)
        LL.setBackgroundColor(Color.GREEN)
        LL.id=(60000+lcount)
        table2.addView(LL, TableRow.LayoutParams.MATCH_PARENT, changeDP(110))
    }
    @SuppressLint("ResourceType")       //이건 뭔지 잘 모르겠다, id=(int값)할 때 뜨는 에러때문에 추가
    fun AddLinear(){        //한 줄 추가
        val LL1= LinearLayout(this)
        LL1.setBackgroundColor(Color.GREEN)
        LL1.id=(61000+icount)
        LL1.gravity= Gravity.CENTER
        LL1.orientation= LinearLayout.HORIZONTAL
        findViewById<TableRow>(60000+lcount-1).addView(LL1)
        (findViewById<LinearLayout>(61000+icount).layoutParams as LinearLayout.LayoutParams).setMargins(changeDP(20),changeDP(5),0,0)

        val LL2= LinearLayout(this)
        LL2.setBackgroundColor(Color.GREEN)
        LL2.id=(62000+icount)
        LL2.gravity= Gravity.CENTER
        LL2.orientation= LinearLayout.VERTICAL
        findViewById<LinearLayout>(61000+icount).addView(LL2,
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        val LL3= LinearLayout(this)
        LL3.setBackgroundColor(Color.GREEN)
        LL3.id=(63000+icount)
        LL3.gravity= Gravity.CENTER
        findViewById<LinearLayout>(61000+icount).addView(LL3,
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT)

    }
    @SuppressLint("ResourceType")   //이건 뭔지 잘 모르겠다, id=(int값)할 때 뜨는 에러때문에 추가
    fun AddIngredient(){    //재료 추가
        val img= ImageView(this)
        img.setImageResource(R.drawable.testaa)
        img.scaleType= ImageView.ScaleType.CENTER_CROP
        img.id=(64000+icount)
        img.layoutParams = LinearLayout.LayoutParams(
            changeDP(80),
            changeDP(60)
        )
        findViewById<LinearLayout>(62000+icount).addView(img)

        val textname = TextView(this)
        textname.text = "감자"
        textname.gravity= Gravity.CENTER
        textname.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            changeDP(30)
        )
        textname.id=(65000+icount)
        textname.setBackgroundColor(Color.GRAY)
        findViewById<LinearLayout>(62000+icount).addView(textname)

        val textdate = TextView(this)
        textdate.text = icount.toString()
        textdate.gravity= Gravity.CENTER
        textdate.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            changeDP(20)
        )
        textdate.setBackgroundColor(Color.WHITE)
        textdate.id=(66000+icount)
        findViewById<LinearLayout>(62000+icount).addView(textdate)

        val checkbox = CheckBox(this)
        checkbox.scaleX= 1.5F
        checkbox.scaleY= 1.5F
        checkbox.layoutParams = LinearLayout.LayoutParams(
            changeDP(30), changeDP(30)
        )
        checkbox.id=(67000+icount)
        findViewById<LinearLayout>(63000+icount).addView(checkbox)
        checkbox.setOnCheckedChangeListener { buttonView, a ->
            println("checkbox")
            print(checkbox.id)
            println(a)
        }
    }
    private fun changeDP(value : Int) : Int{        //int값을 dp값으로 변환
        var displayMetrics = resources.displayMetrics
        var dp = Math.round(value * displayMetrics.density)
        return dp
    }

}