package eg.gov.iti.jets.weatherapp.home.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import eg.gov.iti.jets.mymvvm.model.RepoInterface

class HomeViewModelFactory(private val repoInterface: RepoInterface)
    :ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return if (modelClass.isAssignableFrom(HomeViewModel::class.java)){
            HomeViewModel(repoInterface) as T
        }else{
            throw java.lang.IllegalArgumentException("HomeViewModel class not found")
        }
    }
}