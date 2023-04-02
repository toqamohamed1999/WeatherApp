package eg.gov.iti.jets.weatherapp.alert

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.work.*
import com.google.gson.Gson
import eg.gov.iti.jets.mymvvm.Utilites.ApiState
import eg.gov.iti.jets.mymvvm.datatbase.LocaleSource
import eg.gov.iti.jets.mymvvm.model.Repo
import eg.gov.iti.jets.mymvvm.model.RepoInterface
import eg.gov.iti.jets.mymvvm.network.RemoteSource
import eg.gov.iti.jets.weatherapp.alert.viewModel.AlertDialogModelFactory
import eg.gov.iti.jets.weatherapp.alert.viewModel.AlertDialogViewModel
import eg.gov.iti.jets.weatherapp.model.AlertModel
import eg.gov.iti.jets.weatherapp.model.WeatherRoot
import eg.gov.iti.jets.weatherapp.utils.getTime
import kotlinx.coroutines.flow.catch
import java.util.*


class AlertWorker (val context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    private val TAG = "AlertWorker"

    private var alertModel: AlertModel? = null

    private var weatherRoot: WeatherRoot? = null

    private val repo : RepoInterface = Repo.getInstance(RemoteSource(), LocaleSource(context))!!

    override suspend fun doWork(): Result {
        return try {
            Log.i(TAG, "doWork: alertttt inside")

            val strAlertGson = inputData.getString("alertModel")
            alertModel = Gson().fromJson(strAlertGson, AlertModel::class.java)

            showAlert()
            getWeatherData()
            deleteAlert()

            Result.success(workDataOf("output" to "myOutput"))
        }catch (ex : Exception){
            Log.i(TAG, "doWork: "+ex.message)
            Result.failure(workDataOf("output" to ex.message))
        }
    }

    private fun showAlert(){
        startService()
    }


    private fun startService() {

        val alertServiceIntent =  Intent(applicationContext, AlertService::class.java)
        alertServiceIntent.putExtra("alertModel",alertModel)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (Settings.canDrawOverlays(applicationContext)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    applicationContext.startForegroundService(alertServiceIntent)
                } else {
                    applicationContext.startService(alertServiceIntent)
                }
            }
        } else {
            applicationContext.startService(alertServiceIntent)
        }

    }


    private suspend fun getWeatherData(){

        repo.getCurrentWeather(alertModel?.latitude ?: "",alertModel?.longitude ?:"","metric","eng")
            .catch {
                Log.i(TAG, "getWeatherData: "+it.message)
            }.collect{
                Log.i(TAG, "getWeatherData: $it")
                weatherRoot = it
                getWeatherAlerts()
            }
    }

    private fun getWeatherAlerts(){
        Log.i(TAG, "getWeatherAlerts: alerts  "+weatherRoot!!.alerts)
        if(weatherRoot?.alerts != null){

        }
    }

    private suspend fun deleteAlert(){
        repo.deleteAlert(alertModel!!)
        WorkManager.getInstance().cancelAllWorkByTag("${alertModel!!.id}")
    }

}