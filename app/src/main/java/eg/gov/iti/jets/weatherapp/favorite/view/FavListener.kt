package eg.gov.iti.jets.weatherapp.favorite.view

import eg.gov.iti.jets.weatherapp.model.Favourite

interface FavListener {

    fun deleteFav(favourite: Favourite)
    fun navigateToDetails(favourite: Favourite)

}