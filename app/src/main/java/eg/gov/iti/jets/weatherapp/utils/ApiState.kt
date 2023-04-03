package eg.gov.iti.jets.mymvvm.Utilites

import eg.gov.iti.jets.weatherapp.model.WeatherRoot


sealed class ApiState : WeatherState{

    class Success(val weatherRoot: WeatherRoot) : ApiState()

    class Failure(val msg: Throwable) : ApiState()

    object Loading : ApiState()

}

interface WeatherState{
}
