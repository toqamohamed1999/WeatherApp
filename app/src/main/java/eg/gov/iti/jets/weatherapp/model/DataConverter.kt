package eg.gov.iti.jets.weatherapp.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class DataConverter {
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
    fun fromStringList(stringList: List<String>): String? {
        if (stringList == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<String>>() {}.type
        return gson.toJson(stringList, type)
    }

    @TypeConverter
    fun toStringList(string: String?): List<String>? {
        if (string == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(string, type)
    }
}