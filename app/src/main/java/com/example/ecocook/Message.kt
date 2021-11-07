package com.example.ecocook

import java.io.Serializable

class Message(
    var id : String?=null,
    var message:List<Map<String, String>>?=null,
    val user1 : String?=null,
    val user2 : String?=null
) :Serializable {

}