package com.example.ecocook

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_posting_detail.*
import kotlinx.android.synthetic.main.activity_write_posting.*

class writePostingActivity : AppCompatActivity() {
    var obj = Posting()
    var imageUrl: Uri? = null
    var isRevise:Boolean=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_posting)


        val postingInfo=intent.getSerializableExtra("postingInfo") as Posting
        setAreaSpinner1()
        setBuyDateSpinner()
        setExpiryDateSpinner()
        if(postingInfo!=null){
            initForRevising(postingInfo)
            isRevise=true
        }
        isForSharing()


        writePostingBtn.setOnClickListener {
            writePosting()
        }

        imgUrl.setOnClickListener {
            openGallery()
        }

        cancelBtn.setOnClickListener {
            val myIntent = Intent(this, PostingDetailActivity::class.java)
            myIntent.putExtra("postingInfo", obj)
            startActivity(myIntent)
            finish()
        }

    }

    fun initForRevising(postingInfo:Posting){
        obj=postingInfo
        postingTitleText.setText(postingInfo.postingTitle)
        postingInfo.id?.let { getFireBaseFoodImage(it) }
        priceText.setText(postingInfo.price)
        postingContentTxt.setText(postingInfo.postingContent)
    }

    fun isForSharing() { //나눔인지 아닌지 체크
        shareCheckBox.setOnClickListener {
            if (priceText.isEnabled) {
                priceText.setText("0")
                priceText.isEnabled = false
            } else {
                priceText.setText("")
                priceText.isEnabled = true
            }
        }
    }

    fun writePosting() {
        if (postingTitleText.text.isEmpty()) {
            Toast.makeText(
                baseContext, "제목을 입력해주세요.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        val postingTitle = postingTitleText.text
        if (priceText.text.isEmpty()) {
            Toast.makeText(
                baseContext, "가격을 입력해주세요.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        val price = priceText.text

        val buyDate =
            buyYearSpinner.selectedItem.toString() + "." + buyMonthSpinner.selectedItem.toString() + "." + buyDaySpinner.selectedItem.toString()

        val expiryDate =
            expiryYearSpinner.selectedItem.toString() + "." + expiryMonthSpinner.selectedItem.toString() + "." + expiryDaySpinner.selectedItem.toString()
        if (postingContentTxt.text.isEmpty()) {
            Toast.makeText(
                baseContext, "내용을 입력해주세요.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        val content = postingContentTxt.text

        if (imageUrl == null&&!isRevise) {
            Toast.makeText(
                baseContext, "이미지를 첨부해주세요.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (areaSpinner1.selectedItemPosition == 0) {
            Toast.makeText(
                baseContext, "시/도를 선택해주세요",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (areaSpinner2.selectedItemPosition == 0) {
            Toast.makeText(
                baseContext, "구/동을 선택해주세요",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val user = Firebase.auth.currentUser
        val db = Firebase.firestore
        val f = db.collection("Posting").orderBy("id").get().addOnSuccessListener { result ->
            val postingNum = result.size() + 1
            if (user != null) {
                obj.userId = user.uid
                obj.postingTitle = postingTitle.toString()
                obj.postingContent = content.toString()
                obj.buyDate = buyDate
                obj.expiryDate = expiryDate
                obj.price = price.toString()
                if(!isRevise){
                    obj.id = postingNum.toString()
                    obj.comments = null
                }
                val docRef = Firebase.firestore.collection("users").document(user.uid)
                docRef.get()
                    .addOnSuccessListener{document->
                        obj.auth= document.get("auth").toString()
                    }

                var fileName = "posting_" + obj.id + ".jpg"
                obj.imgUrl=("posting_img/" + fileName)
                val storageRef = Firebase.storage.reference.child("posting_img/" + fileName)
                if(imageUrl!=null) {
                    var uploadTask = storageRef.putFile(imageUrl!!)
                }
                db.collection("Posting").document(obj.id.toString()).set(obj)

                val myIntent = Intent(this, PostingDetailActivity::class.java)
                myIntent.putExtra("postingInfo", obj)
                startActivity(myIntent)
                finish()

            }
        }.addOnFailureListener { exception ->
            Toast.makeText(
                baseContext, "인터넷 연결이 끊어져있습니다.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        startActivityForResult(intent, 1)
    }

    fun getFireBaseFoodImage(id:String){
        var fileName="posting_"+id+".jpg"

        val storageRef=Firebase.storage.reference.child("posting_img/"+fileName)
        storageRef.downloadUrl.addOnSuccessListener { uri->
            Glide.with(this).load(uri).into(imgUrl)
        }.addOnFailureListener {
            // Handle any errors
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                1 -> { //갤러리에 접근하고자 할 때
                    data?.data?.let { uri ->
                        imgUrl.setImageURI(uri)
                        imageUrl = uri
                    }
                }
            }
        }
    }

    fun setBuyDateSpinner() {
        var buyYearArr = resources.getStringArray(R.array.yearArray)
        buyYearSpinner.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            buyYearArr
        )

        var buyMonthArr = resources.getStringArray(R.array.monthArray)
        buyMonthSpinner.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            buyMonthArr
        )

        setBuyDaySpinner(R.array.dayArray31)

        buyMonthSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    setBuyDaySpinner(R.array.dayArray31)
                } else if (position == 1) {
                    setBuyDaySpinner(R.array.dayArray29)
                } else if (position == 2) {
                    setBuyDaySpinner(R.array.dayArray31)
                } else if (position == 3) {
                    setBuyDaySpinner(R.array.dayArray30)
                } else if (position == 4) {
                    setBuyDaySpinner(R.array.dayArray31)
                } else if (position == 5) {
                    setBuyDaySpinner(R.array.dayArray30)
                } else if (position == 6) {
                    setBuyDaySpinner(R.array.dayArray31)
                } else if (position == 7) {
                    setBuyDaySpinner(R.array.dayArray31)
                } else if (position == 8) {
                    setBuyDaySpinner(R.array.dayArray30)
                } else if (position == 9) {
                    setBuyDaySpinner(R.array.dayArray31)
                } else if (position == 10) {
                    setBuyDaySpinner(R.array.dayArray30)
                } else if (position == 11) {
                    setBuyDaySpinner(R.array.dayArray31)
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    fun setBuyDaySpinner(i: Int) {
        buyDaySpinner.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(i)
        )
    }

    fun setExpiryDaySpinner(i: Int) {
        expiryDaySpinner.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(i)
        )
    }

    fun setExpiryDateSpinner(){
        var expiryYearArr = resources.getStringArray(R.array.yearArray)
        expiryYearSpinner.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            expiryYearArr
        )

        var expiryMonthArr = resources.getStringArray(R.array.monthArray)
        expiryMonthSpinner.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            expiryMonthArr
        )

        setExpiryDaySpinner(R.array.dayArray31)

        expiryMonthSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    setExpiryDaySpinner(R.array.dayArray31)
                } else if (position == 1) {
                    setExpiryDaySpinner(R.array.dayArray29)
                } else if (position == 2) {
                    setExpiryDaySpinner(R.array.dayArray31)
                } else if (position == 3) {
                    setExpiryDaySpinner(R.array.dayArray30)
                } else if (position == 4) {
                    setExpiryDaySpinner(R.array.dayArray31)
                } else if (position == 5) {
                    setExpiryDaySpinner(R.array.dayArray30)
                } else if (position == 6) {
                    setExpiryDaySpinner(R.array.dayArray31)
                } else if (position == 7) {
                    setExpiryDaySpinner(R.array.dayArray31)
                } else if (position == 8) {
                    setExpiryDaySpinner(R.array.dayArray30)
                } else if (position == 9) {
                    setExpiryDaySpinner(R.array.dayArray31)
                } else if (position == 10) {
                    setExpiryDaySpinner(R.array.dayArray30)
                } else if (position == 11) {
                    setExpiryDaySpinner(R.array.dayArray31)
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    fun setAreaSpinner1() {
        var areaArr = resources.getStringArray(R.array.areaArray)
        areaSpinner1.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            areaArr
        )

        areaSpinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                obj.area1 = areaArr.get(position)

                if (position == 0) {
                    setAreaSpinner2(R.array.allAreaArray)
                } else if (position == 1) {
                    setAreaSpinner2(R.array.seoulArray)
                } else if (position == 2) {
                    setAreaSpinner2(R.array.gyeonggiArray)
                } else if (position == 3) {
                    setAreaSpinner2(R.array.incheonArray)
                } else if (position == 4) {
                    setAreaSpinner2(R.array.gangwonArray)
                } else if (position == 5) {
                    setAreaSpinner2(R.array.jejuArray)
                } else if (position == 6) {
                    setAreaSpinner2(R.array.busanArray)
                } else if (position == 7) {
                    setAreaSpinner2(R.array.gyeongnamArray)
                } else if (position == 8) {
                    setAreaSpinner2(R.array.daeguArray)
                } else if (position == 9) {
                    setAreaSpinner2(R.array.gyeongbukArray)
                } else if (position == 10) {
                    setAreaSpinner2(R.array.ulsanArray)
                } else if (position == 11) {
                    setAreaSpinner2(R.array.daejeonArray)
                } else if (position == 12) {
                    setAreaSpinner2(R.array.chungnamArray)
                } else if (position == 13){
                    setAreaSpinner2(R.array.chungbukArray)
                } else if (position == 14){
                    setAreaSpinner2(R.array.gawngjuArray)
                } else if (position == 15){
                    setAreaSpinner2(R.array.jeonnamArray)
                } else if (position == 16){
                    setAreaSpinner2(R.array.jeonbukArray)
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        areaSpinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                obj.area2 = areaSpinner2.getItemAtPosition(position) as String?
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    fun setAreaSpinner2(i: Int) {
        areaSpinner2.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(i)
        )
    }
}
