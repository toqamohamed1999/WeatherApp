package eg.gov.iti.jets.weatherapp.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import eg.gov.iti.jets.mymvvm.MyDataBase
import eg.gov.iti.jets.mymvvm.Utilites.ApiState
import eg.gov.iti.jets.weatherapp.MainCoroutineRule
import eg.gov.iti.jets.weatherapp.model.WeatherRoot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class WeatherDaoTest {
    @ExperimentalCoroutinesApi
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    ///coroutine test
    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var database: MyDataBase

    @Before
    fun setUp(){
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MyDataBase::class.java)
            .build()
    }

    @After
    fun closeDatabase() = database.close()


    private var weatherRoot2 = WeatherRoot(
        lat = 4.123,
        lon = 8.567,
        timezone = "paris",
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

    @Test
    fun insertWeather_ReturnWeatherInserted() = runBlockingTest{
        //give
        database.getWeatherDao().insertWeather(weatherRoot)

        //When
        val resultWeather = database.getWeatherDao().getStoredWeather().first()

        //Then
        MatcherAssert.assertThat(resultWeather.lat, CoreMatchers.`is`(7.123))
        MatcherAssert.assertThat(resultWeather.timezone, CoreMatchers.`is`("cairo"))
    }

    @Test
    fun deleteWeather_ReturnWeatherDeleted() = runBlockingTest{
        //give
        database.getWeatherDao().insertWeather(weatherRoot)
        database.getWeatherDao().insertWeather(weatherRoot2)
        //delete
        database.getWeatherDao().deleteWeather(weatherRoot)

        //When
        val resultWeather = database.getWeatherDao().getStoredWeather().first()

        //Then
        MatcherAssert.assertThat(resultWeather.lat, CoreMatchers.`is`(4.123))
        MatcherAssert.assertThat(resultWeather.timezone, CoreMatchers.`is`("paris"))
    }

}