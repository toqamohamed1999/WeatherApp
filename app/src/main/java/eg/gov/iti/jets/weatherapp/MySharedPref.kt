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

    private var pref : SharedPreferences = context!!.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    val editor = pref.edit()

    init {
        writeLanguage(Language.English)
        writeLocation(Location.GPS)
        writeTemperature(Temperature.Celsius)
        writeWindSpeed(WindSpeed.Meter)
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

    fun getSetting(): Setting {
        return Setting(Language.valueOf(pref.getString(LANGUAGE, Language.English.name)!!),
            Location.valueOf(pref.getString(LOCATION, Location.GPS.name)!!),
            Temperature.valueOf(pref.getString(TEMPERATURE, Temperature.Celsius.name)!!),
            WindSpeed.valueOf(pref.getString(WINDSPEED, WindSpeed.Meter.name)!!))
    }


}