package com.example.ecocook

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.activity_auth_location.*
import kotlinx.android.synthetic.main.activity_my_page.*
import java.util.*
import kotlin.collections.ArrayList

class AuthLocationActivity : AppCompatActivity(){

    private val locationManager by lazy {
        getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth_location)

        tedPermission()

        val user= Firebase.auth.currentUser
        val db = Firebase.firestore
        val docRef = db.collection("users").document(user!!.uid)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    if (document.exists()) {
                        var obj = document.toObject<MemberInfo>()
                        if (obj != null) {
                            if(obj.auth=="[인증]")
                            {
                                userLocationText.setText("마지막 인증 위치 : "+obj.address)
                            }
                        }
                    }
                }
            }


        getLocationBtn.setOnClickListener {
            val locationListener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    location?.let {
                        val position = LatLng(it.latitude, it.longitude)
                        val location = getAddress(position)
                        userLocationText.text=location
                        val user= Firebase.auth.currentUser
                        val db = Firebase.firestore

                        val docRef = db.collection("users").document(user!!.uid)
                        docRef.get()
                            .addOnSuccessListener { document ->
                                if (document != null) {
                                    if(document.exists()){
                                        var obj=document.toObject<MemberInfo>()
                                        if (obj != null) {
                                            obj.address=location
                                            obj.auth="[인증]"
                                            docRef.set(obj)
                                            Toast.makeText(baseContext, "인증되었습니다.",
                                                Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                } else {
                                    finish()
                                }
                            }
                            .addOnFailureListener { exception ->
                                //Log.d(TAG, "get failed with ", exception)
                            }

                    }
                }

                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
                override fun onProviderEnabled(provider: String) {}
                override fun onProviderDisabled(provider: String) {}
            }

            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return@setOnClickListener
            }
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                10000,
                1f,
                locationListener
            )
        }
    }

    private fun tedPermission() {
        val permissionListener = object : PermissionListener {
            override fun onPermissionGranted() {}
            override fun onPermissionDenied(deniedPermissions: ArrayList<String>?) {
                Toast.makeText(baseContext, "위치 정보 권한을 설정해주세요.",
                    Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        TedPermission.with(this)
            .setPermissionListener(permissionListener)
            .setRationaleMessage("서비스 사용을 위해서 몇가지 권한이 필요합니다.")
            .setDeniedMessage("[설정] > [권한] 에서 권한을 설정할 수 있습니다.")
            .setPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            .check()
    }

    private fun getAddress(position: LatLng): String? {
        val geoCoder = Geocoder(this, Locale.getDefault())
        val address =
            geoCoder.getFromLocation(position.latitude, position.longitude, 1).first()
                .getAddressLine(0)

        return address
    }
}