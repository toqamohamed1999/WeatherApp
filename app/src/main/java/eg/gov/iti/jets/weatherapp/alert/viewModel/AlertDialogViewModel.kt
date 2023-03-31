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
import kotlinx.coroutines.launch

class AlertDialogViewModel(private val repoInterface: RepoInterface) : ViewModel() {

    private val TAG = "AlertDialogViewModel"

    private var alertMutableStateFlow = MutableStateFlow<RoomState>(RoomState.Loading)
    val alertStateFlow = alertMutableStateFlow.asStateFlow()

    fun insertAlert(alert: AlertModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repoInterface.insertAlert(alert)
        }
    }

}


class AlertDialogModelFactory(private val repoInterface: RepoInterface) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return if (modelClass.isAssignableFrom(AlertDialogViewModel::class.java)) {
            AlertFragmentViewModel(repoInterface) as T
        } else {
            throw java.lang.IllegalArgumentException("AlertDialogViewModel class not found")
        }
    }
}