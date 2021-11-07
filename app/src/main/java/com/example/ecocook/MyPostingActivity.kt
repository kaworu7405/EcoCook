package com.example.ecocook

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_my_posting.*
import kotlinx.android.synthetic.main.activity_posting.*
import android.widget.ListAdapter as ListAdapter

class MyPostingActivity :  AppCompatActivity(){

    var postsList = ArrayList<Posting>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_posting)

        setPosting()
        postClicked()
    }

    fun setPosting() {
        val user = Firebase.auth.currentUser
        if (user != null) {
            val db = Firebase.firestore

            db.collection("Posting").orderBy("id")
                .addSnapshotListener { querySnapshot: QuerySnapshot?, e: FirebaseFirestoreException? ->
                    if (querySnapshot != null) {
                        postsList.clear()
                        for (document in querySnapshot.documents) {
                            val obj = document.toObject<Posting>()
                            if (obj != null) {
                                if(obj.userId==user.uid){
                                    postsList.add(0, obj)
                                }
                            }
                        }
                        val postingAdapter = PostingAdapter(this, R.layout.post_view, postsList)
                        myPostingList.adapter = postingAdapter
                    }
                }
        }
    }

    fun postClicked() {
        myPostingList.setOnItemClickListener { adapterView, view, i, l ->
            val clickedPosting = postsList[i]
            val myIntent = Intent(this, PostingDetailActivity::class.java)
            myIntent.putExtra("postingInfo", clickedPosting)
            startActivity(myIntent)
        }
    }
}