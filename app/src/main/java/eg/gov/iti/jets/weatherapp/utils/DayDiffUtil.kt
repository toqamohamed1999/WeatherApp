package eg.gov.iti.jets.mymvvm

import androidx.recyclerview.widget.DiffUtil
import eg.gov.iti.jets.weatherapp.model.Daily

class DayDiffUtil : DiffUtil.ItemCallback<Daily>() {

    override fun areItemsTheSame(oldItem: Daily, newItem: Daily): Boolean {
        return oldItem.dt == newItem.dt
    }

    override fun areContentsTheSame(oldItem: Daily, newItem: Daily): Boolean {
        return oldItem == newItem
    }
}