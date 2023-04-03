package eg.gov.iti.jets.weatherapp.favorite.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
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

class FavouriteFragmentViewModel (private val repoInterface: RepoInterface) : ViewModel() {

    private val TAG = "AlertFragmentViewModel"

    private var favMutableStateFlow = MutableStateFlow<RoomState>(RoomState.Loading)
    val favStateFlow = favMutableStateFlow.asStateFlow()

    init {
        getStoredFavourites()
    }

     fun getStoredFavourites(){

        viewModelScope.launch(Dispatchers.IO) {
            repoInterface.getStoredFavourites()
                .catch {
                    favMutableStateFlow.value = RoomState.Failure(it)
                }.collect{
                    favMutableStateFlow.value = RoomState.SuccessFavourite(it)
                }
        }
    }


    fun deleteFavourite(favourite: Favourite){
        viewModelScope.launch(Dispatchers.IO) {
            repoInterface.deleteFavourite(favourite)
        }
    }
}


class FavouriteViewModelFactory(private val repoInterface: RepoInterface)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return if (modelClass.isAssignableFrom(FavouriteFragmentViewModel::class.java)) {
            FavouriteFragmentViewModel(repoInterface) as T
        } else {
            throw java.lang.IllegalArgumentException("FavouriteFragmentViewModel class not found")
        }
    }
}