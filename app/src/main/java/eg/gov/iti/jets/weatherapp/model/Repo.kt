package eg.gov.iti.jets.mymvvm.model

import android.content.Context
import androidx.lifecycle.LiveData
import eg.gov.iti.jets.mymvvm.datatbase.LocaleSource
import eg.gov.iti.jets.mymvvm.network.RemoteSource
import eg.gov.iti.jets.mymvvm.network.RemoteSourceInterface
import eg.gov.iti.jets.weatherapp.model.WeatherRoot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class Repo private constructor(
    private var remoteSource: RemoteSource,
    private var localeSource: LocaleSource) : RepoInterface {


    companion object{
        private var instance : Repo? = null

        fun getInstance(remoteSource: RemoteSource, localeSource: LocaleSource) : Repo? {
            return instance ?: synchronized(this){
                instance = Repo(remoteSource,localeSource)

                instance
            }

        }
    }


    override suspend fun getCurrentWeather() : Flow<WeatherRoot> {
        return flowOf(remoteSource.getCurrentWeather("31.6421192", "31.0657133", "metric", "eng"))
    }

    override fun getStoredWeather(): Flow<List<WeatherRoot>> {
        return localeSource.getStoredWeather()
    }

    override suspend fun insertWeather(weatherRoot: WeatherRoot) {
        localeSource.insertWeather(weatherRoot)
    }

    override suspend fun deleteWeather(weatherRoot: WeatherRoot){
        localeSource.deleteWeather(weatherRoot)
    }
}