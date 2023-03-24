package eg.gov.iti.jets.weatherapp.home.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.gov.iti.jets.mymvvm.Utilites.ApiState
import eg.gov.iti.jets.mymvvm.model.RepoInterface
import eg.gov.iti.jets.weatherapp.model.WeatherRoot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(private val repoInterface: RepoInterface) : ViewModel() {

    private val TAG = "HomeViewModel"

    private var weatherMutableStateFlow = MutableStateFlow<ApiState>(ApiState.Loading)
    val weatherStateFlow = weatherMutableStateFlow.asStateFlow()

    init {
        getCurrentWeather()
    }

    private fun getCurrentWeather(){

        viewModelScope.launch(Dispatchers.IO) {
            repoInterface.getCurrentWeather()
                .catch {
                    weatherMutableStateFlow.value = ApiState.Failure(it)
                }.collect{
                    weatherMutableStateFlow.value = ApiState.Success(it)
                }
        }
    }

    fun insertWeather(weatherRoot: WeatherRoot){
        viewModelScope.launch(Dispatchers.IO) {
            repoInterface.insertWeather(weatherRoot)
        }
    }
}