package com.example.ecocook.manage_fridge

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.ecocook.R
import com.example.ecocook.UserFridge
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_add_ingredient.*

class AddIngredient : AppCompatActivity() {
    val ingredient_list= listOf<String>("과일","곡물","견과","정육/계란","유제품","김치/반찬","음료","기타")
    val y=Array(140,{i->i+2000})
    val m=Array(12,{i->i+1})
    val d=Array(31,{i->i+1})
    val amountnum=Array(130,{i->i+1})
    var add_data = AddIngredientData()
    val user = Firebase.auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_ingredient)

        spinner()
        ingredient_save.setOnClickListener{
            ingredientsave()
            startActivity(Intent(this, MyFridge::class.java))
            finish()
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
                add_data.kind=ingredient_list[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        purchase_year.adapter=ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,y)    //구입연도
        purchase_year.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                add_data.py= y[position].toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        purchase_month.adapter=ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,m)   //구입월
        purchase_month.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                add_data.pm= m[position].toString()
                if(add_data.pm.length==1){
                    add_data.pm="0"+add_data.pm
                }

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        purchase_day.adapter=ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,d)     //구입일
        purchase_day.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                add_data.pd= d[position].toString()
                if(add_data.pd.length==1){
                    add_data.pd="0"+add_data.pd
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        expiry_year.adapter=ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,y)    //유통기한연도
        expiry_year.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                add_data.ey= y[position].toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        expiry_month.adapter=ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,m)   //유통기한월
        expiry_month.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                add_data.em= m[position].toString()
                if(add_data.em.length==1){
                    add_data.em="0"+add_data.em
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        expiry_day.adapter=ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,d)     //유통기한일
        expiry_day.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                add_data.ed= d[position].toString()
                if(add_data.ed.length==1){
                    add_data.ed="0"+add_data.ed
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        amount.adapter=ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,amountnum)     //유통기한일
        amount.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                add_data.var_amountnum= amountnum[position].toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }
    fun ingredientsave(){
        add_data.ingredientname=ingredient_name.text.toString()
        var category = add_data.kind
        var name = add_data.ingredientname
        var iconId : String? = null //제가 이후에 음식 종류에 따른 이미지 url가르쳐드릴테니 야채인지 과일인지에 따라 그 url을 여기에 넣어주시면 됩니다!
        var buyDate = (add_data.py+add_data.pm+add_data.pd).toInt()
        var expiryDate = (add_data.ey+add_data.em+add_data.ed).toInt()
        var num = add_data.var_amountnum.toInt()
        var id : Int=0

        val db = Firebase.firestore
        val f = db.collection(user?.uid.toString())
        f.get()
            .addOnSuccessListener { result ->
                var foodNum=result.size()+1 //현재 냉장고에 저장되어있는 음식의 수에다가 1 더해준 값
                val data = hashMapOf(
                    "category" to category,
                    "name" to name,
                    //iconId 설명 : 제가 이후에 음식 종류에 따른 이미지 url가르쳐드릴테니 야채인지 과일인지에 따라 그 url을 여기에 넣어주시면 됩니다!
                    "iconId" to "null",
                    "buyDate" to buyDate,
                    "expiryDate" to expiryDate,
                    "num" to num,
                    "id" to foodNum
                )
                f.document(foodNum.toString()).set(data) // 현재 냉장고에 저장되어있는 음식의 수에다가 1더해준 값을 문서의 이름과 id로 정해준다.
                Log.d("TAG", foodNum.toString())
            }
            .addOnFailureListener { exception ->

            }
    }
}