package eg.gov.iti.jets.weatherapp.favorite.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import eg.gov.iti.jets.weatherapp.R
import eg.gov.iti.jets.weatherapp.databinding.FavItemBinding
import eg.gov.iti.jets.weatherapp.model.Favourite
import eg.gov.iti.jets.weatherapp.utils.*

class FavAdapter(var favListener: FavListener) :
    ListAdapter<Favourite, FavAdapter.FavViewHolder>(FavDiffUtil()) {

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

        Picasso.get().load(getFlagIcon(favItem.countryCode!!))
            .placeholder(R.drawable.ic_launcher_foreground)
            .resize(200, 200)
            .into(holder.favItemBinding.favItemFlagImageView)

        holder.favItemBinding.favItemDeleteButton.setOnClickListener {
            favListener.deleteFav(favItem)
        }

        holder.favItemBinding.constraintLayout.setOnClickListener {
            favListener.navigateToDetails(favItem)
        }

        holder.favItemBinding.favItemLocationTextView.setOnClickListener {
            favListener.navigateToDetails(favItem)
        }
    }

    inner class FavViewHolder(var favItemBinding: FavItemBinding) :
        RecyclerView.ViewHolder(favItemBinding.root) {}

}
