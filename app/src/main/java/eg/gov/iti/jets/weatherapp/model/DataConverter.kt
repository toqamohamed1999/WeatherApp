package eg.gov.iti.jets.weatherapp.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class DataConverter {

/*
    @TypeConverter
    fun fromStringToWeather(weather:String):WeatherRoot?{
        // pasre string to object

        return weather?.let{
            Gson().fromJson(it, WeatherRoot::class.java)
        }
    }

    @TypeConverter
    fun fromWeatherToString(weather:WeatherRoot):String?{
        // pasre object to string

        return weather?.let{
            Gson().toJson(it)
        }
    }

    @TypeConverter
    fun fromStringToAlert(alert:String):List<Alerts>?{
        // pasre string to object

        return alert?.let{
            Gson().fromJson(it, object : TypeToken<List<Alerts>>() {}.type)
        }
    }

    @TypeConverter
    fun fromAlertToString(alert:List<Alerts>):String?{
        // pasre object to string

        return alert?.let{
            Gson().toJson(it)
        }

    }
*/

    @TypeConverter
    fun fromDailyList(dailyList: List<Daily>): String? {
        if (dailyList == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Daily>>() {}.type
        return gson.toJson(dailyList, type)
    }

    @TypeConverter
    fun toDailyList(dailyString: String?): List<Daily>? {
        if (dailyString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Daily>>() {}.type
        return gson.fromJson(dailyString, type)
    }

    @TypeConverter
    fun fromHourlyList(hourlyList: List<Hourly>): String? {
        if (hourlyList == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Hourly>>() {}.type
        return gson.toJson(hourlyList, type)
    }

    @TypeConverter
    fun toHourlyList(hourlyString: String?): List<Hourly>? {
        if (hourlyString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Hourly>>() {}.type
        return gson.fromJson(hourlyString, type)
    }

    @TypeConverter
    fun fromAlertsList(alertsList: List<Alerts>?): String? {
        if (alertsList == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Alerts>?>() {}.type
        return gson.toJson(alertsList, type)
    }

    @TypeConverter
    fun toAlertsList(alertString: String?): List<Alerts>? {
        if (alertString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<Alerts>>() {}.type
        return gson.fromJson(alertString, type)
    }
}