package eg.gov.iti.jets.mymvvm.model

import eg.gov.iti.jets.mymvvm.datatbase.LocaleSource
import eg.gov.iti.jets.mymvvm.datatbase.LocaleSourceInterface
import eg.gov.iti.jets.mymvvm.network.RemoteSource
import eg.gov.iti.jets.mymvvm.network.RemoteSourceInterface
import eg.gov.iti.jets.weatherapp.model.AlertModel
import eg.gov.iti.jets.weatherapp.model.Favourite
import eg.gov.iti.jets.weatherapp.model.WeatherRoot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class Repo private constructor(
    private var remoteSource: RemoteSourceInterface,
    private var localeSource: LocaleSourceInterface
) : RepoInterface {

    private val TAG = "Repo"

    companion object {
        private var instance: Repo? = null

        fun getInstance(
            remoteSource: RemoteSourceInterface,
            localeSource: LocaleSourceInterface
        ): Repo? {
            return instance ?: synchronized(this) {
                instance = Repo(remoteSource, localeSource)

                instance
            }

        }
    }


    override suspend fun getCurrentWeather(
        lat: String,
        lon: String,
        unit: String,
        lang: String
    ): Flow<WeatherRoot> {
        return flowOf(remoteSource.getCurrentWeather(lat, lon, unit, lang))
    }

    override fun getStoredWeather(): Flow<WeatherRoot> {
        return localeSource.getStoredWeather()
    }

    override suspend fun insertWeather(weatherRoot: WeatherRoot) {
        localeSource.insertWeather(weatherRoot)
    }

    override suspend fun deleteWeather(weatherRoot: WeatherRoot) {
        localeSource.deleteWeather(weatherRoot)
    }

    override suspend fun deleteAllWeather() {
        localeSource.deleteAllWeather()
    }

    override fun getStoredAlerts(): Flow<List<AlertModel>> {
        return localeSource.getStoredAlerts()
    }

    override suspend fun insertAlert(alert: AlertModel) : Long {
        return localeSource.insertAlert(alert)
    }

    override suspend fun deleteAlert(alert: AlertModel) {
        localeSource.deleteAlert(alert)
    }

    override fun getStoredFavourites(): Flow<List<Favourite>> {
        return localeSource.getStoredFavourites()
    }

    override suspend fun insertFavourite(favorite: Favourite) {
        localeSource.insertFavourite(favorite)
    }

    override suspend fun deleteFavourite(favorite: Favourite) {
        localeSource.deleteFavourite(favorite)
    }
}