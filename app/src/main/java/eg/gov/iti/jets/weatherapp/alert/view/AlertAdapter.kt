package eg.gov.iti.jets.weatherapp.alert.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import eg.gov.iti.jets.weatherapp.R
import eg.gov.iti.jets.weatherapp.databinding.AlertItemBinding
import eg.gov.iti.jets.weatherapp.model.AlertModel
import eg.gov.iti.jets.weatherapp.utils.*

class AlertAdapter(val context: Context,var deleteAlertListener: DeleteAlertListener) :
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

        holder.alertItemBinding.alertItemLocationTextView.text = alert.address

        holder.alertItemBinding.alertItemTimeTextView.text =
            context.resources.getString(R.string.from)+" :  "+alert.startDate?.substring(0, 16)+
        "\n "+ context.resources.getString(R.string.to)+" :  "+alert.endDate?.substring(0, 16)

        holder.alertItemBinding.alertItemDeleteButton.setOnClickListener {
            deleteAlertListener.deleteAlert(alert)
        }
    }

    inner class AlertViewHolder(var alertItemBinding : AlertItemBinding) :
        RecyclerView.ViewHolder(alertItemBinding.root) {}

}
