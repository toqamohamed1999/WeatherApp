package eg.gov.iti.jets.weatherapp.utils

import androidx.recyclerview.widget.DiffUtil
import eg.gov.iti.jets.weatherapp.model.AlertModel

class AlertDiffUtil : DiffUtil.ItemCallback<AlertModel>() {

    override fun areItemsTheSame(oldItem: AlertModel, newItem: AlertModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: AlertModel, newItem: AlertModel): Boolean {
        return oldItem == newItem
    }
}