package eg.gov.iti.jets.weatherapp.favorite.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import eg.gov.iti.jets.mymvvm.DayDiffUtil
import eg.gov.iti.jets.weatherapp.R
import eg.gov.iti.jets.weatherapp.databinding.DayItemBinding
import eg.gov.iti.jets.weatherapp.databinding.FavItemBinding
import eg.gov.iti.jets.weatherapp.home.view.DayAdapter
import eg.gov.iti.jets.weatherapp.model.Daily
import eg.gov.iti.jets.weatherapp.model.Favourite
import eg.gov.iti.jets.weatherapp.utils.FavDiffUtil
import eg.gov.iti.jets.weatherapp.utils.getIcon
import eg.gov.iti.jets.weatherapp.utils.getSplitString
import eg.gov.iti.jets.weatherapp.utils.getTime

class FavAdapter(var deleteFavListener: DeleteFavListener) : ListAdapter<Favourite, FavAdapter.FavViewHolder>(FavDiffUtil()) {

    private lateinit var favItemBinding: FavItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        //view binding
        val inflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        favItemBinding = FavItemBinding.inflate(inflater, parent, false)
        return FavViewHolder(favItemBinding)
    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        val favItem = getItem(position)

        holder.favItemBinding.favItemLocationTextView.text = favItem.address

      //  holder.favItemBinding.favItemLatTextView.text = "${favItem.latitude.toString()} , ${favItem.longitude}"


        holder.favItemBinding.favItemDeleteButton.setOnClickListener{
                deleteFavListener.deleteFav(favItem)
            }
    }

    inner class FavViewHolder(var favItemBinding: FavItemBinding) :
        RecyclerView.ViewHolder(favItemBinding.root) {}

}
