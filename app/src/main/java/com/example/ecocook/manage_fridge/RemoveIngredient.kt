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
    var ingredient = 0      //총 재료
    var icount = 0      //현재 만들어진 재료
    var lcount = 0      //현재 만들어진 줄 개수
    var check_array=ArrayList<Boolean>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remove_ingredient)
        select_btn.setOnClickListener(View.OnClickListener {            //취소하면 그냥 돌아감
            startActivity(Intent(this, MyFridge::class.java))
            finish()
        })
        select_remove_btn.setOnClickListener(View.OnClickListener {     //삭제 버튼
            for(i in 0..icount-1){
                if(check_array[i]==true)
                    ingredient-=1
            }
            var pref = getSharedPreferences("pref",Context.MODE_PRIVATE)   //앱종료되도 값 유지
            pref.edit().putInt("ingredient",ingredient).apply()         //ingredient값 저장
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
        ingredient = pref.getInt("ingredient",0)            //ingredient값 가져오기
        for(i in 0..ingredient-1){      //ingredient값만큼 재료가 있므르로 재료들 보여주기 0부터로 해서 ingredient-1
            if(icount==lcount*3){       //한 줄에 3개씩 보여주기 위함
                AddTablerow()           //tablerow 생성
                lcount+=1               //어디까지 생성했는지 체크
            }
            AddLinear()                 //linearlayout생성
            AddIngredient()             //재료그림, 이름, 유통기한, 체크박스 생성
            icount+=1                   //어디까지 생성했는지 체크
        }
    }
    fun AddTablerow(){        //한 줄 추가
        val LL= TableRow(this)
        LL.setBackgroundColor(Color.GREEN)      //임시 배경색
        LL.id=(60000+lcount)                    //id부여
        table2.addView(LL, TableRow.LayoutParams.MATCH_PARENT, changeDP(110))   //110을 changeDP로 dp값으로 변경해서 입력
    }
    @SuppressLint("ResourceType")       //이건 뭔지 잘 모르겠다, id=(int값)할 때 뜨는 에러때문에 추가
    fun AddLinear(){        //한 줄 추가
        val LL1= LinearLayout(this)      //한 음식재료를 위한 layout공간
        LL1.setBackgroundColor(Color.GREEN)     //임시 배경색
        LL1.id=(61000+icount)                   //id부여
        LL1.gravity= Gravity.CENTER             //gravity를 center로
        LL1.orientation= LinearLayout.HORIZONTAL
        findViewById<TableRow>(60000+lcount-1).addView(LL1)     //60000+lcount-1의 tablerow에 layout추가
        (findViewById<LinearLayout>(61000+icount).layoutParams as LinearLayout.LayoutParams).setMargins(changeDP(20),changeDP(5),0,0)
        //현재 만든 layout에 마진값 추가

        val LL2= LinearLayout(this)     //한 음식재료를 보여주는 layout공간에서 재료와 체크박스를 나누기 위한 공간(재료부분)
        LL2.setBackgroundColor(Color.GREEN)
        LL2.id=(62000+icount)
        LL2.gravity= Gravity.CENTER
        LL2.orientation= LinearLayout.VERTICAL
        findViewById<LinearLayout>(61000+icount).addView(LL2,
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)

        val LL3= LinearLayout(this)     //한 음식재료를 보여주는 layout공간에서 재료와 체크박스를 나누기 위한 공간(체크박스부분)
        LL3.setBackgroundColor(Color.GREEN)
        LL3.id=(63000+icount)
        LL3.gravity= Gravity.CENTER
        findViewById<LinearLayout>(61000+icount).addView(LL3,
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT)

    }
    @SuppressLint("ResourceType")   //이건 뭔지 잘 모르겠다, id=(int값)할 때 뜨는 에러때문에 추가
    fun AddIngredient(){    //재료 추가
        val img= ImageView(this)            //이미지
        img.setImageResource(R.drawable.testaa)
        img.scaleType= ImageView.ScaleType.CENTER_CROP      //scaleType을 center_crop으로
        img.id=(64000+icount)
        img.layoutParams = LinearLayout.LayoutParams(
            changeDP(80),
            changeDP(60)
        )
        findViewById<LinearLayout>(62000+icount).addView(img)

        val textname = TextView(this)           //재료이름
        textname.text = "감자"
        textname.gravity= Gravity.CENTER
        textname.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            changeDP(30)
        )
        textname.id=(65000+icount)
        textname.setBackgroundColor(Color.GRAY)
        findViewById<LinearLayout>(62000+icount).addView(textname)

        val textdate = TextView(this)       //유통기한
        textdate.text = icount.toString()
        textdate.gravity= Gravity.CENTER
        textdate.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            changeDP(20)
        )
        textdate.setBackgroundColor(Color.WHITE)
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

}