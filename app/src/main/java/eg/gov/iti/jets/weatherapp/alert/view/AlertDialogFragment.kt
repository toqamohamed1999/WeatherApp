package eg.gov.iti.jets.weatherapp.alert.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import eg.gov.iti.jets.weatherapp.databinding.AlertDialogBinding
import java.util.*

//The order of execution of the above methods will be:
// onAttach -> onCreate -> onCreateDialog -> onCreateView -> onViewCreated -> onDestroy

class AlertDialogFragment : DialogFragment() {

    private var _binding: AlertDialogBinding? = null
    private val binding get() = _binding!!


    companion object {

        const val TAG = "AlertDialogFragment"

        private const val KEY_TITLE = "KEY_TITLE"
        private const val KEY_SUBTITLE = "KEY_SUBTITLE"

        fun newInstance(title: String, subTitle: String): AlertDialogFragment {
            val args = Bundle()
            args.putString(KEY_TITLE, title)
            args.putString(KEY_SUBTITLE, subTitle)
            val fragment = AlertDialogFragment()
            fragment.arguments = args
            return fragment
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AlertDialogBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fromDateTextview.setOnClickListener {
            showDateDialog(it as TextView)
        }
        binding.fromTimeTextview.setOnClickListener {
            showTimeDialog(it as TextView)
        }

        binding.toDateTextview.setOnClickListener {
            showDateDialog(it as TextView)
        }
        binding.toTimeTextview.setOnClickListener {
            showTimeDialog(it as TextView)
        }

        binding.cancelTv.setOnClickListener {
            dismiss()
        }

        binding.okTv.setOnClickListener {

        }


    }


    private var mDay: Int? = null
    private var mMinute: Int? = null
    private var mYear: Int? = null
    private var mMonth: Int? = null
    private var mHour: Int? = null

    private fun showDateDialog(dateTextView: TextView) {

        val c: Calendar = Calendar.getInstance()
        mYear = c.get(Calendar.YEAR)
        mMonth = c.get(Calendar.MONTH)
        mDay = c.get(Calendar.DAY_OF_MONTH)


        val datePickerDialog = DatePickerDialog(
            requireActivity(),
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                dateTextView.text =
                    (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
            },
            mYear!!,
            mMonth!!,
            mDay!!
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000;
        datePickerDialog.show()
    }

    private fun showTimeDialog(timeTextView: TextView) {
        val c2: Calendar = Calendar.getInstance()
        mHour = c2.get(Calendar.HOUR_OF_DAY)
        mMinute = c2.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireActivity(),
            { _, hourOfDay, minute ->
                timeTextView.text = "$hourOfDay:$minute"
            },
            mHour!!,
            mMinute!!,
            false
        )
        timePickerDialog.show()
    }

}