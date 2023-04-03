package eg.gov.iti.jets.weatherapp.model

import kotlinx.coroutines.flow.Flow

interface FakeRepoInterface {

    suspend fun getCurrentWeather(
        lat: String,
        lon: String,
        unit: String,
        lang: String
    ): Flow<WeatherRoot>

    fun getStoredWeather(): Flow<WeatherRoot>

    suspend fun insertWeather(weatherRoot: WeatherRoot)

    suspend fun deleteWeather(weatherRoot: WeatherRoot)

    fun getStoredAlerts(): Flow<List<AlertModel>>

    suspend fun insertAlert(alert: AlertModel): Long

    suspend fun deleteAlert(alert: AlertModel)

    fun getStoredFavourites(): Flow<List<Favourite>>

    suspend fun insertFavourite(favorite: Favourite)

    suspend fun deleteFavourite(favorite: Favourite)
}