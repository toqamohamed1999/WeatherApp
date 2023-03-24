package eg.gov.iti.jets.weatherapp.utils

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.TextView
import eg.gov.iti.jets.weatherapp.R

class MyCustomDialog {

        fun showResetPasswordDialog(activity: Activity?) {
            val dialog = Dialog(activity!!)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.initial_setting_dialog)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val dialogBtn_remove = dialog.findViewById<TextView>(R.id.okTv)
            dialogBtn_remove.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }

    fun showAlertDialog(activity: Activity?) {
        val dialog = Dialog(activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.display_alert_dialog)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val dialogBtn_remove = dialog.findViewById<TextView>(R.id.okTv)
        dialogBtn_remove.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

}