package eg.gov.iti.jets.mymvvm

import eg.gov.iti.jets.weatherapp.API_KEY
import eg.gov.iti.jets.weatherapp.model.WeatherRoot
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("onecall")
    suspend fun getCurrentWeather(@Query("lat") lat: String?,
                                  @Query("lon") lon: String?,
                                  @Query("units") units: String = "metric",
                                  @Query("lang") lang: String? = "eng",
                                  @Query("exclude") exclude: String="current",
                                  @Query("appid") appId: String=API_KEY): WeatherRoot

}
