package eg.gov.iti.jets.weatherapp.utils

import android.app.Activity
import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import eg.gov.iti.jets.weatherapp.R
import eg.gov.iti.jets.weatherapp.model.Temperature
import eg.gov.iti.jets.weatherapp.model.WindSpeed
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt


var Temp_Unit = " º C"
var WindSpeed_Unit = " m/s"



//"hh:mm a   E, MMM dd, yyyy"
fun getTime(pattern:String,date:Long): String =
    SimpleDateFormat(pattern, Locale.getDefault()).format(Date(date*1000))

fun getIcon(iconPath :String):String= "https://openweathermap.org/img/w/${iconPath}.png"

fun getFlagIcon(countryCode :String):String= "https://flagsapi.com/${countryCode}/flat/64.png"

fun getSplitString(str : String): String =
    if(!str.contains(".")) str
    else str.substring(0,str.indexOf("."))


//fun convertFromKelvinToCelsius(tempKelvin: Double): Double {
//    return (tempKelvin - 273.15f).roundToInt().toDouble()
//}
//
//fun convertFromKelvinToFahrenheit(tempKelvin: Double): Double {
//    return (1.8 * (tempKelvin - 273) + 32).roundToInt().toDouble()
//
//}

fun convertFromCelsiusToKelvin(tempCelsius: Double): Double {
    return (tempCelsius + 273.15f).roundToInt().toDouble()
}

fun convertFromCelsiusToFahrenheit(tempCelsius: Double): Double {
    return ((tempCelsius * (9/5)) + 32).roundToInt().toDouble()
}

fun convertMeterToMile(metersPerSec: Double): Double {
    return (metersPerSec *  2.237).roundToInt().toDouble()
}

//fun convertFromFahrenheitToCelsius(tempF: Double): Double {
//    return ((tempF - 32) * (5/9)).roundToInt().toDouble()
//}
//
//fun convertFromFahrenheitToKelvin(tempF: Double): Double {
//    return (((tempF - 32) * (5/9)) + 273.15f).roundToInt().toDouble()
//}
//
//fun convertMileToMeter(milesPerSec: Double): Double {
//    return (milesPerSec /  2.237).roundToInt().toDouble()
//}

fun getTemp(temp: String,mySharedPref: MySharedPref) : String {

    return when(mySharedPref.getSetting().temperature){
        Temperature.Fahrenheit -> {
            Temp_Unit = " º F"
            convertFromCelsiusToFahrenheit(temp.toDouble()).toString()}
        Temperature.Kelvin -> {
            Temp_Unit = " º K"
            convertFromCelsiusToKelvin(temp.toDouble()).toString()
        }
        else -> {
            Temp_Unit = " º C"
            temp
        }
    }
}

fun getWindSpeed(windSpeed : String,mySharedPref: MySharedPref) : String {

    return when(mySharedPref.getSetting().windSpeed){
        WindSpeed.Mile -> {
            WindSpeed_Unit = " mph"
            convertMeterToMile(windSpeed.toDouble()).toString()
        }
        else -> {
            WindSpeed_Unit = " m/s"
            windSpeed
        }
    }
}
 fun showSnackBar(activity: Activity,msg :String,icon : Int) {

    val snackBar: Snackbar = Snackbar.make(
        activity.findViewById(android.R.id.content), msg, 4000
    )
    val snackBarLayout = snackBar.view
    val textView =
        snackBarLayout.findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView
    textView.setCompoundDrawablesWithIntrinsicBounds(
        icon,
        0,
        0,
        0
    )
    textView.compoundDrawablePadding =
        activity.resources.getDimensionPixelOffset(R.dimen.snackBar_margin)
    snackBar.show()
}

fun getDate(strDate : String) : Date?{
    //val dtStart = "05-11-2017 05:42 PM"
    val format = SimpleDateFormat("dd-MM-yyyy hh:mm a")
    try {
        return format.parse(strDate)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return null
}



fun getCurrentDate():String{
    val sdf = SimpleDateFormat("hh:mm a   E, MMM dd, yyyy", Locale.getDefault())
    return sdf.format(Date())
}

fun getCurrentDateAsDate():Date{
    return Date()
}

//Celsius to Kelvin: K = C + 273.15
//Kelvin to Celcius: C = K - 273.15
//Fahrenheit to Celcius: C = (F-32) (5/9)
//Celsius to Fahrenheit: F = C(9/5) + 32
//Fahrenheit to Kelvin: K = (F-32) (5/9) + 273.15
//Kelvin to Fahrenheit: F = (K-273.15) (9/5) + 32