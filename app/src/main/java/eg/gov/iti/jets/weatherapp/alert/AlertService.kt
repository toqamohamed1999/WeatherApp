package eg.gov.iti.jets.weatherapp.alert

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.support.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import eg.gov.iti.jets.weatherapp.MainActivity
import eg.gov.iti.jets.weatherapp.MySharedPref
import eg.gov.iti.jets.weatherapp.R
import eg.gov.iti.jets.weatherapp.model.AlertModel


class AlertService : Service() {

    private val TAG = "AlertService"

    private var alertModel : AlertModel? = null


    private val mySharedPref by lazy {
        MySharedPref.getMyPref(this)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if(intent!!.hasExtra("alertModel")){
            alertModel = intent.getSerializableExtra("alertModel") as AlertModel
        }

        // create the custom or default notification
        // based on the android version
        if(mySharedPref.getSetting().notification == eg.gov.iti.jets.weatherapp.model.Notification.Enable) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) createNotification()
            else startForeground(1, Notification())
        }

        // create an instance of Window class - and display the content on screen
        //display alert dialog
        val window = AlertWindowManager(this,alertModel!!)
        window.open()

        return START_NOT_STICKY
    }


    val NOTIFICATION_CHANNEL_ID = "210"

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotification() {

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_MUTABLE
        )

        val channelName = "Background Service"
        val chan = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            channelName,
            NotificationManager.IMPORTANCE_MIN
        )
        val manager =
            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?)!!
        manager.createNotificationChannel(chan)

        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)

        val alarmSound: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notification: Notification = notificationBuilder.setOngoing(true)
            .setContentTitle("Weather Alert")
            .setContentText("No alerts in "+alertModel?.address)
            .setSmallIcon(R.drawable.logo)
            .setSound(alarmSound)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setPriority(NotificationManager.IMPORTANCE_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(2, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}