package eg.gov.iti.jets.weatherapp.home.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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



    fun getCurrentWeather(lat : String,lon:String,unit : String ,lang:String){

        viewModelScope.launch(Dispatchers.IO) {
            repoInterface.getCurrentWeather(lat,lon,unit,lang)
                .catch {
                    weatherMutableStateFlow.value = ApiState.Failure(it)
                }.collect{
                    weatherMutableStateFlow.value = ApiState.Success(it)
                }
        }
    }

//    private fun getStoredWeather(){
//
//        viewModelScope.launch(Dispatchers.IO) {
//            repoInterface.getStoredWeather()
//                .catch {
//                    weatherMutableStateFlow.value = Room.Failure(it)
//                }.collect{
//                    weatherMutableStateFlow.value = ApiState.Success(it)
//                }
//        }
//    }

    fun insertWeather(weatherRoot: WeatherRoot){
        viewModelScope.launch(Dispatchers.IO) {
            repoInterface.insertWeather(weatherRoot)
        }
    }
}


class HomeViewModelFactory(private val repoInterface: RepoInterface)
    : ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return if (modelClass.isAssignableFrom(HomeViewModel::class.java)){
            HomeViewModel(repoInterface) as T
        }else{
            throw java.lang.IllegalArgumentException("HomeViewModel class not found")
        }
    }
}