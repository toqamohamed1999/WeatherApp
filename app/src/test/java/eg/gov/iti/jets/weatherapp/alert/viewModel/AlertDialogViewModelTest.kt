package eg.gov.iti.jets.weatherapp.alert.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import eg.gov.iti.jets.weatherapp.MainCoroutineRule
import eg.gov.iti.jets.weatherapp.favorite.viewModel.FavouriteFragmentViewModel
import eg.gov.iti.jets.weatherapp.model.AlertModel
import eg.gov.iti.jets.weatherapp.model.FakeRepo
import eg.gov.iti.jets.weatherapp.model.Favourite
import eg.gov.iti.jets.weatherapp.utils.RoomState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
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
class AlertDialogViewModelTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var alertDialogViewModel: AlertDialogViewModel
    private lateinit var repo: FakeRepo


    private var alertList: MutableList<AlertModel> = mutableListOf(
        AlertModel("12:00", "1.0", "1.1", alertEnabled = false),
        AlertModel("01:00", "2.0", "2.1", alertEnabled = true),
        AlertModel("02:00", "3.0", "3.1", alertEnabled = false))


    ///coroutine test
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setup() {
        repo = FakeRepo()
        alertDialogViewModel = AlertDialogViewModel(repo)
    }


    @Test
    fun insertAlert_alertInserted() = runBlockingTest{
        repo.alertData = alertList
        alertDialogViewModel.insertAlert(AlertModel("07:00", "7.0", "7.1", alertEnabled = true))

        //When
        alertDialogViewModel.getStoredAlerts()
        var alertListData:List<AlertModel> = emptyList()

        when (val result = alertDialogViewModel.stateFlow.first()) {
            is RoomState.SuccessAlert -> {

                alertListData = result.alertModelList
            }
            else -> {

            }
        }
        //Then
        MatcherAssert.assertThat(alertListData.size, CoreMatchers.`is`(4))
    }

    @Test
    fun deleteAlert_deleteAlert() = runBlockingTest{
        //give
        repo.alertData = alertList
        alertDialogViewModel.deleteAlert(alertList[1])

        //When
        alertDialogViewModel.getStoredAlerts()
        var alertListData:List<AlertModel> = emptyList()

        when (val result = alertDialogViewModel.stateFlow.first()) {
            is RoomState.SuccessAlert -> {

                alertListData = result.alertModelList
            }
            else -> {

            }
        }
        //Then
        MatcherAssert.assertThat(alertListData.size, CoreMatchers.`is`(2))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getStoredAlerts_ReturnStoredAlerts() = runBlockingTest {

        //give
        repo.alertData = alertList

        //When
        alertDialogViewModel.getStoredAlerts()
        var alertListData:List<AlertModel> = emptyList()

        when (val result = alertDialogViewModel.stateFlow.first()) {
            is RoomState.SuccessAlert -> {

                alertListData = result.alertModelList
            }
            else -> {

            }
        }
        //Then
        MatcherAssert.assertThat(alertListData.size, CoreMatchers.`is`(3))
    }
}