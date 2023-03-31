package eg.gov.iti.jets.weatherapp.favorite.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import eg.gov.iti.jets.mymvvm.Utilites.ApiState
import eg.gov.iti.jets.mymvvm.Utilites.WeatherState
import eg.gov.iti.jets.mymvvm.model.RepoInterface
import eg.gov.iti.jets.weatherapp.alert.viewModel.AlertFragmentViewModel
import eg.gov.iti.jets.weatherapp.model.AlertModel
import eg.gov.iti.jets.weatherapp.model.Favourite
import eg.gov.iti.jets.weatherapp.utils.RoomState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavDetailsViewModel (private val repoInterface: RepoInterface) : ViewModel() {

    private val TAG = "FavDetailsViewModel"

    private var favMutableStateFlow = MutableStateFlow<WeatherState>(ApiState.Loading)
    val favDetailsStateFlow = favMutableStateFlow.asStateFlow()


    fun getFavDetailsWeather(lat : String,lon:String,unit : String ,lang:String){

        viewModelScope.launch(Dispatchers.IO) {
            repoInterface.getCurrentWeather(lat,lon,unit,lang)
                .catch {
                    favMutableStateFlow.value = ApiState.Failure(it)
                }.collect{
                    favMutableStateFlow.value = ApiState.Success(it)
                }
        }
    }


}


class FavDetailsModelFactory(private val repoInterface: RepoInterface)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return if (modelClass.isAssignableFrom(FavDetailsViewModel::class.java)) {
            FavDetailsViewModel(repoInterface) as T
        } else {
            throw java.lang.IllegalArgumentException("FavDetailsViewModel class not found")
        }
    }
}