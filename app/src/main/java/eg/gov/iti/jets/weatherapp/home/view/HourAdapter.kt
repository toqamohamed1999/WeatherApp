package eg.gov.iti.jets.weatherapp.home.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import eg.gov.iti.jets.mymvvm.HourDiffUtil
import eg.gov.iti.jets.weatherapp.MySharedPref
import eg.gov.iti.jets.weatherapp.R
import eg.gov.iti.jets.weatherapp.databinding.HourItemBinding
import eg.gov.iti.jets.weatherapp.model.Hourly
import eg.gov.iti.jets.weatherapp.utils.*

class HourAdapter(private val mySharedPref: MySharedPref) :
    ListAdapter<Hourly, HourAdapter.HourViewHolder>(HourDiffUtil()) {

    private lateinit var hourItemBinding: HourItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourViewHolder {
        //view binding
        val inflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        hourItemBinding = HourItemBinding.inflate(inflater, parent, false)
        return HourViewHolder(hourItemBinding)
    }

    override fun onBindViewHolder(holder: HourViewHolder, position: Int) {
        val hour = getItem(position)

        Picasso.get().load(getIcon(hour.weather[0].icon))
            .placeholder(R.drawable.ic_launcher_foreground)
            .resize(200, 200)
            .into(holder.hourItemBinding.hourWeatherIcon)
        holder.hourItemBinding.hourName.text = getTime("hh:mm a", hour.dt)
        holder.hourItemBinding.hourTemp.text =
            getSplitString(getTemp(hour.temp, mySharedPref)) + Temp_Unit
        holder.hourItemBinding.hourWindSpeed.text =
            getSplitString(getWindSpeed(hour.windSpeed, mySharedPref)) + WindSpeed_Unit

    }

    inner class HourViewHolder(var hourItemBinding: HourItemBinding) :
        RecyclerView.ViewHolder(hourItemBinding.root) {}
}