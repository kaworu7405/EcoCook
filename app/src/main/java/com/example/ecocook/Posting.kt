package com.example.ecocook
import java.io.Serializable
import android.widget.ArrayAdapter

class Posting (
    val userId : String?=null, //uid
    val id : String?=null, //게시글 id
    val postingTitle : String?=null, //게시글 제목
    val postingContent : String?=null, //게시글 내용
    val createAt : String?=null, //글 작성, 수정 시간
    val imgUrl : String?=null, //이미지 url 링크 주소
    val comments : List<Map<String, String>>?=null, //댓글
    val auth : String?=null, //인증회원?
    val buyDate : String?=null,
    val expiryDate : String?=null
) : Serializable {
}
