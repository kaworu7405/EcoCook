package com.example.ecocook.manage_fridge

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import com.example.ecocook.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_add_ingredient.*
import kotlinx.android.synthetic.main.activity_add_ingredient.amount
import kotlinx.android.synthetic.main.activity_add_ingredient.category_ingredient
import kotlinx.android.synthetic.main.activity_add_ingredient.expiry_day
import kotlinx.android.synthetic.main.activity_add_ingredient.expiry_month
import kotlinx.android.synthetic.main.activity_add_ingredient.expiry_year
import kotlinx.android.synthetic.main.activity_add_ingredient.ingredient_name
import kotlinx.android.synthetic.main.activity_add_ingredient.ingredient_save
import kotlinx.android.synthetic.main.activity_add_ingredient.purchase_day
import kotlinx.android.synthetic.main.activity_add_ingredient.purchase_month
import kotlinx.android.synthetic.main.activity_add_ingredient.purchase_year
import kotlinx.android.synthetic.main.activity_edit_ingredient.*

class EditIngredient : AppCompatActivity() {
    val ingredient_list= listOf<String>("과일","쌀/곡물","견과","정육/계란","유제품","김치/반찬","음료","기타")
    val y=Array(140,{i->i+2000})
    val m=Array(12,{i->i+1})
    val d=Array(31,{i->i+1})
    val amountnum=Array(130,{i->i+1})
    var id =0
    var add_data = AddIngredientData()
    val user = Firebase.auth.currentUser


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_ingredient)
        if(intent.hasExtra("detailindex")){
            id= intent.getIntExtra("detailindex",0)
        }
        if(intent.hasExtra("detailname")){
            edit_name.setText(intent.getStringExtra("detailname"))
        }
        spinner()
        ingredient_save.setOnClickListener{
            ingredientupdate()
            startActivity(Intent(this, DetailList::class.java))
            finish()
        }
    }
    override fun onBackPressed() {
        startActivity(Intent(this, DetailLook::class.java))
        finish()
    }
    fun spinner(){
        category_ingredient.adapter=
            ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,ingredient_list)    //식재료명
        category_ingredient.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                add_data.kind=ingredient_list[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        purchase_year.adapter= ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,y)    //구입연도
        purchase_year.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                add_data.py= y[position].toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        purchase_month.adapter= ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,m)   //구입월
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

        purchase_day.adapter= ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,d)     //구입일
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
        expiry_year.adapter= ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,y)    //유통기한연도
        expiry_year.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                add_data.ey= y[position].toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        expiry_month.adapter= ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,m)   //유통기한월
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

        expiry_day.adapter= ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,d)     //유통기한일
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
        amount.adapter= ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,amountnum)     //유통기한일
        amount.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                add_data.var_amountnum= amountnum[position].toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }
    fun ingredientupdate(){
        add_data.ingredientname=edit_name.text.toString()
        var category = add_data.kind
        var name = add_data.ingredientname
        var iconId : String? = null //제가 이후에 음식 종류에 따른 이미지 url가르쳐드릴테니 야채인지 과일인지에 따라 그 url을 여기에 넣어주시면 됩니다!
        var buyDate = (add_data.py+add_data.pm+add_data.pd).toInt()
        var expiryDate = (add_data.ey+add_data.em+add_data.ed).toInt()
        var num = add_data.var_amountnum.toInt()


        val db = Firebase.firestore
        val f = db.collection(user?.uid.toString())
        if (f != null) {
            f.document(id.toString()).update("name", name)
            f.document(id.toString()).update("category", category)
            f.document(id.toString()).update("buyDate", buyDate)
            f.document(id.toString()).update("expiryDate", expiryDate)
            f.document(id.toString()).update("num", num)
        }
    }
}