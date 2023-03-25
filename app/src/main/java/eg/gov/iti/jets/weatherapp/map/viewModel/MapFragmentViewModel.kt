package eg.gov.iti.jets.weatherapp.map.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import eg.gov.iti.jets.mymvvm.model.RepoInterface
import eg.gov.iti.jets.weatherapp.favorite.viewModel.FavouriteFragmentViewModel
import eg.gov.iti.jets.weatherapp.model.Favourite
import eg.gov.iti.jets.weatherapp.utils.RoomState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class MapFragmentViewModel (private val repoInterface: RepoInterface) : ViewModel() {

    private val TAG = "AlertFragmentViewModel"

    private var favMutableStateFlow = MutableStateFlow<RoomState>(RoomState.Loading)
    val favStateFlow = favMutableStateFlow.asStateFlow()


    fun insertFavourite(favourite: Favourite){
        viewModelScope.launch(Dispatchers.IO) {
            repoInterface.insertFavourite(favourite)
        }
    }
}


class MapViewModelFactory(private val repoInterface: RepoInterface)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return if (modelClass.isAssignableFrom(MapFragmentViewModel::class.java)) {
            MapFragmentViewModel(repoInterface) as T
        } else {
            throw java.lang.IllegalArgumentException("MapFragmentViewModel class not found")
        }
    }
}