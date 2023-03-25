package eg.gov.iti.jets.weatherapp.utils

import androidx.recyclerview.widget.DiffUtil
import eg.gov.iti.jets.weatherapp.model.Favourite

class FavDiffUtil : DiffUtil.ItemCallback<Favourite>() {

    override fun areItemsTheSame(oldItem: Favourite, newItem: Favourite): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Favourite, newItem: Favourite): Boolean {
        return oldItem == newItem
    }
}