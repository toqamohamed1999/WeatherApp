package eg.gov.iti.jets.mymvvm.model

import eg.gov.iti.jets.weatherapp.model.WeatherRoot
import kotlinx.coroutines.flow.Flow


interface RepoInterface {

    suspend fun getCurrentWeather() : Flow<WeatherRoot>

    fun getStoredWeather (): Flow<List<WeatherRoot>>

    suspend fun insertWeather(weatherRoot: WeatherRoot)

    suspend fun deleteWeather(weatherRoot: WeatherRoot)
}