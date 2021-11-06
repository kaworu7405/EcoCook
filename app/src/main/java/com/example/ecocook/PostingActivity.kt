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
        setAreaList()
        postingBtn.setOnClickListener {
            startActivity(Intent(this, writePostingActivity::class.java))
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

    fun setAreaList2(i:Int){
        val areaArray2:Array<String> = resources.getStringArray(R.array.jeonbukArray)
        val adapter=ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, areaArray2)
        areaList2.adapter=adapter
    }

    fun setAreaList(){
        val areaArray1:Array<String> = resources.getStringArray(R.array.areaArray)
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,areaArray1)
        areaList1.adapter=adapter

        areaList1.setOnItemClickListener { adapterView, view, i, l ->
            val clickedRoom = areaArray1[i]
            Toast.makeText(
                baseContext, clickedRoom,
                Toast.LENGTH_SHORT
            ).show()

            if(clickedRoom.equals("전체 지역")){
                setAreaList2(R.array.allAreaArray)
            }
            else if(clickedRoom.equals("서울")){
                setAreaList2(R.array.seoulArray)
            }
            else if(clickedRoom.equals("경기도")){
                setAreaList2(R.array.gyeonggiArray)
            }
            else if(clickedRoom.equals("인천")){
                setAreaList2(R.array.incheonArray)
            }
            else if(clickedRoom.equals("강원")){
                setAreaList2(R.array.gangwonArray)
            }
            else if(clickedRoom.equals("제주")){
                setAreaList2(R.array.jejuArray)
            }
            else if(clickedRoom.equals("부산")){
                setAreaList2(R.array.busanArray)
            }
            else if(clickedRoom.equals("경남")){
                setAreaList2(R.array.gyeongnamArray)
            }
            else if(clickedRoom.equals("대구")){
                setAreaList2(R.array.daeguArray)
            }
            else if(clickedRoom.equals("경북")){
                setAreaList2(R.array.gyeongbukArray)
            }
            else if(clickedRoom.equals("울산")){
                setAreaList2(R.array.ulsanArray)
            }
            else if(clickedRoom.equals("대전")){
                setAreaList2(R.array.daejeonArray)
            }
            else if(clickedRoom.equals("충남")){
                setAreaList2(R.array.chungnamArray)
            }
            else if(clickedRoom.equals("충북")){
                setAreaList2(R.array.chungbukArray)
            }
            else if(clickedRoom.equals("광주")){

                setAreaList2(R.array.gawngjuArray)
            }
            else if(clickedRoom.equals("전남")){
                setAreaList2(R.array.jeonnamArray)
            }
            else if(clickedRoom.equals("전북")){
                setAreaList2(R.array.jeonbukArray)
            }
        }

        areaList2.setOnItemClickListener { adapterView, view, i, l ->
            val clickedRoom = areaArray1[i]
            Toast.makeText(
                baseContext, clickedRoom,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
