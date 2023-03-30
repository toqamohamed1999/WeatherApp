package eg.gov.iti.jets.weatherapp.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Window
import android.widget.TextView
import eg.gov.iti.jets.weatherapp.R

class MyCustomDialog {
    private val TAG = "MyCustomDialog"

    fun showAlertDialog(context : Context) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.display_alert_dialog)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val dialogRemoveBtn = dialog.findViewById<TextView>(R.id.alertItem_dismiss_textView)
        dialogRemoveBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

        Log.i(TAG, "doWork: alertttt custom")
    }

}