package eg.gov.iti.jets.mymvvm.datatbase

import android.content.Context
import androidx.lifecycle.LiveData
import eg.gov.iti.jets.mymvvm.MyDataBase
import eg.gov.iti.jets.weatherapp.database.AlertDao
import eg.gov.iti.jets.weatherapp.database.FavouriteDao
import eg.gov.iti.jets.weatherapp.model.AlertModel
import eg.gov.iti.jets.weatherapp.model.Favourite
import eg.gov.iti.jets.weatherapp.model.WeatherRoot
import kotlinx.coroutines.flow.Flow

class LocaleSource(context: Context) : LocaleSourceInterface {

    private val weatherDao : WeatherDao by lazy{
        MyDataBase.getInstance(context)
            .getWeatherDao()
    }

    private val alertDao : AlertDao by lazy{
        MyDataBase.getInstance(context)
            .getAlertDao()
    }

    private val favouriteDao : FavouriteDao by lazy{
        MyDataBase.getInstance(context)
            .getFavouriteDao()
    }

    override fun getStoredWeather(): Flow<WeatherRoot> {
        return weatherDao.getStoredWeather()
    }

    override suspend fun insertWeather(weatherRoot: WeatherRoot) {
        weatherDao.insertWeather(weatherRoot)
    }

    override suspend fun deleteWeather(weatherRoot: WeatherRoot) {
        weatherDao.deleteWeather(weatherRoot)
    }

    override suspend fun deleteAllWeather() {
        weatherDao.deleteAllWeather()
    }

    override fun getStoredAlerts(): Flow<List<AlertModel>> {
        return alertDao.getStoredAlerts()
    }

    override suspend fun insertAlert(alert: AlertModel) : Long{
        return alertDao.insertAlert(alert)
    }

    override suspend fun deleteAlert(alert: AlertModel) {
        alertDao.deleteAlert(alert)
    }

    override fun getStoredFavourites(): Flow<List<Favourite>> {
        return favouriteDao.getStoredFavourites()
    }

    override suspend fun insertFavourite(favorite: Favourite) {
        favouriteDao.insertFavourite(favorite)
    }

    override suspend fun deleteFavourite(favorite: Favourite){
        favouriteDao.deleteFavourite(favorite)
    }
}