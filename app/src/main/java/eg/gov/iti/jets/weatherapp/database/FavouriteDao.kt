package eg.gov.iti.jets.weatherapp.database

import androidx.room.*
import eg.gov.iti.jets.weatherapp.model.Favourite
import kotlinx.coroutines.flow.Flow


@Dao
interface FavouriteDao {

    @Query("select * from favourite")
    fun getStoredFavourites(): Flow<List<Favourite>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavourite(favorite: Favourite): Long

    @Delete
    suspend fun deleteFavourite(favorite: Favourite): Int
}