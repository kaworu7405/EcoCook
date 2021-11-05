package com.example.ecocook

data class MemberInfo(
    val name:String?=null,
    val phoneNumber:String?=null,
    val birthDay:String?=null,
    val address:String?=null,
    val myRecipes: ArrayList<String>?=null
)