package com.example.ecocook

data class MemberInfo(
    val name:String?=null,
    val phoneNumber:String?=null,
    val birthDay:String?=null,
    var address:String?=null,
    var myRecipes: ArrayList<String>?=null,
    var auth : String?=null
)