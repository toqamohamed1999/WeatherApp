package eg.gov.iti.jets.weatherapp.alert

import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.graphics.PixelFormat
import android.media.MediaPlayer
import android.os.Build
import android.util.Log
import android.view.*
import android.widget.TextView
import eg.gov.iti.jets.weatherapp.R


class AlertWindow(val context: Context) {

    private val TAG = "AlertWindow"

    // declaring required variables
    private val mView: View
    private var mParams: WindowManager.LayoutParams? = null
    private val mWindowManager: WindowManager
    private val layoutInflater: LayoutInflater

    private val mediaPlayer = MediaPlayer.create(context,R.raw.weather_sound)

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // set the layout parameters of the window
            mParams = WindowManager.LayoutParams( // Shrink the window to wrap the content rather
                // than filling the screen
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,  // Display it on top of other application windows
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,  // Don't let it grab the input focus
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,  // Make the underlying application window visible
                // through any transparent parts
                PixelFormat.TRANSLUCENT
            )
        }
        // getting a LayoutInflater
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        // inflating the view with the custom layout we created
        mView = layoutInflater.inflate(R.layout.display_alert_dialog, null)

        // set onClickListener on the remove button, which removes
        // the view from the window
        mView.findViewById<TextView>(R.id.alertItem_dismiss_textView).setOnClickListener {
            //stop media player
            if(mediaPlayer.isPlaying){
                mediaPlayer.stop()
            }
            close()
        }

        // Define the position of the
        // window within the screen
        mParams!!.width = WindowManager.LayoutParams.WRAP_CONTENT
        mParams!!.gravity = Gravity.TOP
        mParams!!.y = 15
        mWindowManager = context.getSystemService(WINDOW_SERVICE) as WindowManager
    }

    fun open() {
        try {
            // check if the view is already
            // inflated or present in the window
            if (mView.windowToken == null) {
                if (mView.parent == null) {
                    mWindowManager.addView(mView, mParams)
                }
            }
            //start media player
            if(!mediaPlayer.isPlaying){
                mediaPlayer.start()
            }
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
        }
    }

    private fun close() {
        try {
            // remove the view from the window
            (context.getSystemService(WINDOW_SERVICE) as WindowManager).removeView(mView)
            // invalidate the view
            mView.invalidate()
            // remove all views
            (mView.parent as ViewGroup).removeAllViews()

        } catch (e: Exception) {
            Log.i(TAG, e.toString())
        }
    }
}