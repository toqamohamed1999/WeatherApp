package eg.gov.iti.jets.weatherapp.model

import androidx.room.Entity
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


@Entity(tableName = "weatherRoot", primaryKeys = ["timezone"])
data class WeatherRoot (
    val lat: Double,
    val lon: Double,
    val timezone: String,
    @SerializedName("timezone_offset")
    val timezoneOffset: String,
    val hourly: List<Hourly>,
    val daily: List<Daily>,
  //  val alerts: List<Alerts>
)

data class Hourly(
    val dt: Long,
    val temp: String,
    @SerializedName("feels_like")
    val feelsLike: String,
    val pressure: String,
    val humidity: String,
    @SerializedName("dew_point")
    val dewPoint: String,
    val uvi: String,
    val clouds: String,
    val visibility: String,
    @SerializedName("wind_speed")
    val windSpeed: String,
    @SerializedName("wind_deg")
    val windDeg: String,
    @SerializedName("wind_gust")
    val windGust: String,
    val weather: List<Weather>,
    val pop: String,
)

data class Weather(
    val id: Long,
    val main: String,
    val description: String,
    val icon: String,
)
data class Daily (
    val dt: Long,
    val sunrise: String,
    val sunset: String,
    val moonrise: String,
    @SerializedName("moonset")
    val moonSet: String,
    @SerializedName("moon_phase")
    val moonPhase: String,
    val temp: Temp,
    @SerializedName("feels_like")
    val feelsLike: FeelsLike,
    val pressure: String,
    val humidity: String,
    @SerializedName("dew_point")
    val dewPoint: String,
    @SerializedName("wind_speed")
    val windSpeed: String,
    @SerializedName("wind_deg")
    val windDeg: String,
    @SerializedName("wind_gust")
    val windGust: String,
    val weather: List<Weather>,
    val clouds: String,
    val pop: String,
    val rain: String,
    val uvi: String,
)

data class Temp(
    val day: String,
    val min: String,
    val max: String,
    val night: String,
    val eve: String,
    val morn: String,
)

data class FeelsLike(
    val day: String,
    val night: String,
    val eve: String,
    val morn: String,
)

data class Alerts(
    @SerializedName("sender_name")
    var senderName: String,
    var event: String,
    var start: Long,
    var end: Long,
    var description: String,
   // var tags: List<String>
)


enum class Language{English,Arabic}
enum class Temperature{Celsius,Kelvin,Fahrenheit}
enum class Location{GPS,Map}
enum class WindSpeed{Mile,Meter}
enum class Notification{Enable,Disable}

class Setting(val language: Language,
                   val location: Location,
                   val temperature: Temperature,
                   val windSpeed: WindSpeed){}



