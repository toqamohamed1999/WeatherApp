package eg.gov.iti.jets.weatherapp.model


import eg.gov.iti.jets.mymvvm.model.RepoInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeRepo : RepoInterface {

     var favData: MutableList<Favourite> = mutableListOf()
     var alertData: MutableList<AlertModel> = mutableListOf()

    var weatherRoot = WeatherRoot(
         lat = 0.0,
         lon = 0.0,
         timezone = "",
         hourly = emptyList(),
         daily = emptyList(),
         alerts = emptyList(),
         timezoneOffset = ""
     )

    override suspend fun getCurrentWeather(
        lat: String,
        lon: String,
        unit: String,
        lang: String
    ): Flow<WeatherRoot> {
        return flowOf(weatherRoot)
    }

    override fun getStoredWeather(): Flow<WeatherRoot> {
        return flowOf(weatherRoot)
    }

    override suspend fun insertWeather(weatherRoot: WeatherRoot) {
        this.weatherRoot = weatherRoot
    }

    override suspend fun deleteWeather(weatherRoot: WeatherRoot) {
        TODO("Not yet implemented")
    }

    override fun getStoredAlerts(): Flow<List<AlertModel>> {
        return flowOf(alertData)
    }

    override suspend fun insertAlert(alert: AlertModel) : Long {
        alertData.add(alert)
        return 0
    }

    override suspend fun deleteAlert(alert: AlertModel) {
        alertData.remove(alert)
    }

    override fun getStoredFavourites(): Flow<List<Favourite>> {
        return flowOf(favData)
    }

    override suspend fun insertFavourite(favorite: Favourite) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFavourite(favorite: Favourite) {
        favData.remove(favorite)
    }

    override suspend fun deleteAllWeather() {
        TODO("Not yet implemented")
    }
}