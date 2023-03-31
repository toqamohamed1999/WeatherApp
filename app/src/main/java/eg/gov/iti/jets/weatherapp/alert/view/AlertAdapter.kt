package eg.gov.iti.jets.weatherapp.alert.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import eg.gov.iti.jets.mymvvm.DayDiffUtil
import eg.gov.iti.jets.weatherapp.R
import eg.gov.iti.jets.weatherapp.databinding.AlertItemBinding
import eg.gov.iti.jets.weatherapp.databinding.DayItemBinding
import eg.gov.iti.jets.weatherapp.databinding.FavItemBinding
import eg.gov.iti.jets.weatherapp.home.view.DayAdapter
import eg.gov.iti.jets.weatherapp.model.AlertModel
import eg.gov.iti.jets.weatherapp.model.Daily
import eg.gov.iti.jets.weatherapp.model.Favourite
import eg.gov.iti.jets.weatherapp.utils.*

class AlertAdapter(var deleteAlertListener: DeleteAlertListener) :
    ListAdapter<AlertModel, AlertAdapter.AlertViewHolder>(AlertDiffUtil()) {

    private lateinit var alertItemBinding: AlertItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertViewHolder {
        //view binding
        val inflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        alertItemBinding = AlertItemBinding.inflate(inflater, parent, false)
        return AlertViewHolder(alertItemBinding)
    }

    override fun onBindViewHolder(holder: AlertViewHolder, position: Int) {
        val alert = getItem(position)

//        holder.alertItemBinding.favItemLocationTextView.text = favItem.address
//
//        Picasso.get().load(getFlagIcon(alert.countryCode!!))
//            .placeholder(R.drawable.ic_launcher_foreground)
//            .resize(200, 200)
//            .into(holder.alertItemBinding.favItemFlagImageView)

        holder.alertItemBinding.alertItemDeleteButton.setOnClickListener {
            deleteAlertListener.deleteAlert(alert)
        }
    }

    inner class AlertViewHolder(var alertItemBinding : AlertItemBinding) :
        RecyclerView.ViewHolder(alertItemBinding.root) {}

}
