package com.example.ecocook

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.*
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_write_posting.*

class writePostingActivity : AppCompatActivity() {
    var obj = Posting()
    var imageUrl: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_posting)

        isForSharing()
        setAreaSpinner1()

        writePostingBtn.setOnClickListener {
            writePosting()
        }

        imgUrl.setOnClickListener {
            openGallery()
        }

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
        if (!buyYear.isSelected || !buyMonth.isSelected || !buyDay.isSelected) {
            Toast.makeText(
                baseContext, "구매일자를 입력해주세요.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        val buyDate =
            areaSpinner1.selectedItem.toString() + areaSpinner2.selectedItem.toString() + expiryDay.selectedItem.toString()
        if (!areaSpinner1.isSelected || !areaSpinner2.isSelected || !expiryDay.isSelected) {
            Toast.makeText(
                baseContext, "유통기한을 입력해주세요.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        val expiryDate =
            areaSpinner1.selectedItem.toString() + areaSpinner2.selectedItem.toString() + expiryDay.selectedItem.toString()
        if (!postingContentTxt.text.isEmpty()) {
            Toast.makeText(
                baseContext, "내용을 입력해주세요.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        val content = postingContentTxt.text

        if (imageUrl == null) {
            Toast.makeText(
                baseContext, "이미지를 첨부해주세요.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if(areaSpinner1.selectedItemPosition ==0){
            Toast.makeText(
                baseContext, "시/도를 선택해주세요",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if(areaSpinner2.selectedItemPosition ==0){
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
                obj.userId = user.uid.toString()
                obj.id = postingNum.toString()
                obj.postingTitle = postingTitle.toString()
                obj.postingContent = content.toString()
                obj.comments = null
                obj.auth = null//이후에 추가
                obj.buyDate = buyDate
                obj.expiryDate = expiryDate




                var fileName = "posting_" + obj.id + ".jpg"

                val storageRef = Firebase.storage.reference.child("posting_img/" + fileName)

                var uploadTask = storageRef.putFile(imageUrl!!)

                uploadTask.addOnFailureListener {
                    Toast.makeText(
                        baseContext, "사진 업로드 성공!",
                        Toast.LENGTH_SHORT
                    ).show()
                }.addOnSuccessListener {
                    Toast.makeText(
                        baseContext, "사진 업로드 실패..",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                db.collection("Posting").document(postingNum.toString()).set(obj)

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
/*
val areaArray1:Array<String> = resources.getStringArray(R.array.areaArray)
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,areaArray1)
 */
    fun setAreaSpinner1() {
        var areaArr=R.array.areaArray
        areaSpinner1.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(areaArr)
        )

        areaSpinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if(position==0){
                    setAreaSpinner2(R.array.allAreaArray)
                }
                else if(position==1){
                    setAreaSpinner2(R.array.seoulArray)
                } else if(position==2){
                    setAreaSpinner2(R.array.gyeonggiArray)
                } else if(position==3){
                    setAreaSpinner2(R.array.incheonArray)
                } else if(position==4){
                    setAreaSpinner2(R.array.gangwonArray)
                } else if(position==5){
                    setAreaSpinner2(R.array.jejuArray)
                } else if(position==6){
                    setAreaSpinner2(R.array.busanArray)
                } else if(position==7){
                    setAreaSpinner2(R.array.gyeongnamArray)
                } else if(position==8){
                    setAreaSpinner2(R.array.daeguArray)
                } else if(position==9){
                    setAreaSpinner2(R.array.gyeongbukArray)
                } else if(position==10){
                    setAreaSpinner2(R.array.ulsanArray)
                } else if(position==11){
                    setAreaSpinner2(R.array.daejeonArray)
                } else if(position==12){
                    setAreaSpinner2(R.array.chungnamArray)
                } else if(position==13){
                    setAreaSpinner2(R.array.chungbukArray)
                } else if(position==14){
                    setAreaSpinner2(R.array.gawngjuArray)
                } else if(position==15){
                    setAreaSpinner2(R.array.jeonnamArray)
                } else if(position==16){
                    setAreaSpinner2(R.array.gyeongbukArray)
                }

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

    }

    fun setAreaSpinner2(i:Int){
        areaSpinner2.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(i)
        )
    }
}
