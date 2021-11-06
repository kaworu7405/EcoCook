package com.example.ecocook

// 사용자의 냉장고에 저장되는 식재료 정보입니다.
class UserFridge {
    var category : String? = null //음식분류(과일, 채소, ...)
    var name : String? = null //음식명
    var iconId : String? = null //아래의 방법으로 ImageView에 사진 불러오세요!
    var buyDate : Int = 20211029 //산 날짜
    var expiryDate : Int = 20211029 //유통기한
    var num : Int = 5 //개수
    var id : Int=0
}

/*
//사진 넣으실 때 ImageView 쓰시면 아래 코드 쓰시면 됩니다!
    fun getFireBaseProfileImage(){ //profile 사진을 ImageView에 설정해주는 함수
        var fileName=obj.category+".png" //채소 아이콘 이미지를 가져오려면 "채소.png"가 되도록

        val storageRef=Firebase.storage.reference.child("icon_img/"+fileName)
        storageRef.downloadUrl.addOnSuccessListener { uri->
            Glide.with(this).load(uri).into(레이아웃에서 ImageView의 id 넣으세요!!)
        }.addOnFailureListener {
            // Handle any errors
        }
    }
 */