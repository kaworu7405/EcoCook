package com.example.ecocook

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_posting.*
import kotlinx.android.synthetic.main.activity_posting.view.*

class PostingActivity: AppCompatActivity() {

    val postsList=ArrayList<Posting>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posting)

        setPosting()

        val areaArray1:Array<String> = resources.getStringArray(R.array.areaArray)
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,areaArray1)
        areaList1.adapter=adapter

        areaList1.setOnItemClickListener { adapterView, view, i, l ->
            val clickedRoom = areaArray1[i]
            Toast.makeText(
                baseContext, clickedRoom,
                Toast.LENGTH_SHORT
            ).show()
        }

        postClicked()
    }

    fun setPosting(){
        val user = Firebase.auth.currentUser

        if(user!=null){
            val db = Firebase.firestore

            db.collection("Posting")
                .addSnapshotListener { querySnapshot: QuerySnapshot?, e: FirebaseFirestoreException? ->
                    if (querySnapshot != null) {
                        for (document in querySnapshot.documents) {
                            val obj = document.toObject<Posting>()
                            if (obj != null) {
                                print(obj.userId)
                                postsList.add(obj)
                            }
                        }
                        val postingAdapter=PostingAdapter(this, R.layout.post_view, postsList)
                        postingList.adapter=postingAdapter
                    }
                }
        }
    }

    fun postClicked(){
        postingList.setOnItemClickListener { adapterView, view, i, l ->
            val clickedPosting=postsList[i]
            val myIntent=Intent(this, PostingDetailActivity::class.java)
            myIntent.putExtra("postingInfo", clickedPosting)
            startActivity(myIntent)
        }
    }
}
