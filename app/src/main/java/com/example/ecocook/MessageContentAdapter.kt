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

class MessageContentAdapter (
    val mContext: Context,
    val resId: Int,
    val mList: List<Map<String, String>>

) : ArrayAdapter<Map<String, String>>(mContext, resId, mList) {

    val inf = LayoutInflater.from(mContext)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var tempRow = convertView
        if (tempRow == null) {
            tempRow = inf.inflate(R.layout.message_content_view, null)
        }
        val row = tempRow!!

        val data = mList[position]
        val nameText = row.findViewById<TextView>(R.id.nameTextView)
        val contentText = row.findViewById<TextView>(R.id.messageContentTextView)

        val user = Firebase.auth.currentUser

        val keyName=data.keys.toString().replace("[", "").replace("]", "")

        val db = Firebase.firestore
        val docRef = db.collection("users").document(keyName)
        docRef.get()
            .addOnSuccessListener{document->
                nameText.text= document.get("name").toString()

            }
        contentText.text="${data[keyName]}"
        return row
    }
}