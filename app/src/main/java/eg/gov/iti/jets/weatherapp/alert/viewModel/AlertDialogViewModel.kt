package eg.gov.iti.jets.weatherapp.alert.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import eg.gov.iti.jets.mymvvm.model.RepoInterface
import eg.gov.iti.jets.weatherapp.model.AlertModel
import eg.gov.iti.jets.weatherapp.utils.RoomState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AlertDialogViewModel(private val repoInterface: RepoInterface) : ViewModel() {

    private val TAG = "AlertDialogViewModel"

    private var alertMutableStateFlow = MutableStateFlow<Long>(0)
    val alertStateFlow = alertMutableStateFlow.asStateFlow()

    private var mutableStateFlow = MutableStateFlow<RoomState>(RoomState.Loading)
    val stateFlow = mutableStateFlow.asStateFlow()

    init {
        getStoredAlerts()
    }

     fun insertAlert(alert: AlertModel) {
        viewModelScope.launch(Dispatchers.IO) {
            //return inserted alert ID
            alertMutableStateFlow.value = repoInterface.insertAlert(alert)
        }
    }

    fun deleteAlert(alert: AlertModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repoInterface.deleteAlert(alert)
        }
    }

    fun getStoredAlerts(){

        viewModelScope.launch(Dispatchers.IO) {
            repoInterface.getStoredAlerts()
                .catch {
                    mutableStateFlow.value = RoomState.Failure(it)
                }.collect{
                    mutableStateFlow.value = RoomState.SuccessAlert(it)
                }
        }
    }

}


class AlertDialogModelFactory(private val repoInterface: RepoInterface) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return if (modelClass.isAssignableFrom(AlertDialogViewModel::class.java)) {
            AlertDialogViewModel(repoInterface) as T
        } else {
            throw java.lang.IllegalArgumentException("AlertDialogViewModel class not found")
        }
    }
}