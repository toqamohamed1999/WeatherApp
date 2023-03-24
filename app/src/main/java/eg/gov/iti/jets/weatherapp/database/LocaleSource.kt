package eg.gov.iti.jets.mymvvm.datatbase

import android.content.Context
import androidx.lifecycle.LiveData
import eg.gov.iti.jets.mymvvm.MyDataBase
import eg.gov.iti.jets.weatherapp.model.WeatherRoot
import kotlinx.coroutines.flow.Flow

class LocaleSource(context: Context) : LocaleSourceInterface {

    private val weatherDao : WeatherDao by lazy{
        MyDataBase.getInstance(context)
            .getWeatherDao()
    }

    override fun getStoredWeather(): Flow<List<WeatherRoot>> {
        return weatherDao.getStoredWeather()
    }

    override suspend fun insertWeather(weatherRoot: WeatherRoot) {
        weatherDao.insertWeather(weatherRoot)
    }

    override suspend fun deleteWeather(weatherRoot: WeatherRoot) {
        weatherDao.deleteWeather(weatherRoot)
    }
}