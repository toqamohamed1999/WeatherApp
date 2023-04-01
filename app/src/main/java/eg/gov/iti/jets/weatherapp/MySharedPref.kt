package eg.gov.iti.jets.weatherapp

import android.content.Context
import android.content.SharedPreferences
import android.location.Address
import eg.gov.iti.jets.weatherapp.model.*

class MySharedPref private constructor(var context : Context) {

    private val PREF_NAME = "SettingPref"
    private val  LANGUAGE= "language"
    private val LOCATION = "location"
    private val TEMPERATURE = "temperature"
    private val WINDSPEED = "windSpeed"
    private val IS_FIRST = "is_First"
    private val NOTIFICATION = "notification"
    private val LAT = "lat"
    private val LON = "lon"
    private val ADDRESS = "address"


    private var pref : SharedPreferences = context!!.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val editor = pref.edit()

    companion object{

        private var mySharedPref: MySharedPref? = null
         fun getMyPref (context: Context): MySharedPref {
             return if(mySharedPref == null)
                 MySharedPref(context)
             else mySharedPref!!
        }
    }


    fun writeIsFirst(isFirst : Boolean) {
        editor.putBoolean(IS_FIRST, isFirst)
        editor.commit()
    }

    fun writeLanguage(language: Language) {
        editor.putString(LANGUAGE, language.name)
        editor.commit()
    }

    fun writeTemperature(temperature: Temperature) {
        editor.putString(TEMPERATURE, temperature.name)
        editor.commit()
    }
    fun writeWindSpeed(windSpeed: WindSpeed) {
        editor.putString(WINDSPEED, windSpeed.name)
        editor.commit()
    }
    fun writeLocation(location: Location) {
        editor.putString(LOCATION, location.name)
        editor.commit()
    }

    fun writeNotification(notification: Notification) {
        editor.putString(NOTIFICATION, notification.name)
        editor.commit()
    }

    fun readISFIRST() : Boolean {
        return pref.getBoolean(IS_FIRST, true)!!
    }

    fun readLat() : String {
        return pref.getString(LAT, "N/F")!!
    }

    fun readLon() : String {
        return pref.getString(LON, "N/F")!!
    }

    fun readAddress() : String {
        return pref.getString(ADDRESS, "N/F")!!
    }


    fun writeLat(lat : String)  {
        editor.putString(LAT, lat)
        editor.commit()
    }

    fun writeLon(lon : String)  {
        editor.putString(LON, lon)
        editor.commit()
    }

    fun writeAddress(address : String)  {
        editor.putString(ADDRESS, address)
        editor.commit()
    }



    fun getSetting(): Setting {
        return Setting(Language.valueOf(pref.getString(LANGUAGE, Language.English.name)!!),
            Location.valueOf(pref.getString(LOCATION, Location.GPS.name)!!),
            Temperature.valueOf(pref.getString(TEMPERATURE, Temperature.Celsius.name)!!),
            WindSpeed.valueOf(pref.getString(WINDSPEED, WindSpeed.Meter.name)!!),
            Notification.valueOf(pref.getString(NOTIFICATION, Notification.Enable.name)!!))
    }


}