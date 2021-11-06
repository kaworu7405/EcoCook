package com.example.ecocook
import java.io.Serializable
import android.widget.ArrayAdapter

class Posting (
    var userId : String?=null, //uid
    var id : String?=null, //게시글 id
    var postingTitle : String?=null, //게시글 제목
    var postingContent : String?=null, //게시글 내용
    val imgUrl : String?=null, //이미지 url 링크 주소
    var comments : List<Map<String, String>>?=null, //댓글
    var auth : String?=null, //인증회원?
    var buyDate : String?=null,
    var expiryDate : String?=null,
    val price:String?=null,
    val area1:String?=null,
    val area2:String?=null
) : Serializable {
}