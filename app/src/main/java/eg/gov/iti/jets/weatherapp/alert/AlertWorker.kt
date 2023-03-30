package eg.gov.iti.jets.weatherapp.alert

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import eg.gov.iti.jets.weatherapp.utils.MyCustomDialog


class AlertWorker (val context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    private val TAG = "AlertWorker"

    override fun doWork(): Result {
        return try {
            Log.i(TAG, "doWork: alertttt inside")
            Result.success(workDataOf("output" to "myOutput"))
        }catch (ex : Exception){
            Log.i(TAG, "doWork: "+ex.message)
            Result.failure(workDataOf("output" to ex.message))
        }
    }

    private fun setUpAlertDialog(context: Context){
        Log.i(TAG, "setUpAlertDialog: alertttt fun")
        val alert = MyCustomDialog()
        alert.showAlertDialog(context)
    }


}