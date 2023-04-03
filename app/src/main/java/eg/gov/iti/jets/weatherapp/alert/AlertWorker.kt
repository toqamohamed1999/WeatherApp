package eg.gov.iti.jets.weatherapp.alert


import android.content.Context
import android.content.Intent

import android.os.Build
import android.provider.Settings
import android.util.Log

import androidx.work.*
import com.google.gson.Gson
import eg.gov.iti.jets.foodplanner.NetworkChecker
import eg.gov.iti.jets.mymvvm.datatbase.LocaleSource
import eg.gov.iti.jets.mymvvm.model.Repo
import eg.gov.iti.jets.mymvvm.model.RepoInterface
import eg.gov.iti.jets.mymvvm.network.RemoteSource
import eg.gov.iti.jets.weatherapp.model.AlertModel
import eg.gov.iti.jets.weatherapp.model.Alerts
import eg.gov.iti.jets.weatherapp.model.WeatherRoot
import eg.gov.iti.jets.weatherapp.utils.getCurrentDateAsDate
import eg.gov.iti.jets.weatherapp.utils.getDate
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEmpty
import java.util.*



class AlertWorker(val context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    private val TAG = "AlertWorker"

    private var alertModel: AlertModel? = null  //from my database
    private var alert: Alerts? =
        Alerts(event = "No Alerts", description = "No Alerts")  //from Api call

    private var weatherRoot: WeatherRoot? = null

    private val repo: RepoInterface = Repo.getInstance(RemoteSource(), LocaleSource(context))!!

    @androidx.annotation.RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result {
        return try {

            startAlertWork()

            Result.success()
        } catch (ex: Exception) {
            Log.i(TAG, "doWork: " + ex.message)
            Result.failure()
        }
    }

    @androidx.annotation.RequiresApi(Build.VERSION_CODES.O)
    private suspend fun startAlertWork() {
        val strAlertGson = inputData.getString("alertModel")
        alertModel = Gson().fromJson(strAlertGson, AlertModel::class.java)

        if (getCurrentDateAsDate() <= getDate(alertModel?.endDate!!)) {

            getWeatherData()
            deleteAlert()
        } else {
            deleteAlert()
        }
    }

    @androidx.annotation.RequiresApi(Build.VERSION_CODES.O)
    private fun showAlert() {
        startService()
    }


    private fun startService() {

        val alertServiceIntent = Intent(applicationContext, AlertService::class.java)
        alertServiceIntent.putExtra("alertModel", alertModel)
        alertServiceIntent.putExtra("alertApi", alert)

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


    @androidx.annotation.RequiresApi(Build.VERSION_CODES.O)
    private suspend fun getWeatherData() {

        if(NetworkChecker.isNetworkAvailable(applicationContext)) {
            repo.getCurrentWeather(
                alertModel?.latitude ?: "",
                alertModel?.longitude ?: "",
                "metric",
                "eng"
            )
                .catch {
                    Log.i(TAG, "getWeatherData: " + it.message)
                    showAlert()
                }.collect {
                    weatherRoot = it
                    getWeatherAlerts(it)
                    showAlert()
                }
        }else{
            showAlert()
        }
    }

    private fun getWeatherAlerts(weatherRoot: WeatherRoot) {
        Log.i(TAG, "getWeatherAlerts: alerts  " + weatherRoot!!.alerts)
        if (weatherRoot?.alerts != null) {
            alert = weatherRoot?.alerts!![0]
        }
    }

    private suspend fun deleteAlert() {
        if (getCurrentDateAsDate().day >= getDate(alertModel?.endDate!!)!!.day) {
            repo.deleteAlert(alertModel!!)
            WorkManager.getInstance().cancelAllWorkByTag("${alertModel!!.currentTime}")
        }
    }
    
}