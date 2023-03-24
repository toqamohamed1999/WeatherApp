package eg.gov.iti.jets.mymvvm.datatbase

import eg.gov.iti.jets.weatherapp.model.WeatherRoot
import kotlinx.coroutines.flow.Flow

interface LocaleSourceInterface {

    fun getStoredWeather() : Flow<List<WeatherRoot>>

    suspend fun insertWeather(weatherRoot: WeatherRoot)

    suspend fun deleteWeather(weatherRoot: WeatherRoot)
}