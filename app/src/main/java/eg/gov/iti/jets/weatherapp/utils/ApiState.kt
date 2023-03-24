package eg.gov.iti.jets.mymvvm.Utilites

import eg.gov.iti.jets.weatherapp.model.WeatherRoot


sealed class ApiState {

    class Success(val weatherRoot: WeatherRoot) : ApiState()

    class Failure(val msg: Throwable) : ApiState()

    object Loading : ApiState()

}