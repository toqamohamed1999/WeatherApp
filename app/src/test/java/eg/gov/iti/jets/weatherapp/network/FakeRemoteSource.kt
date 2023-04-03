package eg.gov.iti.jets.weatherapp.network

import eg.gov.iti.jets.mymvvm.network.RemoteSourceInterface
import eg.gov.iti.jets.weatherapp.model.WeatherRoot

class FakeRemoteSource(private val weatherRoot: WeatherRoot) : RemoteSourceInterface {

    override suspend fun getCurrentWeather(
        lat: String,
        lon: String,
        units: String,
        lang: String
    ): WeatherRoot {
        return weatherRoot
    }
}