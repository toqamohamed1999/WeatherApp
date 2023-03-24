package eg.gov.iti.jets.weatherapp.home.view

interface LocationListener {

    fun setLocation(locationData : Triple<String,String,String>)
}