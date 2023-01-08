package com.example.weatherapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent

import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices


class splashScreen : AppCompatActivity() {
    lateinit var mFusedLocation:FusedLocationProviderClient
    private var myRequestCode = 1010
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)


        mFusedLocation = LocationServices.getFusedLocationProviderClient(this)

        getLastLocation()

    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if(checkPermisson()){
            if (LocationEnable()) {
                mFusedLocation.lastLocation.addOnCompleteListener { task ->
                    var location : Location? = task.result

                    if (location == null) {
                        NewLocation()
                    } else {
                         Handler(Looper.getMainLooper()).postDelayed({
                           var intent = Intent(this,MainActivity::class.java)
                             intent.putExtra("lat",location.latitude.toString())
                             intent.putExtra("long",location.longitude.toString())
                             startActivity(intent)
                             finish()

                         },2000)

                    }

                }

            }else
            {
                Toast.makeText(this,"Please Enable Mobile Location",Toast.LENGTH_LONG).show()
            }

        }else{
            RequestPermission()
        }
    }

    @SuppressLint("MissingPermission")
    private fun NewLocation() {

        var locationRequest = com.google.android.gms.location.LocationRequest()
        locationRequest.priority = com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1
        mFusedLocation = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocation.requestLocationUpdates(locationRequest,locationCallBack, Looper.myLooper())



    }

    private  val locationCallBack = object:LocationCallback(){

        override fun onLocationResult(p0 : LocationResult) {
           var lastLocation: Location? = p0.lastLocation
        }
    }

    private fun RequestPermission() {

        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION),myRequestCode)

    }

    private fun LocationEnable(): Boolean {

        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

    }

    private fun checkPermisson(): Boolean {

       if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED ||
               ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
       )
       {
           return true
       }
        return false

    }

    override fun onRequestPermissionsResult(
        requestCode : Int ,
        permissions : Array<out String> ,
        grantResults : IntArray
    ) {
        super.onRequestPermissionsResult(requestCode , permissions , grantResults)
        if(requestCode == myRequestCode){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                    getLastLocation()
            }
        }
    }
}