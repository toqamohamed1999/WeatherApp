package eg.gov.iti.jets.weatherapp

import android.content.Context
import android.content.SharedPreferences
import eg.gov.iti.jets.weatherapp.model.*

class MySharedPref(var context : Context) {

    private val PREF_NAME = "SettingPref"
    private val  LANGUAGE= "language"
    private val LOCATION = "location"
    private val TEMPERATURE = "temperature"
    private val WINDSPEED = "windSpeed"
    private val IS_FIRST = "is_First"
    private val NOTIFICATION = "notification"



    private var pref : SharedPreferences = context!!.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val editor = pref.edit()


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

    fun getSetting(): Setting {
        return Setting(Language.valueOf(pref.getString(LANGUAGE, Language.English.name)!!),
            Location.valueOf(pref.getString(LOCATION, Location.GPS.name)!!),
            Temperature.valueOf(pref.getString(TEMPERATURE, Temperature.Celsius.name)!!),
            WindSpeed.valueOf(pref.getString(WINDSPEED, WindSpeed.Meter.name)!!))
    }


}