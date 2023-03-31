package eg.gov.iti.jets.weatherapp.alert.view

import eg.gov.iti.jets.weatherapp.model.AlertModel

interface DeleteAlertListener {

    fun deleteAlert(alertModel: AlertModel)
}