package eg.gov.iti.jets.mymvvm.datatbase

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import eg.gov.iti.jets.weatherapp.model.AlertModel
import eg.gov.iti.jets.weatherapp.model.Favourite
import eg.gov.iti.jets.weatherapp.model.WeatherRoot
import kotlinx.coroutines.flow.Flow

interface LocaleSourceInterface {

    fun getStoredWeather() : Flow<WeatherRoot>

    suspend fun insertWeather(weatherRoot: WeatherRoot)

    suspend fun deleteWeather(weatherRoot: WeatherRoot)

    fun getStoredAlerts(): Flow<List<AlertModel>>

    suspend fun insertAlert(alert: AlertModel) : Long

    suspend fun deleteAlert(alert: AlertModel)

    fun getStoredFavourites(): Flow<List<Favourite>>

    suspend fun insertFavourite(favorite: Favourite)

    suspend fun deleteFavourite(favorite: Favourite)

    suspend fun deleteAllWeather()
}