package eg.gov.iti.jets.weatherapp.favorite.viewModel

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import eg.gov.iti.jets.mymvvm.Utilites.ApiState
import eg.gov.iti.jets.weatherapp.MainCoroutineRule
import eg.gov.iti.jets.weatherapp.home.viewModel.HomeViewModel
import eg.gov.iti.jets.weatherapp.model.FakeRepo
import eg.gov.iti.jets.weatherapp.model.Favourite
import eg.gov.iti.jets.weatherapp.model.WeatherRoot
import eg.gov.iti.jets.weatherapp.utils.RoomState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@RunWith(JUnit4::class)
class FavouriteFragmentViewModelTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var favouriteViewModel: FavouriteFragmentViewModel
    private lateinit var repo: FakeRepo


    private var favList: MutableList<Favourite> = mutableListOf(
        Favourite(id = 1, latitude = 1.0, longitude = 1.0,  address = "egypt1", countryCode = ""),
        Favourite(id = 2, latitude = 2.0, longitude = 2.0,  address = "egypt2", countryCode = ""),
        Favourite(id = 3, latitude = 3.0, longitude = 3.0,  address = "egypt3", countryCode = ""))


    ///coroutine test
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setup() {
        repo = FakeRepo()
        favouriteViewModel = FavouriteFragmentViewModel(repo)
    }


    @ExperimentalCoroutinesApi
    @Test
    fun getStoredFavourites_ReturnStoredFav() = runBlockingTest {

        //give
        repo.favData = favList

        //When
        favouriteViewModel.getStoredFavourites()
        var favListData:List<Favourite> = emptyList()

        when (val result = favouriteViewModel.favStateFlow.first()) {
            is RoomState.SuccessFavourite -> {

                favListData = result.favouriteList
            }
            else -> {

            }
        }
        //Then
        MatcherAssert.assertThat(favListData.size, CoreMatchers.`is`(3))
    }

    @Test
    fun deleteFavourite_deleteFav() =  runBlockingTest{

        //give
        repo.favData = favList
        favouriteViewModel.deleteFavourite(favList[2])

        //When
        favouriteViewModel.getStoredFavourites()
        var favListData:List<Favourite> = emptyList()

        when (val result = favouriteViewModel.favStateFlow.first()) {
            is RoomState.SuccessFavourite -> {

                favListData = result.favouriteList
            }
            else -> {

            }
        }
        //Then
        MatcherAssert.assertThat(favListData.size, CoreMatchers.`is`(2))
    }
}