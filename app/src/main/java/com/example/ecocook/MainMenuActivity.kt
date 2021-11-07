package com.example.ecocook

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ecocook.manage_fridge.MyFridge
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_mainmenu.*

class MainMenuActivity : AppCompatActivity(), View.OnClickListener {
    var backKeyPressedTime: Long = 0
    val foodArr = ArrayList<UserFridge>()

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

        functionButton.setOnClickListener { //설정화면 가기 버튼
            startActivity(Intent(this, functionActivity::class.java))

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
                            }
                        }
                    }
                }
        }

        //냉장고 DB TEST할 수 있는 버튼!
        DbTestButton2.setOnClickListener {
            val db = Firebase.firestore
            val f = db.collection(user?.uid.toString())
            /////////////////////////////////////////////////////////////////////
            // 유저의 냉장고에 있는 음식을 수정하는 코드입니다.
            // 메론의 개수를 5개로 변경하고 expiryDate를 20211108로 변경 싶을 때 사용할 수 있습니다.
            /*
            if (f != null) {
                f.document("음식id").update("num", 5)
                f.document("음식id").update("expiryDate", 20211108)
            }
            */
            ////////////////////////////////////////////////////////////////////

/*
            ////////////////////////////////////////////////////////////////////
            //유저의 냉장고에 있는 음식을 삭제하는 코드입니다
            val deleteId=3 //여기에 삭제시킬 id 입력하시면 됩니다!
            f.orderBy("id").get()
                .addOnSuccessListener { result ->
                    var foodNum=result.size()-1
                    for(food in result){
                        val obj = food.toObject<UserFridge>()

                        Log.d("TAG", obj.id.toString())

                        if (obj != null && obj.id > deleteId) {
                            val newObjId=obj.id-1
                            obj.id = newObjId
                            f.document(newObjId.toString()).set(obj)
                            if(foodNum==obj.id){
                                f.document((obj.id+1).toString()).delete()
                            }
                        }
                    }
                    if(deleteId==result.size()){
                        f.document(deleteId.toString()).delete()
                    }
                }
                .addOnFailureListener { exception ->
                }
                //////////////////////////////////////////////


 */
            /////////////////////////////////////////////////////////////////////
            //유저의 냉장고에 있는 음식을 가져오는 코드입니다.
/*
            f.orderBy("id").addSnapshotListener { querySnapshot: QuerySnapshot?, _: FirebaseFirestoreException? ->
                if (querySnapshot != null) {
                    // 반복문으로 모든 음식에 접근합니다.(document가 음식)
                    // UserFridge.kt에 각 필드 설명 적혀있습니다!
                    for (document in querySnapshot.documents) {
                        val obj = document.toObject<UserFridge>()
                        if (obj != null) {
                            Log.d("TAG", "이 음식은 " + obj.category.toString())
                            Log.d("TAG", obj.name.toString() + "입니다")
                            Log.d("TAG", obj.expiryDate.toString() + "까지 먹어야돼요!!")
                        }
                    }
                }
            }
 */

            /////////////////////////////////////////////////////////////////////////

/*
            //UserFridge.kt의 class를 object화 하여 db에 저장해줍니다.
            ////////////////////////////////////////////////////////////////////
            //유저의 냉장고에 새로운 음식을 추가하는 코드입니다.
            f.get()
                .addOnSuccessListener { result ->
                    var foodNum = result.size() + 1 //현재 냉장고에 저장되어있는 음식의 수에다가 1 더해준 값
                    val data = hashMapOf(
                        "category" to "과일",
                        "name" to "메론",
                        //iconId 설명 : 제가 이후에 음식 종류에 따른 이미지 url가르쳐드릴테니 야채인지 과일인지에 따라 그 url을 여기에 넣어주시면 됩니다!
                        "iconId" to "null",
                        "buyDate" to 20211029,
                        "expiryDate" to 20211105,
                        "num" to 7,
                        "id" to foodNum
                    )
                    f.document(foodNum.toString())
                        .set(data) // 현재 냉장고에 저장되어있는 음식의 수에다가 1더해준 값을 문서의 이름과 id로 정해준다.
                    Log.d("TAG", foodNum.toString())
                }
                .addOnFailureListener { exception ->
                }
            /////////////////////////////////////////////////////////////////////



 */





        }
        ///////////////////////////////////////////////////////////////////////

        MyPageButton.setOnClickListener {
            startActivity(Intent(this, MyPageActivity::class.java))
        }
        ManageFridgeButton.setOnClickListener(this)         //냉장고 관리로 클릭

        GroceriesButton.setOnClickListener {
            startActivity(Intent(this, PostingActivity::class.java))
        }

        //좋아요한 DB TEST BUTTON!
        DbTestButton3.setOnClickListener {
            if (user != null) { //null이 아니면
                val db = Firebase.firestore
                val docRef = db.collection("users").document(user.uid)
                docRef.get().addOnSuccessListener { documentSnapshot ->
                    val obj = documentSnapshot.toObject<MemberInfo>()

                    //사용자가 좋아요한 recipes가져오기
                    //split 함수는 ]기준으로 문자열 잘라서 배열에 저장하는 함수
                    //즉 array[0]에는 레시피이름, array[1]에는 url
                    val array = obj?.myRecipes?.get(0)?.split("]")
                    val recipeName = array?.get(0) //recipeName에는 레시피이름(예로들면 "간장파스타")가 저장됨
                    if (obj != null) {
                        Log.d("TAG", obj.name.toString())
                    }

                    /*
                    //myRecipes 배열 0번째에 '간장파스타'가 저장되어있고 이걸 삭제하려고 한다면?
                    //삭제하는 코드!
                    if (obj != null) {
                        obj.myRecipes?.removeAt(0)
                        docRef.set(obj)
                    }
                    */

                    //사용자가 recipe를 좋아요해서 좋아요한 레시피를 추가한다면
                    if (obj != null) {
                        if (obj.myRecipes==null) {
                            var arr=ArrayList<String>()
                            arr.add("간장파스타]https://www.10000recipe.com/recipe/6762181")
                            obj.myRecipes=arr
                            docRef.set(obj)
                        }
                        else{
                            obj.myRecipes!!.add("간장파스타]https://www.10000recipe.com/recipe/6762181")
                            docRef.set(obj)
                        }
                    }


                }
            }
        }
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