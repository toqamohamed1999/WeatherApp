package eg.gov.iti.jets.mymvvm

import android.provider.SyncStateContract.Constants
import eg.gov.iti.jets.weatherapp.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object MyRetrofit {

    val retrofit: Retrofit =  Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

object APIClient{
    val apiInterface : ApiInterface by lazy {
        MyRetrofit.retrofit.create(ApiInterface::class.java)
    }
}

