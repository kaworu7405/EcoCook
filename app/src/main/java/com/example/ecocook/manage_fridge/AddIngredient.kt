package com.example.ecocook.manage_fridge

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.ecocook.R
import kotlinx.android.synthetic.main.activity_add_ingredient.*

class AddIngredient : AppCompatActivity() {
    val ingredient_list= listOf<String>("과일","쌀/곡물/견과","정육/계란","유제품","김치/반찬","생수/음료/커피/차","기타")
    val y=Array(140,{i->i+1900})
    val m=Array(12,{i->i+1})
    val d=Array(31,{i->i+1})
    val amountnum=Array(130,{i->i+1})
    var add_data = AddIngredientData()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_ingredient)
        spinner()
        ingredient_save.setOnClickListener{
            ingredientsave()
        }
    }
    override fun onBackPressed() {
        startActivity(Intent(this, MyFridge::class.java))
        finish()
    }
    fun spinner(){
        category_ingredient.adapter=ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,ingredient_list)    //식재료명
        category_ingredient.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                findViewById<TextView>(R.id.textView5).text= ingredient_list[position]
                add_data.kind=ingredient_list[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        purchase_year.adapter=ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,y)    //구입연도
        purchase_year.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                add_data.py= y[position].toString()
                textView6.text=add_data.py+add_data.pm+add_data.pd
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        purchase_month.adapter=ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,m)   //구입월
        purchase_month.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                add_data.pm= m[position].toString()
                textView6.text=add_data.py+add_data.pm+add_data.pd
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        purchase_day.adapter=ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,d)     //구입일
        purchase_day.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                add_data.pd= d[position].toString()
                textView6.text=add_data.py+add_data.pm+add_data.pd
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        expiry_year.adapter=ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,y)    //유통기한연도
        expiry_year.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                add_data.ey= y[position].toString()
                println(add_data.ey+add_data.em+add_data.ed)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        expiry_month.adapter=ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,m)   //유통기한월
        expiry_month.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                add_data.em= m[position].toString()
                println(add_data.ey+add_data.em+add_data.ed)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        expiry_day.adapter=ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,d)     //유통기한일
        expiry_day.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                add_data.ed= d[position].toString()
                println(add_data.ey+add_data.em+add_data.ed)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        amount.adapter=ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,amountnum)     //유통기한일
        amount.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                add_data.var_amountnum= amountnum[position].toString()
                println(add_data.var_amountnum)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }
    fun ingredientsave(){
        add_data.ingredientname=ingredient_name.text.toString()
        println("식재료")
        println(add_data.ingredientname)
    }
}