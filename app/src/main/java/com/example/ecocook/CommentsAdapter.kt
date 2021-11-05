package com.example.ecocook

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CommentsAdapter (
    val mContext : Context,
    val resId:Int,
    val mList:List<Map<String, String>>

    ) : ArrayAdapter<Map<String, String>>(mContext, resId, mList) {

    val inf = LayoutInflater.from(mContext)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var tempRow = convertView
        if (tempRow == null) {
            tempRow = inf.inflate(R.layout.comment_view, null)
        }
        val row = tempRow!!

        val data = mList[position]
        val userIdText = row.findViewById<TextView>(R.id.userIdText)
        val commentContentText = row.findViewById<TextView>(R.id.commentText)

        val db = Firebase.firestore
        val keyName=data.keys.toString().replace("[", "").replace("]", "")

        val docRef = db.collection("users").document(keyName)
        docRef.get()
            .addOnSuccessListener{document->
                userIdText.text= document.get("name").toString()
            }

        commentContentText.text = "${data[keyName]}"

        return row
    }
}