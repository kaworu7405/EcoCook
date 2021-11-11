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
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_posting.*
import kotlinx.android.synthetic.main.activity_posting.view.*
import kotlinx.android.synthetic.main.activity_write_posting.*
import java.util.*
import kotlin.collections.ArrayList

class PostingActivity : AppCompatActivity() {

    val postsList = ArrayList<Posting>()
    var area1 = "전체 지역"
    var area2 = "전체 지역"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posting)

        initPosting("전체 지역", "전체 지역", "")
        setAreaList()
        postingBtn.setOnClickListener {
            /*
            val myIntent = Intent(this, PostingDetailActivity::class.java)
            myIntent.putExtra("postingInfo", clickedPosting)
            startActivity(myIntent)
             */
            var isNew = true
            val myIntent = Intent(this, writePostingActivity::class.java)
            myIntent.putExtra("isNew", isNew)
            startActivity(myIntent)
        }

        findBtn.setOnClickListener {
            searchPosting()
        }

        postClicked()
        checkBox()
    }

    fun searchPosting() {
        var findText = searchInputText.text
        val user = Firebase.auth.currentUser
        if (user != null) {
            val db = Firebase.firestore
            db.collection("Posting").orderBy("id").get().addOnSuccessListener { result ->
                if (result != null) {
                    postsList.clear()
                    for (document in result) {
                        val obj = document.toObject<Posting>()
                        if (obj != null) {
                            if (obj.postingContent?.contains(findText) == true || obj.postingTitle?.contains(
                                    findText
                                ) == true
                            ) {
                                postsList.add(0, obj)
                            }
                        }
                    }
                    Log.d("TAG", "111111111111111111111111111")
                    val postingAdapter = PostingAdapter(this, R.layout.post_view, postsList)
                    postingList.adapter = postingAdapter
                }
            }
        }
    }

    fun checkBox() {
        allCheckBox.setOnCheckedChangeListener { compoundButton, b ->
            if (b) { //check되었으면
                noAuthCheckBox.isChecked = false
                AuthCheckBox.isChecked = false
                noAuthCheckBox.isEnabled = false
                AuthCheckBox.isEnabled = false
                setPosting(area1, area2, "")
            } else {
                AuthCheckBox.isChecked = true
                noAuthCheckBox.isEnabled = true
                AuthCheckBox.isEnabled = true
            }
        }

        noAuthCheckBox.setOnCheckedChangeListener { compoundButton, b ->
            if (!b && !allCheckBox.isChecked) {
                AuthCheckBox.isChecked = true
            } else if (b && !allCheckBox.isChecked) {
                AuthCheckBox.isChecked = false
                setPosting(area1, area2, "[일반]")
            }
        }

        AuthCheckBox.setOnCheckedChangeListener { compoundButton, b ->
            if (!b && !allCheckBox.isChecked) {
                noAuthCheckBox.isChecked = true
            } else if (b && !allCheckBox.isChecked) {
                noAuthCheckBox.isChecked = false
                setPosting(
                    area1,
                    area2,
                    "[인증]"
                )
            }
        }
    }

    fun setPosting(category1: String, category2: String, auth: String) {
        val user = Firebase.auth.currentUser
        if (user != null) {
            val db = Firebase.firestore

            db.collection("Posting").orderBy("id").get().addOnSuccessListener {result->
                if (result != null) {
                    postsList.clear()
                    for (document in result) {
                        val obj = document.toObject<Posting>()
                        if (obj != null) {
                            if (category1 == "전체 지역") {
                                if (auth == "[인증]" && obj.auth == auth) {
                                    postsList.add(0, obj)
                                } else if (auth == "[일반]" && obj.auth == auth) {
                                    postsList.add(0, obj)
                                } else if (auth == "") {
                                    postsList.add(0, obj)
                                }
                            } else if (obj.area1.equals(category1)) {
                                if (category2 == "전체 지역") {
                                    if (auth == "[인증]" && obj.auth == auth) {
                                        postsList.add(0, obj)
                                    } else if (auth == "[일반]" && obj.auth == auth) {
                                        postsList.add(0, obj)
                                    } else if (auth == "") {
                                        postsList.add(0, obj)
                                    }
                                } else if (category2 == obj.area2) {
                                    if (auth == "[인증]" && obj.auth == auth) {
                                        postsList.add(0, obj)
                                    } else if (auth == "[일반]" && obj.auth == auth) {
                                        postsList.add(0, obj)
                                    } else if (auth == "") {
                                        postsList.add(0, obj)
                                    }
                                }
                            }
                        }
                    }
                    Log.d("TAG", "22222222222222222222")
                    val postingAdapter = PostingAdapter(this, R.layout.post_view, postsList)
                    postingList.adapter = postingAdapter
                }
            }
        }
    }

    fun initPosting(category1: String, category2: String, auth: String) {
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
                                if (category1 == "전체 지역") {
                                    if (auth == "[인증]" && obj.auth == auth) {
                                        postsList.add(0, obj)
                                    } else if (auth == "[일반]" && obj.auth == auth) {
                                        postsList.add(0, obj)
                                    } else if (auth == "") {
                                        postsList.add(0, obj)
                                    }
                                } else if (obj.area1.equals(category1)) {
                                    if (category2 == "전체 지역") {
                                        if (auth == "[인증]" && obj.auth == auth) {
                                            postsList.add(0, obj)
                                        } else if (auth == "[일반]" && obj.auth == auth) {
                                            postsList.add(0, obj)
                                        } else if (auth == "") {
                                            postsList.add(0, obj)
                                        }
                                    } else if (category2 == obj.area2) {
                                        if (auth == "[인증]" && obj.auth == auth) {
                                            postsList.add(0, obj)
                                        } else if (auth == "[일반]" && obj.auth == auth) {
                                            postsList.add(0, obj)
                                        } else if (auth == "") {
                                            postsList.add(0, obj)
                                        }
                                    }
                                }
                            }
                        }
                        Log.d("TAG", "333333333333333333")
                        val postingAdapter = PostingAdapter(this, R.layout.post_view, postsList)
                        postingList.adapter = postingAdapter
                    }
                }
        }
    }

    fun postClicked() {
        postingList.setOnItemClickListener { adapterView, view, i, l ->
            val clickedPosting = postsList[i]
            val myIntent = Intent(this, PostingDetailActivity::class.java)
            myIntent.putExtra("postingInfo", clickedPosting)
            startActivity(myIntent)
        }
    }

    fun setAreaList() {
        val areaArray1: Array<String> = resources.getStringArray(R.array.areaArray)
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, areaArray1)
        areaList1.adapter = adapter
        var authCondition: String = ""

        if (allCheckBox.isChecked) {
            authCondition = ""
        } else if (AuthCheckBox.isChecked) {
            authCondition = "[인증]"
        } else if (noAuthCheckBox.isChecked) {
            authCondition = "[일반]"
        }

        areaList1.setOnItemClickListener { adapterView, view, i, l ->
            val clickedArea1 = areaArray1[i]
            area1 = clickedArea1
            if (clickedArea1.equals("전체 지역")) {
                setAreaList2("전체 지역", R.array.allAreaArray, authCondition)

                setPosting("전체 지역", "전체 지역", authCondition)
            } else if (clickedArea1.equals("서울")) {
                setAreaList2("서울", R.array.seoulArray, authCondition)

                setPosting("서울", "전체 지역", authCondition)
            } else if (clickedArea1.equals("경기도")) {
                setAreaList2("경기도", R.array.gyeonggiArray, authCondition)

                setPosting("경기도", "전체 지역", authCondition)
            } else if (clickedArea1.equals("인천")) {
                setAreaList2("인천", R.array.incheonArray, authCondition)
                setPosting("인천", "전체 지역", authCondition)
            } else if (clickedArea1.equals("강원")) {
                setAreaList2("강원", R.array.gangwonArray, authCondition)
                setPosting("강원", "전체 지역", authCondition)
            } else if (clickedArea1.equals("제주")) {
                setAreaList2("제주", R.array.jejuArray, authCondition)
                setPosting("제주", "전체 지역", authCondition)
            } else if (clickedArea1.equals("부산")) {
                setAreaList2("부산", R.array.busanArray, authCondition)
                setPosting("부산", "전체 지역", authCondition)
            } else if (clickedArea1.equals("경남")) {
                setAreaList2("경남", R.array.gyeongnamArray, authCondition)
                setPosting("경남", "전체 지역", authCondition)
            } else if (clickedArea1.equals("대구")) {
                setAreaList2("대구", R.array.daeguArray, authCondition)

                setPosting("대구", "전체 지역", authCondition)
            } else if (clickedArea1.equals("경북")) {
                setAreaList2("경북", R.array.gyeongbukArray, authCondition)
                setPosting("경북", "전체 지역", authCondition)
            } else if (clickedArea1.equals("울산")) {
                setAreaList2("울산", R.array.ulsanArray, authCondition)
                setPosting("울산", "전체 지역", authCondition)
            } else if (clickedArea1.equals("대전")) {
                setAreaList2("대전", R.array.daejeonArray, authCondition)
                setPosting("대전", "전체 지역", authCondition)
            } else if (clickedArea1.equals("충남")) {
                setAreaList2("충남", R.array.chungnamArray, authCondition)
                setPosting("충남", "전체 지역", authCondition)
            } else if (clickedArea1.equals("충북")) {
                setAreaList2("충북", R.array.chungbukArray, authCondition)
                setPosting("충북", "전체 지역", authCondition)
            } else if (clickedArea1.equals("광주")) {
                setAreaList2("광주", R.array.gawngjuArray, authCondition)
                setPosting("광주", "전체 지역", authCondition)
            } else if (clickedArea1.equals("전남")) {
                setAreaList2("전남", R.array.jeonnamArray, authCondition)
                setPosting("전남", "전체 지역", authCondition)
            } else if (clickedArea1.equals("전북")) {
                setAreaList2("전북", R.array.jeonbukArray, authCondition)
                setPosting("전북", "전체 지역", authCondition)
            }
        }

    }

    fun setAreaList2(str: String, i: Int, authCondition: String) {
        val areaArray2: Array<String> = resources.getStringArray(i)
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, areaArray2)
        areaList2.adapter = adapter
        areaList2.setOnItemClickListener { adapterView, view, i, l ->
            val clickedArea1 = areaArray2[i]
            area2 = clickedArea1
            setPosting(str, areaArray2.get(i), authCondition)
        }
    }
}
