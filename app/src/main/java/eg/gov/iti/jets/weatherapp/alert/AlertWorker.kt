package eg.gov.iti.jets.weatherapp.alert

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import java.util.*


class AlertWorker (val context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    private val TAG = "AlertWorker"

    override fun doWork(): Result {
        return try {
            Log.i(TAG, "doWork: alertttt inside")

            showAlert()

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (Settings.canDrawOverlays(applicationContext)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    applicationContext.startForegroundService(
                        Intent(
                            applicationContext, AlertService::class.java
                        )
                    )
                } else {
                    applicationContext.startService(
                        Intent(
                            applicationContext, AlertService::class.java
                        )
                    )
                }
            }
        } else {
            applicationContext.startService(Intent(applicationContext, AlertService::class.java))
        }

    }

}