package eg.gov.iti.jets.weatherapp.location

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import eg.gov.iti.jets.weatherapp.LOCATION_PERMISSION_CODE

class MyLocation(private val activity: Activity) {

    private val TAG = "MyLocation"

    private lateinit var fusedLocationProviderClient : FusedLocationProviderClient
    private lateinit var latitude : String
    private lateinit var longitude : String

     private fun checkPermission():Boolean{
        return ActivityCompat.checkSelfPermission(
            activity,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

     private fun isLocationEnabled():Boolean{
        val locationManager : LocationManager =
            activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun requestPermission(){

        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ), LOCATION_PERMISSION_CODE
        )
    }


    @SuppressLint("MissingPermission")
    private fun requestLocationData(){
        val locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            myLocationCallback,
            Looper.myLooper()
        )
    }

    private val myLocationCallback : LocationCallback = object : LocationCallback(){

        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            val lastLocation = locationResult.lastLocation
            latitude = lastLocation?.latitude.toString()
            longitude = lastLocation?.longitude.toString()
            Log.i(TAG, "onLocationResult: $latitude  $longitude")
        }
    }

    @SuppressLint("MissingPermission")
    fun getLastLocation() {
        if(checkPermission()){
            if(isLocationEnabled()){

                requestLocationData()

            }else{
                Toast.makeText(activity,"Please, enable your location", Toast.LENGTH_LONG).show()
                activity.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
        }else{
            requestPermission()
        }
    }

}