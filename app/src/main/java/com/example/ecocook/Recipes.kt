package com.example.ecocook

class Recipes {
    val category : String?=null //양식, 중식, 한식 등..분류
    val comment : String?=null //음식 설명
    val ingredients : List<String>?=null //재료
    val instruction : List<String>?=null //만드는 방법
    val name : String?=null //음식이름
    val image : String?=null //이미지 url
}

/*
//사진 넣으실 때 ImageView쓰시면 아래 코드 쓰시면 됩니다!
    fun getFireBaseProfileImage(){ //profile 사진을 ImageView에 설정해주는 함수
        var fileName=obj.name+".jpg" //간장파스타의 사진을 가져오려면 "간장파스타.jpg"가 되도록

        val storageRef=Firebase.storage.reference.child("recipe_img/"+fileName)
        storageRef.downloadUrl.addOnSuccessListener { uri->
            Glide.with(this).load(uri).into(레이아웃에서 ImageView의 id 넣으세요!!)
        }.addOnFailureListener {
            // Handle any errors
        }
    }
 */