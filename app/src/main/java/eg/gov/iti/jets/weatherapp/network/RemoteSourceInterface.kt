package eg.gov.iti.jets.mymvvm.network

import eg.gov.iti.jets.weatherapp.model.WeatherRoot

interface RemoteSourceInterface {

     suspend fun getCurrentWeather(lat: String,
                                lon: String,
                                units: String,
                                lang: String): WeatherRoot

}