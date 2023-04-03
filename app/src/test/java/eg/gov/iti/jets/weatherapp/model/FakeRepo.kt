package eg.gov.iti.jets.weatherapp.model


import eg.gov.iti.jets.mymvvm.model.RepoInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeRepo : RepoInterface {

    private var weatherData: MutableList<WeatherRoot> = mutableListOf()


    override suspend fun getCurrentWeather(
        lat: String,
        lon: String,
        unit: String,
        lang: String
    ): Flow<WeatherRoot> {
        return flowOf(weatherData[0])
    }

    override fun getStoredWeather(): Flow<WeatherRoot> {
        return flowOf(weatherData[0])
    }

    override suspend fun insertWeather(weatherRoot: WeatherRoot) {
        weatherData.add(weatherRoot)
    }

    override suspend fun deleteWeather(weatherRoot: WeatherRoot) {
        weatherData.remove(weatherRoot)
    }

    override fun getStoredAlerts(): Flow<List<AlertModel>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertAlert(alert: AlertModel) : Long {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAlert(alert: AlertModel) {
        TODO("Not yet implemented")
    }

    override fun getStoredFavourites(): Flow<List<Favourite>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertFavourite(favorite: Favourite) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFavourite(favorite: Favourite) {
        TODO("Not yet implemented")
    }
}