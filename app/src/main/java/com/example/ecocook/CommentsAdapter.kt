package com.example.ecocook

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import android.app.Activity as Activity

class CommentsAdapter (
    val mContext : Context,
    val resId:Int,
    val mList:List<Map<String, String>>

    ) : ArrayAdapter<Map<String, String>>(mContext, resId, mList) {

    val inf = LayoutInflater.from(mContext)
    val glide=Glide.with(mContext)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var tempRow = convertView
        if (tempRow == null) {
            tempRow = inf.inflate(R.layout.comment_view, null)
        }
        val row = tempRow!!

        val data = mList[position]
        val userIdText = row.findViewById<TextView>(R.id.userIdText)
        val commentContentText = row.findViewById<TextView>(R.id.commentText)
        val img=row.findViewById<ImageView>(R.id.userImg)

        val db = Firebase.firestore
        val keyName=data.keys.toString().replace("[", "").replace("]", "")

        val docRef = db.collection("users").document(keyName)
        docRef.get()
            .addOnSuccessListener{document->
                userIdText.text= document.get("name").toString()

                if(document.get("hasImage").toString()=="true") {
                    var fileName = "profile_" + keyName + ".jpg"

                    val storageRef = Firebase.storage.reference.child("profile_img/" + fileName)
                    storageRef.downloadUrl.addOnSuccessListener { uri ->
                        glide.load(uri).into(img)
                    }.addOnFailureListener {
                        // Handle any errors
                    }
                }
            }

        commentContentText.text = "${data[keyName]}"
        return row
    }
}