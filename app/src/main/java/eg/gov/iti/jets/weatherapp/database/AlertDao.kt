package eg.gov.iti.jets.weatherapp.database

import androidx.room.*
import eg.gov.iti.jets.weatherapp.model.AlertModel
import kotlinx.coroutines.flow.Flow


@Dao
interface AlertDao {

    @Query("select * from alert")
    fun getStoredAlerts(): Flow<List<AlertModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alert: AlertModel): Long

    @Delete
    suspend fun deleteAlert(alert: AlertModel): Int
}