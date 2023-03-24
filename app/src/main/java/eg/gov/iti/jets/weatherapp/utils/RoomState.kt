package eg.gov.iti.jets.weatherapp.utils

import eg.gov.iti.jets.weatherapp.model.WeatherRoot

sealed class RoomState {

    class Success(val weatherRoot: WeatherRoot) : RoomState()

    class Failure(val msg: Throwable) : RoomState()

    object Loading : RoomState()

}