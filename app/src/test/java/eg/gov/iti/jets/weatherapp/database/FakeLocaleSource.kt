package eg.gov.iti.jets.weatherapp.database

import eg.gov.iti.jets.mymvvm.datatbase.LocaleSourceInterface
import eg.gov.iti.jets.weatherapp.model.AlertModel
import eg.gov.iti.jets.weatherapp.model.Favourite
import eg.gov.iti.jets.weatherapp.model.WeatherRoot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeLocaleSource(private val favList: MutableList<Favourite> = mutableListOf()) :
    LocaleSourceInterface {


    override fun getStoredFavourites(): Flow<List<Favourite>> {
        return flowOf(favList)
    }

    override suspend fun insertFavourite(favorite: Favourite) {
        favList.add(favorite)
    }

    override suspend fun deleteFavourite(favorite: Favourite) {
        favList.remove(favorite)
    }

    override suspend fun deleteAllWeather() {
        TODO("Not yet implemented")
    }

    override fun getStoredWeather(): Flow<WeatherRoot> {
        TODO("Not yet implemented")
    }

    override suspend fun insertWeather(weatherRoot: WeatherRoot) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteWeather(weatherRoot: WeatherRoot) {
        TODO("Not yet implemented")
    }

    override fun getStoredAlerts(): Flow<List<AlertModel>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertAlert(alert: AlertModel): Long {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAlert(alert: AlertModel) {
        TODO("Not yet implemented")
    }


}