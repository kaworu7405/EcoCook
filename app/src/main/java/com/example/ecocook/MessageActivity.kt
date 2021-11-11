package com.example.ecocook

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_message.*
import kotlinx.android.synthetic.main.activity_posting_detail.*

class MessageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        val message = intent.getSerializableExtra("message") as Message
        val db = Firebase.firestore

        val docRef = db.collection("Message").document(message.id.toString())
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w("TAG", "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val obj = snapshot.toObject<Message>()
                Log.w("TAG", "11111111111111111111111", e)
                if (obj != null) {
                    if(obj.message!=null){
                        val messageAdapter=MessageContentAdapter(this, R.layout.message_content_view,
                            obj.message!!
                        )
                        messageLogList.adapter = messageAdapter
                    }
                }
            } else {
                Log.w("TAG", "222222222222222222222", e)
                Log.d("TAG", "Current data: null")
            }
        }
        messageSendBtn.setOnClickListener {
            if (chattingInputText.text.toString().isNotEmpty()) {
                val user = Firebase.auth.currentUser
                var arr = ArrayList<Map<String, String>>()
                val docRef = db.collection("Message").document(message.id.toString())
                docRef.get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            if (document.exists()) {
                                val obj = document.toObject<Message>()
                                var chatting = obj?.message
                                if (chatting != null) {
                                    for (c in chatting) {
                                        arr.add(c)
                                    }
                                }
                                if (user != null) {
                                    arr.add(mapOf(user.uid to chattingInputText.text.toString()))
                                    chattingInputText.setText("")
                                }
                                if (obj != null) {
                                    obj.message = arr
                                    docRef.set(obj)
                                }

                            }
                        }
                    }
            }
        }
    }


}