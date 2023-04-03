package eg.gov.iti.jets.weatherapp.home.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import eg.gov.iti.jets.mymvvm.DayDiffUtil
import eg.gov.iti.jets.weatherapp.utils.MySharedPref
import eg.gov.iti.jets.weatherapp.R
import eg.gov.iti.jets.weatherapp.databinding.DayItemBinding
import eg.gov.iti.jets.weatherapp.model.Daily
import eg.gov.iti.jets.weatherapp.utils.*


class DayAdapter(private val mySharedPref: MySharedPref) : ListAdapter<Daily, DayAdapter.DayViewHolder>(DayDiffUtil()) {

        private lateinit var dayItemBinding: DayItemBinding

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
            //view binding
            val inflater =
                parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            dayItemBinding = DayItemBinding.inflate(inflater, parent, false)
            return DayViewHolder(dayItemBinding)
        }

        override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
            val day = getItem(position)

            Picasso.get().load(getIcon(day.weather[0].icon))
                .placeholder(R.drawable.ic_launcher_foreground)
                .resize(200, 200)
                .into(holder.dayItemBinding.dayWeatherIcon)
            holder.dayItemBinding.dayName.text = getTime("E, MMM dd",day.dt)
            holder.dayItemBinding.dayState.text = day.weather[0].description
            holder.dayItemBinding.dayMaxTemp.text = getSplitString(getTemp(day.temp.max, mySharedPref))
            holder.dayItemBinding.dayMaxTemp.append(" / "+getSplitString(getTemp(day.temp.min, mySharedPref))+ Temp_Unit)

        }

        inner class DayViewHolder(var dayItemBinding: DayItemBinding) :
            RecyclerView.ViewHolder(dayItemBinding.root) {}
}