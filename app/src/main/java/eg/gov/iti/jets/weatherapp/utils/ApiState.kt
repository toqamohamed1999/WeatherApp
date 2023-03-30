package eg.gov.iti.jets.mymvvm.Utilites

import eg.gov.iti.jets.weatherapp.model.WeatherRoot


sealed class ApiState : WeatherState{

    class Success(val weatherRoot: WeatherRoot) : ApiState()

    class Failure(val msg: Throwable) : ApiState()

    object Loading : ApiState()

}

interface WeatherState{
}

//sealed class ApiResponse <T>{
//    class OnSucess<T>(var data: T):ApiResponse<T>()
//    class OnLoading<T>():ApiResponse<T>()
//    class onError<T>(var message: String):ApiResponse<T>()
//}