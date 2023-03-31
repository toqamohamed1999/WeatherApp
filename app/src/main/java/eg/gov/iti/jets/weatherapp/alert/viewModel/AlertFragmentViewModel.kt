package eg.gov.iti.jets.weatherapp.alert.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import eg.gov.iti.jets.mymvvm.Utilites.ApiState
import eg.gov.iti.jets.mymvvm.model.RepoInterface
import eg.gov.iti.jets.weatherapp.home.viewModel.HomeViewModel
import eg.gov.iti.jets.weatherapp.model.AlertModel
import eg.gov.iti.jets.weatherapp.model.WeatherRoot
import eg.gov.iti.jets.weatherapp.utils.RoomState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AlertFragmentViewModel (private val repoInterface: RepoInterface) : ViewModel() {

    private val TAG = "AlertFragmentViewModel"

    private var alertMutableStateFlow = MutableStateFlow<RoomState>(RoomState.Loading)
    val alertStateFlow = alertMutableStateFlow.asStateFlow()


    init {
        getStoredAlerts()
    }

    private fun getStoredAlerts(){

        viewModelScope.launch(Dispatchers.IO) {
            repoInterface.getStoredAlerts()
                .catch {
                    alertMutableStateFlow.value = RoomState.Failure(it)
                }.collect{
                    alertMutableStateFlow.value = RoomState.SuccessAlert(it)
                }
        }
    }

    fun deleteAlert(alert: AlertModel){
        viewModelScope.launch(Dispatchers.IO) {
            repoInterface.deleteAlert(alert)
        }
    }
}


class AlertViewModelFactory(private val repoInterface: RepoInterface)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return if (modelClass.isAssignableFrom(AlertFragmentViewModel::class.java)) {
            AlertFragmentViewModel(repoInterface) as T
        } else {
            throw java.lang.IllegalArgumentException("AlertFragmentViewModel class not found")
        }
    }
}