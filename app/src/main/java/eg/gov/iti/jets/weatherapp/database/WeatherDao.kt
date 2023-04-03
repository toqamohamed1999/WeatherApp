package eg.gov.iti.jets.mymvvm.datatbase

import androidx.room.*
import eg.gov.iti.jets.weatherapp.model.WeatherRoot
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Query("select * from weatherRoot")
    fun getStoredWeather (): Flow<WeatherRoot>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weatherRoot: WeatherRoot): Long

    @Delete
    suspend fun deleteWeather(weatherRoot: WeatherRoot): Int

    @Query("DELETE FROM weatherRoot")
    suspend fun deleteAllWeather()
}