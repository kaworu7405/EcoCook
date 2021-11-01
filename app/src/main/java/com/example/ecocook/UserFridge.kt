package com.example.ecocook

// 사용자의 냉장고에 저장되는 식재료 정보입니다.
class UserFridge {
    var category : String? = null //음식분류(과일, 채소, ...)
    var name : String? = null //음식명
    var iconId : String? = null //제가 이후에 음식 종류에 따른 이미지 url가르쳐드릴테니 야채인지 과일인지에 따라 그 url을 여기에 넣어주시면 됩니다!
    var buyDate : Int = 20211029 //산 날짜
    var expiryDate : Int = 20211029 //유통기한
    var num : Int = 5 //개수
    var id : Int=0


}

/*
//사진 넣으실 때 ImageView 쓰시면 아래 코드 쓰시면 됩니다!
//근데 아직 icon의 이미지가 없어서 storage에 안올라와있어요..
//icon 이미지 구해서 storage에 저장해놓으면 말씀드리도록 하겠습니다!!
    fun getFireBaseProfileImage(){ //profile 사진을 ImageView에 설정해주는 함수
        var fileName=obj.category+".jpg" //채소 아이콘 이미지를 가져오려면 "채소.jpg"가 되도록

        val storageRef=Firebase.storage.reference.child("icon_img/"+fileName)
        storageRef.downloadUrl.addOnSuccessListener { uri->
            Glide.with(this).load(uri).into(레이아웃에서 ImageView의 id 넣으세요!!)
        }.addOnFailureListener {
            // Handle any errors
        }
    }
 */