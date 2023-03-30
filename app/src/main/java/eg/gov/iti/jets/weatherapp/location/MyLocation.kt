package eg.gov.iti.jets.weatherapp.location

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import eg.gov.iti.jets.weatherapp.LOCATION_PERMISSION_CODE
import java.util.*
import java.util.concurrent.TimeUnit

class MyLocation(private val activity: Activity,private val locationListener: eg.gov.iti.jets.weatherapp.home.view.LocationListener) {

    private val TAG = "MyLocation"

    private lateinit var fusedLocationProviderClient : FusedLocationProviderClient
    private lateinit var latitude : String
    private lateinit var longitude : String
    private lateinit var address : String


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
        locationRequest.numUpdates = 1

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


            Log.i(TAG, "onLocationResult: ttt$latitude  $longitude")
            locationListener.setLocation(Pair(latitude,longitude))
        }
    }

    @SuppressLint("MissingPermission")
    fun getLastLocation(){
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

//    private fun getAddress(){
//        val geocoder = Geocoder(activity, Locale.getDefault())
//
//        val addresses: List<Address>? = geocoder.getFromLocation(
//            latitude.toDouble(),
//            longitude.toDouble(),
//            1
//        )
//        address = addresses!![0].getAddressLine(0)
//
////        val city: String = addresses!![0].locality
////        val state: String = addresses!![0].adminArea
////        val country: String = addresses!![0].countryName
////        val postalCode: String = addresses!![0].postalCode
////        val knownName: String = addresses!![0].featureName
//
//    }
}