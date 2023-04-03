package eg.gov.iti.jets.weatherapp.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import eg.gov.iti.jets.mymvvm.MyDataBase
import eg.gov.iti.jets.weatherapp.MainCoroutineRule
import eg.gov.iti.jets.weatherapp.model.Favourite
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.*
import org.junit.runner.RunWith



@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class FavDaoTest {

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

    private var favList: MutableList<Favourite> = mutableListOf(
        Favourite(id = 1, latitude = 1.0, longitude = 1.0,  address = "egypt1", countryCode = ""),
        Favourite(id = 2, latitude = 2.0, longitude = 2.0,  address = "egypt2", countryCode = ""),
        Favourite(id = 3, latitude = 3.0, longitude = 3.0,  address = "egypt3", countryCode = ""))



    @Test
    fun insertFav_ReturnFavInserted() = runBlockingTest {
        //When
        database.getFavouriteDao().insertFavourite(favList[0])

        val resultFavList = database.getFavouriteDao().getStoredFavourites().first()

        //Then
        MatcherAssert.assertThat(resultFavList.size, CoreMatchers.`is`(1))
    }

    @Test
    fun deleteFav_ReturnFavDeleted() = runBlockingTest {
        //When
        database.getFavouriteDao().insertFavourite(favList[0])
        database.getFavouriteDao().insertFavourite(favList[1])
        database.getFavouriteDao().insertFavourite(favList[2])
        //delete
        database.getFavouriteDao().deleteFavourite(favList[2])

        val resultFavList = database.getFavouriteDao().getStoredFavourites().first()

        //Then
        MatcherAssert.assertThat(resultFavList.size, CoreMatchers.`is`(2))
    }
}