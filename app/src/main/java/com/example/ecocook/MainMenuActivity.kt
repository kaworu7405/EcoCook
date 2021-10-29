package com.example.ecocook

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ecocook.manage_fridge.MyFridge
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_mainmenu.*

class MainMenuActivity : AppCompatActivity(), View.OnClickListener {
    var backKeyPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mainmenu)


        val user = Firebase.auth.currentUser

        if (user != null) { //null이 아니면
            val db = Firebase.firestore
            val docRef = db.collection("users").document(user.uid)
            docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        if (document.exists()) {
                            var str = document.getData()?.get("name").toString() + "님 환영합니다!"
                            Toast.makeText(
                                baseContext, str,
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            val nextIntent = Intent(this, MemberInitActivity::class.java)
                            startActivity(nextIntent)
                        }
                    } else {
                        val nextIntent = Intent(this, MemberInitActivity::class.java)
                        startActivity(nextIntent)
                    }
                }
                .addOnFailureListener { exception ->
                    //Log.d(TAG, "get failed with ", exception)
                }


        } else { //user가 null이면 login 화면으로
            startActivity(Intent(this, LoginActivity::class.java))
        }

        TempLogoutButton.setOnClickListener { //로그아웃 버튼
            FirebaseAuth.getInstance().signOut() //로그아웃
            startActivity(Intent(this, LoginActivity::class.java))//Login Activity로 이동
        }

        //레시피 DB TEST할 수 있는 버튼!
        //DB에 저장되어있는 레시피를 꺼내올 수 있음
        DbTestButton.setOnClickListener {
            val db = Firebase.firestore

            val recipesRef = db.collection("recipes")
            db.collection("recipes")
                .addSnapshotListener { querySnapshot: QuerySnapshot?, e: FirebaseFirestoreException? ->
                    if (querySnapshot != null) {
                        // 반복문으로 모든 레시피에 접근합니다.(document가 레시피)
                        // Recipes.kt 클래스에 보시면 각 필드 설명적혀있습니다
                        for (document in querySnapshot.documents) {
                            //obj로 각 필드에 접근할 수 있습니다 지금은 db에 간장 파스타밖에 없어서ㅠㅠ..
                            //반복문을 돌려봤자 지금은 간장 파스타만 obj에 저장됩니다.
                            val obj = document.toObject<Recipes>()
                            if (obj != null) {
                                //category 출력하기(양식인지 중식인지 등..)
                                obj.category?.let { it1 ->
                                    Log.d("TAG", it1)
                                }

                                //재료 차례대로 출력하기
                                for (i in obj.ingredients!!) {
                                    Log.d("TAG", i)
                                }
                                // 요리 하는 방법 출력하기
                                for (i in obj.instruction!!) {
                                    Log.d("TAG", i)
                                }
                            }
                        }
                    }
                }
        }

        //냉장고 DB TEST할 수 있는 버튼!
        DbTestButton2.setOnClickListener {
            val db = Firebase.firestore
            val f = user?.let { it1 -> db.collection(it1.uid) }
            /*
            //UserFridge.kt의 class를 object화 하여 db에 저장해줍니다.
            ////////////////////////////////////////////////////////////////////
            //유저의 냉장고에 새로운 음식을 추가하는 코드입니다.
            val data= hashMapOf(
                "category" to "과일",
                "name" to "메론",
                //iconId 설명 : 제가 이후에 음식 종류에 따른 이미지 url가르쳐드릴테니 야채인지 과일인지에 따라 그 url을 여기에 넣어주시면 됩니다!
                "iconId" to "null",
                "buyDate" to 20211029,
                "expiryDate" to 20211105,
                "num" to 7
            )
            //document의 인자로는 음식명을 해주시면 될 것 같습니다!
            //음식명은 중복되어서는 안됩니다! DB에서보면 컬럼이랑 같다고 보셔도 될것같습니다!
            //사용자의 냉장고에 저장되어있는 음식들 이름을 본 뒤 같은 이름이 저장되어있으면 경고문을 띄우고 "이미 존재하는 음식이다" 이런식으로 알리면 좋을 것 같습니다!
            f?.document("메론")?.set(data)
            /////////////////////////////////////////////////////////////////////
             */


            /////////////////////////////////////////////////////////////////////
            // 유저의 냉장고에 있는 음식을 수정하는 코드입니다.
            // 메론의 개수를 5개로 변경하고 expiryDate를 20211108로 변경 싶을 때 사용할 수 있습니다.
            /*
            if (f != null) {
                f.document("메론").update("num", 5)
                f.document("메론").update("expiryDate", 20211108)
            }
            */
            ////////////////////////////////////////////////////////////////////


            /*
            ////////////////////////////////////////////////////////////////////
            //유저의 냉장고에 있는 음식을 삭제하는 코드입니다
            if (f != null) {
                f.document("메론").delete().addOnSuccessListener{
                    //삭제가 잘 된 경우 코드
                    Log.d("TAG", "DocumentSnapshot successfully deleted!")
                }.addOnFailureListener{
                    //삭제가 안 된 경우 코드
                        e -> Log.w("TAG", "Error deleting document", e)
                }
            }
            /////////////////////////////////////////////////////////////////
             */

            /////////////////////////////////////////////////////////////////////
            //유저의 냉장고에 있는 음식을 가져오는 코드입니다.
            if (f != null) //만약에 null이면 아직 사용자가 저장한 음식이 하나도 없다는 의미입니다!
            {
                f.addSnapshotListener { querySnapshot: QuerySnapshot?, e: FirebaseFirestoreException? ->
                    if (querySnapshot != null) {
                        // 반복문으로 모든 음식에 접근합니다.(document가 음식)
                        // UserFridge.kt에 각 필드 설명 적혀있습니다!
                        for (document in querySnapshot.documents) {
                            val obj = document.toObject<UserFridge>()
                            if (obj != null) {
                                Log.d("TAG", "이 음식은 "+obj.category.toString())
                                Log.d("TAG", obj.name.toString()+"입니다")
                                Log.d("TAG", obj.expiryDate.toString()+"까지 먹어야돼요!!")
                            }
                        }
                    }
                }
            }
            else {//null이므로 아직 사용자가 저장한 음식이 하나도 없다는 의미입니다!
                Log.d("TAG", "아직 유저가 저장한 음식 없음~!!")
            }
        }
        ///////////////////////////////////////////////////////////////////////

        MyPageButton.setOnClickListener {
            startActivity(Intent(this, MyPageActivity::class.java))
        }
        ManageFridgeButton.setOnClickListener(this)         //냉장고 관리로 클릭
    }

    override fun onBackPressed() {                          //이거 뒤로가기 누르는거 연속으로 눌러야 꺼지거나 그런건가?
        if (System.currentTimeMillis() - backKeyPressedTime < 2000) {
            finishAffinity()
        }
        backKeyPressedTime = System.currentTimeMillis()
    }

    fun manageFridge() {             //냉장고 관리로 넘어감
        startActivity(Intent(this, MyFridge::class.java))
        finish()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ManageFridgeButton -> {    //냉장고 관리 버튼을 눌렀을 때
                manageFridge()
            }
        }
    }
}