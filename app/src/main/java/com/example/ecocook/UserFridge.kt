package com.example.ecocook

// 사용자의 냉장고에 저장되는 식재료 정보입니다.
class UserFridge {
    var category : String? = null //음식분류(과일, 채소, ...)
    var name : String? = null //음식명
    var iconId : String? = null //제가 이후에 음식 종류에 따른 이미지 url가르쳐드릴테니 야채인지 과일인지에 따라 그 url을 여기에 넣어주시면 됩니다!
    var buyDate : Int = 20211029 //산 날짜
    var expiryDate : Int = 20211029 //유통기한
    var num : Int = 5 //개수
}