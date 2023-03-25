package eg.gov.iti.jets.weatherapp.favorite.view

import eg.gov.iti.jets.weatherapp.model.Favourite

interface DeleteFavListener {

    fun deleteFav(favourite: Favourite)
}