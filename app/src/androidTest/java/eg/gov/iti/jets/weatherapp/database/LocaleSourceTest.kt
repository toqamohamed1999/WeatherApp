package eg.gov.iti.jets.weatherapp.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import eg.gov.iti.jets.mymvvm.MyDataBase
import eg.gov.iti.jets.mymvvm.datatbase.LocaleSource
import eg.gov.iti.jets.weatherapp.MainCoroutineRule
import eg.gov.iti.jets.weatherapp.model.AlertModel
import eg.gov.iti.jets.weatherapp.model.Favourite
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@MediumTest
class LocaleSourceTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    ///coroutine test
    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var database: MyDataBase
    private lateinit var localDataSource: LocaleSource

    @Before
    fun setUp(){
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MyDataBase::class.java)
            .allowMainThreadQueries()
            .build()

        localDataSource = LocaleSource(ApplicationProvider.getApplicationContext())
    }

    private var alertList: MutableList<AlertModel> = mutableListOf(
        AlertModel("12:00", "1.0", "1.1", alertEnabled = false),
        AlertModel("01:00", "2.0", "2.1", alertEnabled = true),
        AlertModel("02:00", "3.0", "3.1", alertEnabled = false)
    )

    private var favList: MutableList<Favourite> = mutableListOf(
        Favourite(id = 1, latitude = 1.0, longitude = 1.0,  address = "egypt1", countryCode = ""),
        Favourite(id = 2, latitude = 2.0, longitude = 2.0,  address = "egypt2", countryCode = ""),
        Favourite(id = 3, latitude = 3.0, longitude = 3.0,  address = "egypt3", countryCode = "")
    )


    @Test
    fun insertAlert_ReturnAlertInserted() = runBlockingTest {
        //When
        localDataSource.insertAlert(alertList[1])
        localDataSource.insertAlert(alertList[2])
        localDataSource.insertAlert(alertList[3])

        val resultAlertList = localDataSource.getStoredAlerts().first()

        //Then
        MatcherAssert.assertThat(resultAlertList.size, CoreMatchers.`is`(3))
    }

    @Test
    fun deleteAlert_ReturnAlertFavDeleted() = runBlockingTest {
        //When
        localDataSource.insertAlert(alertList[0])
        localDataSource.insertAlert(alertList[1])
        localDataSource.insertAlert(alertList[2])
        //delete
        localDataSource.deleteAlert(alertList[2])

        val resultAlertList = localDataSource.getStoredAlerts().first()

        //Then
        MatcherAssert.assertThat(resultAlertList.size, CoreMatchers.`is`(2))
    }


    @Test
    fun insertFav_ReturnFavInserted() = runBlockingTest {
        //When
        localDataSource.insertFavourite(favList[0])

        val resultFavList = localDataSource.getStoredFavourites().first()
        println("resultFavList = $resultFavList")

        //Then
        MatcherAssert.assertThat(resultFavList.size, CoreMatchers.`is`(1))
    }

    @Test
    fun deleteFav_ReturnFavDeleted() = runBlockingTest {
        //When
        localDataSource.insertFavourite(favList[0])
        localDataSource.insertFavourite(favList[1])
        localDataSource.insertFavourite(favList[2])
        //delete
        localDataSource.deleteFavourite(favList[2])

        val resultFavList = localDataSource.getStoredFavourites().first()

        //Then
        MatcherAssert.assertThat(resultFavList.size, CoreMatchers.`is`(2))
    }
}