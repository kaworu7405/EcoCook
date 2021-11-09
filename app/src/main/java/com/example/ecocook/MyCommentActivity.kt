package com.example.ecocook

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_member_init.*
import kotlinx.android.synthetic.main.activity_my_comment.*
import kotlinx.android.synthetic.main.activity_my_posting.*
import kotlinx.android.synthetic.main.activity_posting_detail.*


class MyCommentActivity : AppCompatActivity() {

    var commentsList = ArrayList<Map<String, String>>()
    var commentsIndex = ArrayList<Int>()
    var postingIdList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_comment)

        init()
        deleteCommentBtn.setOnClickListener {
            var index = myCommentList.checkedItemPosition

            val db = Firebase.firestore
            val docRef = db.collection("Posting").document(postingIdList.get(index))
            docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        if (document.exists()) {
                            var obj = document.toObject<Posting>()
                            obj?.comments?.removeAt(commentsIndex.get(index))
                            if (obj != null) {
                                db.collection("Posting").document(postingIdList.get(index)).set(obj)
                                init()
                            }
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    //Log.d(TAG, "get failed with ", exception)
                }

        }
    }

    fun init() {
        val user = Firebase.auth.currentUser
        if (user != null) {
            val db = Firebase.firestore
            db.collection("Posting").orderBy("id").get().addOnSuccessListener { result ->
                if (result != null) {
                    commentsList.clear()
                    postingIdList.clear()
                    commentsIndex.clear()
                    for (document in result.documents) {
                        val obj = document.toObject<Posting>()
                        if (obj != null) {
                            if(obj.comments!=null) {
                                var size: Int? = obj.comments?.size
                                for (i in 0 until size!!) {
                                    var id = obj.comments?.get(i)?.keys.toString().replace("[", "")
                                        .replace("]", "")
                                    if (id == user.uid) {
                                        Log.d("TAG", obj.comments?.get(i).toString())
                                        obj.comments?.let { commentsList.add(0, it[i]) }
                                        postingIdList.add(0, obj.id.toString())
                                        commentsIndex.add(0, i)
                                    }
                                }
                            }
                        }
                    }
                    val commentsAdapter = CommentsAdapter(this, R.layout.comment_view, commentsList)
                    myCommentList.adapter = commentsAdapter
                }
            }
        }
    }
}
