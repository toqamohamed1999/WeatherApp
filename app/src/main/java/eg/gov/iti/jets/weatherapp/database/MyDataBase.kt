package eg.gov.iti.jets.mymvvm

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import eg.gov.iti.jets.mymvvm.datatbase.WeatherDao
import eg.gov.iti.jets.weatherapp.database.AlertDao
import eg.gov.iti.jets.weatherapp.database.FavouriteDao
import eg.gov.iti.jets.weatherapp.model.AlertModel
import eg.gov.iti.jets.weatherapp.model.DataConverter
import eg.gov.iti.jets.weatherapp.model.Favourite
import eg.gov.iti.jets.weatherapp.model.WeatherRoot


@Database(entities = [WeatherRoot::class,AlertModel::class,Favourite::class], version = 1)
@TypeConverters(DataConverter::class)
abstract class MyDataBase : RoomDatabase() {
    private val TAG = "MyDataBase"

    abstract fun getWeatherDao() :WeatherDao
    abstract fun getAlertDao() :AlertDao
    abstract fun getFavouriteDao() :FavouriteDao


    companion object{
        @Volatile
        private var instance : MyDataBase? = null

        fun getInstance(context : Context):MyDataBase{
            return instance ?: synchronized(this){

                instance = Room.databaseBuilder(context,MyDataBase::class.java,"mydatabase").build()

                instance!!
            }
        }

    }
}
