package eg.gov.iti.jets.weatherapp.home.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import eg.gov.iti.jets.mymvvm.Utilites.ApiState
import eg.gov.iti.jets.weatherapp.MainCoroutineRule
import eg.gov.iti.jets.weatherapp.model.FakeRepo
import eg.gov.iti.jets.weatherapp.model.Favourite
import eg.gov.iti.jets.weatherapp.model.WeatherRoot
import eg.gov.iti.jets.weatherapp.utils.RoomState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@RunWith(JUnit4::class)
class HomeViewModelTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var repo: FakeRepo

    private var defaultWeatherRoot = WeatherRoot(
        lat = 0.0,
        lon = 0.0,
        timezone = "cairo",
        hourly = emptyList(),
        daily = emptyList(),
        alerts = emptyList(),
        timezoneOffset = ""
    )

    private var weatherRoot = WeatherRoot(
        lat = 7.123,
        lon = 4.567,
        timezone = "cairo",
        hourly = emptyList(),
        daily = emptyList(),
        alerts = emptyList(),
        timezoneOffset = ""
    )

    private var weatherRootInserted = WeatherRoot(
        lat = 1234.0,
        lon = 5678.0,
        timezone = "china",
        hourly = emptyList(),
        daily = emptyList(),
        alerts = emptyList(),
        timezoneOffset = ""
    )


    ///coroutine test
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setup() {
        repo = FakeRepo()
        homeViewModel = HomeViewModel(repo)
    }


    @ExperimentalCoroutinesApi
    @Test
    fun getCurrentWeather_ReturnRemoteWeather() = runBlockingTest {

        //give
        repo.weatherRoot = weatherRoot

        //When
        homeViewModel.getCurrentWeather("", "", "", "")
        var weatherRootData  = defaultWeatherRoot


        when (val result = homeViewModel.weatherStateFlow.first()) {
            is ApiState.Success -> {

                println(result.weatherRoot)
                weatherRootData = result.weatherRoot
            }
            is ApiState.Failure -> {
                println(result.msg)
            }
            is ApiState.Loading -> {
                println("loading")
            }
            else -> {
            }
        }
        //Then
        MatcherAssert.assertThat(weatherRootData!!.lat, CoreMatchers.`is`(7.123))
        MatcherAssert.assertThat(weatherRootData!!.timezone, CoreMatchers.`is`("cairo"))

    }

    @Test
    fun insertWeather_ReturnWeatherInserted() = runBlockingTest{
        //give
        repo.weatherRoot = defaultWeatherRoot
        homeViewModel.insertWeather(weatherRootInserted)

        //When
        homeViewModel.getCurrentWeather("", "", "", "")
        var weatherRootData  = defaultWeatherRoot

        when (val result = homeViewModel.weatherStateFlow.first()) {
            is ApiState.Success -> {

                weatherRootData = result.weatherRoot
            }
            else -> {

            }
        }
        //Then
        MatcherAssert.assertThat(weatherRootData!!.lat, CoreMatchers.`is`(1234.0))
        MatcherAssert.assertThat(weatherRootData!!.timezone, CoreMatchers.`is`("china"))
    }
}
