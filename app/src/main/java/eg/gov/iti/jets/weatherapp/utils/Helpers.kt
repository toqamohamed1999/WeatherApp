package eg.gov.iti.jets.weatherapp.utils

import java.text.SimpleDateFormat
import java.util.*

//"hh:mm a   E, MMM dd, yyyy"
fun getTime(pattern:String,date:Long): String =
    SimpleDateFormat(pattern, Locale.getDefault()).format(Date(date*1000))

fun getIcon(iconPath :String):String= "https://openweathermap.org/img/w/${iconPath}.png"


//Celsius to Kelvin: K = C + 273.15
//Kelvin to Celcius: C = K - 273.15
//Fahrenheit to Celcius: C = (F-32) (5/9)
//Celsius to Fahrenheit: F = C(9/5) + 32
//Fahrenheit to Kelvin: K = (F-32) (5/9) + 273.15
//Kelvin to Fahrenheit: F = (K-273.15) (9/5) + 32