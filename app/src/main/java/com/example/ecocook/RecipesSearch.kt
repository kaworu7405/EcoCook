package com.example.ecocook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import com.example.ecocook.manage_fridge.DetailList
import kotlinx.android.synthetic.main.activity_my_fridge.*
import kotlinx.android.synthetic.main.activity_my_fridge.icon2list
import kotlinx.android.synthetic.main.activity_recipes_search.*

class RecipesSearch : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipes_search)
        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {  //검색 눌렀을 때
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(newText: String?): Boolean { //검색창 수정될 때
                TODO("Not yet implemented")
            }

        })
        ll1.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, DetailList::class.java))
            finish()
        })
        ll2.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, DetailList::class.java))
            finish()
        })
        ll3.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, DetailList::class.java))
            finish()
        })
        ll4.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, DetailList::class.java))
            finish()
        })
        ll5.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, DetailList::class.java))
            finish()
        })
        ll6.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, DetailList::class.java))
            finish()
        })
    }

    override fun onStart() {
        super.onStart()

    }
    override fun onBackPressed() {
        startActivity(Intent(this, MainMenuActivity::class.java))
        finish()
    }
}