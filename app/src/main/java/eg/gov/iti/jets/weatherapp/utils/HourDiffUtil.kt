package eg.gov.iti.jets.mymvvm

import androidx.recyclerview.widget.DiffUtil
import eg.gov.iti.jets.weatherapp.model.Daily
import eg.gov.iti.jets.weatherapp.model.Hourly

class HourDiffUtil : DiffUtil.ItemCallback<Hourly>() {

    override fun areItemsTheSame(oldItem: Hourly, newItem: Hourly): Boolean {
        return oldItem.dt == newItem.dt
    }

    override fun areContentsTheSame(oldItem: Hourly, newItem: Hourly): Boolean {
        return oldItem == newItem
    }
}