package com.example.ecocook

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_posting_detail.*

class MessageAdapter(
    val mContext: Context,
    val resId: Int,
    val mList: List<Message>

) : ArrayAdapter<Message>(mContext, resId, mList) {

    val inf = LayoutInflater.from(mContext)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var tempRow = convertView
        if (tempRow == null) {
            tempRow = inf.inflate(R.layout.message_list_view, null)
        }
        val row = tempRow!!

        val data = mList[position]
        val anotherUserImageView = row.findViewById<ImageView>(R.id.AnotherUserImage)
        val explainText = row.findViewById<TextView>(R.id.messageExplainText)

        val user = Firebase.auth.currentUser
        var anotherUserId=""
        if (user != null) {
            if(user.uid==data.user1){
                anotherUserId= data.user2.toString()
            }
            else{
                anotherUserId= data.user1.toString()
            }
        }

        val db = Firebase.firestore
        val docRef = db.collection("users").document(anotherUserId)
        docRef.get()
            .addOnSuccessListener { document ->
                val str=document.get("name").toString()+"님과의 채팅입니다."
                explainText.setText(str)

                var fileName = "profile_" + anotherUserId + ".jpg"

                val storageRef = Firebase.storage.reference.child("profile_img/" + fileName)
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    Glide.with(mContext).load(uri).into(anotherUserImageView)
                }.addOnFailureListener {
                    // Handle any errors
                }
            }
        return row
    }
}