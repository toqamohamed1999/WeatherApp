package eg.gov.iti.jets.weatherapp.utils

import eg.gov.iti.jets.mymvvm.Utilites.WeatherState
import eg.gov.iti.jets.weatherapp.model.AlertModel
import eg.gov.iti.jets.weatherapp.model.Favourite
import eg.gov.iti.jets.weatherapp.model.WeatherRoot

sealed class RoomState : WeatherState {

    class SuccessWeather(val weatherRoot: WeatherRoot) : RoomState()

    class Failure(val msg: Throwable) : RoomState()

    object Loading : RoomState()

    class SuccessAlert(val alertModelList: List<AlertModel>) : RoomState()

    class SuccessFavourite(val favouriteList : List<Favourite>) : RoomState()


}