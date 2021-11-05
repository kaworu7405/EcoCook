package com.example.ecocook

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class PostingAdapter (
    val mContext : Context,
    val resId:Int,
    val mList:List<Posting>

        ) : ArrayAdapter<Posting>(mContext, resId, mList) {

    val inf = LayoutInflater.from(mContext)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var tempRow=convertView
        if(tempRow==null){
            tempRow=inf.inflate(R.layout.post_view, null)
        }
        val row=tempRow!!

        val data=mList[position]
        val authText=row.findViewById<TextView>(R.id.authText)
        val postTitleText=row.findViewById<TextView>(R.id.postTitleText)
        val contentText=row.findViewById<TextView>(R.id.contentText)

        authText.text="${data.auth}"
        postTitleText.text="${data.postingTitle}"
        contentText.text="${data.postingContent}"

        return row
    }
}

