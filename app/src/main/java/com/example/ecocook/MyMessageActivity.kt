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
import kotlinx.android.synthetic.main.activity_message.*
import kotlinx.android.synthetic.main.activity_my_message.*
import kotlinx.android.synthetic.main.activity_my_posting.*

class MyMessageActivity : AppCompatActivity() {

    var messageList = ArrayList<Message>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_message)

        setMessage()
        chattingClicked()
    }

    fun setMessage() {
        val user = Firebase.auth.currentUser
        if (user != null) {
            val db = Firebase.firestore

            db.collection("Message")
                .addSnapshotListener { querySnapshot: QuerySnapshot?, e: FirebaseFirestoreException? ->
                    if (querySnapshot != null) {
                        messageList.clear()
                        for (document in querySnapshot.documents) {
                            val obj = document.toObject<Message>()
                            if (obj != null) {
                                if (user.uid == obj.user1 || user.uid == obj.user2) {
                                    messageList.add(0, obj)
                                }
                            }
                        }
                        val messageAdapter = MessageAdapter(this, R.layout.post_view, messageList)
                        myMessageListView.adapter = messageAdapter
                    }
                }
        }
    }

    fun chattingClicked(){
        myMessageListView.setOnItemClickListener { adapterView, view, i, l ->
            val clickedMessage=messageList[i]
            val myIntent = Intent(this, MessageActivity::class.java)
            myIntent.putExtra("message", clickedMessage)

            startActivity(myIntent)
        }
    }

}