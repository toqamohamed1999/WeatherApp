package eg.gov.iti.jets.weatherapp.model

import eg.gov.iti.jets.mymvvm.Utilites.ApiState
import eg.gov.iti.jets.mymvvm.model.Repo
import eg.gov.iti.jets.weatherapp.database.FakeLocaleSource
import eg.gov.iti.jets.weatherapp.network.FakeRemoteSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class RepoTest {

    private var weatherRoot = WeatherRoot(
        lat = 7.123,
        lon = 4.567,
        timezone = "cairo",
        hourly = emptyList(),
        daily = emptyList(),
        alerts = emptyList(),
        timezoneOffset = ""
    )

    private var defaultWeatherRoot = WeatherRoot(
        lat = 0.0,
        lon = 0.0,
        timezone = "cairo",
        hourly = emptyList(),
        daily = emptyList(),
        alerts = emptyList(),
        timezoneOffset = ""
    )

    private var favList: MutableList<Favourite> = mutableListOf(
        Favourite(id = 1, latitude = 1.0, longitude = 1.0, address = "egypt1", countryCode = ""),
        Favourite(id = 2, latitude = 2.0, longitude = 2.0, address = "egypt2", countryCode = ""),
        Favourite(id = 3, latitude = 3.0, longitude = 3.0, address = "egypt3", countryCode = "")
    )


    private lateinit var fakeRemoteDataSource: FakeRemoteSource
    private lateinit var fakeLocaleDataSource: FakeLocaleSource
    private lateinit var repo: Repo

    @Before
    fun setup() {
        fakeRemoteDataSource = FakeRemoteSource(weatherRoot)
        fakeLocaleDataSource = FakeLocaleSource(favList)

        repo = Repo.getInstance(fakeRemoteDataSource, fakeLocaleDataSource)!!
    }


    @ExperimentalCoroutinesApi
    @Test
    fun getCurrentWeather_ReturnRemoteWeather() = runBlockingTest {

        //When
        val resultWeather = repo.getCurrentWeather("", "", "", "").first()

        //Then
        MatcherAssert.assertThat(resultWeather.lat, CoreMatchers.`is`(7.123))
        MatcherAssert.assertThat(resultWeather.timezone, CoreMatchers.`is`("cairo"))
    }

    @Test
    fun insertFav_ReturnFavInserted() = runBlockingTest {
        //When
        repo.insertFavourite(
            Favourite(
                id = 4,
                latitude = 4.0,
                longitude = 4.0,
                address = "egypt4",
                countryCode = ""
            )
        )

        val resultFavList = repo.getStoredFavourites().first()

        //Then
        MatcherAssert.assertThat(resultFavList.size, CoreMatchers.`is`(4))
    }

    @Test
    fun deleteWeather_ReturnFavDeleted() = runBlockingTest {
        //When
        repo.deleteFavourite(favList[2])

        val resultFavList = repo.getStoredFavourites().first()

        //Then
        MatcherAssert.assertThat(resultFavList.size, CoreMatchers.`is`(2))
    }

}