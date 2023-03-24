package eg.gov.iti.jets.mymvvm.network

import android.util.Log
import eg.gov.iti.jets.mymvvm.APIClient
import eg.gov.iti.jets.weatherapp.model.WeatherRoot

class RemoteSource :RemoteSourceInterface{
    private val TAG = "RemoteSource"

    override suspend fun getCurrentWeather(
        lat: String,
        lon: String,
        units: String,
        lang: String
    ): WeatherRoot {
        Log.i(TAG, "getCurrentWeather: "+APIClient.apiInterface.getCurrentWeather(lat, lon, units, lang))
        return APIClient.apiInterface.getCurrentWeather(lat, lon, units, lang)
    }
}