package com.example.ecocook.manage_fridge

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ecocook.MainMenuActivity
import com.example.ecocook.R

class MyFridge : AppCompatActivity() {
    var backKeyPressedTime:Long=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_fridge)
    }
    override fun onBackPressed() {                          //이거 뒤로가기 누르는거 연속으로 눌러야 꺼지거나 그런건가?
        /*if(System.currentTimeMillis()-backKeyPressedTime<2000){
            finishAffinity()
        }
        backKeyPressedTime=System.currentTimeMillis()*/
        startActivity(Intent(this, MainMenuActivity::class.java))
        finish()
    }
}