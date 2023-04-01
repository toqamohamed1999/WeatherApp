package eg.gov.iti.jets.weatherapp.alert.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.work.*
import eg.gov.iti.jets.mymvvm.datatbase.LocaleSource
import eg.gov.iti.jets.mymvvm.model.Repo
import eg.gov.iti.jets.mymvvm.network.RemoteSource
import eg.gov.iti.jets.weatherapp.MySharedPref
import eg.gov.iti.jets.weatherapp.alert.AlertService
import eg.gov.iti.jets.weatherapp.alert.AlertWorker
import eg.gov.iti.jets.weatherapp.alert.viewModel.AlertDialogModelFactory
import eg.gov.iti.jets.weatherapp.alert.viewModel.AlertDialogViewModel
import eg.gov.iti.jets.weatherapp.databinding.AlertDialogBinding
import eg.gov.iti.jets.weatherapp.utils.getDate
import java.util.*
import java.util.concurrent.TimeUnit
import eg.gov.iti.jets.weatherapp.R
import eg.gov.iti.jets.weatherapp.model.AlertModel


//The order of execution of the above methods will be:
// onAttach -> onCreate -> onCreateDialog -> onCreateView -> onViewCreated -> onDestroy

class AlertDialogFragment : DialogFragment() {

    private var _binding: AlertDialogBinding? = null
    private val binding get() = _binding!!

    private var mDay: Int? = null
    private var mMinute: Int? = null
    private var mYear: Int? = null
    private var mMonth: Int? = null
    private var mHour: Int? = null

    private var startDate: String? = null
    private var endDate: String? = null
    private var startTime: String? = null
    private var endTime: String? = null

    private var fullStartDate: Date? = null
    private var fullEndDate: Date? = null

    private lateinit var alert : AlertModel


    private val mySharedPref by lazy {
        MySharedPref.getMyPref(requireContext())
    }

    private val viewModel: AlertDialogViewModel by lazy {

        val factory = AlertDialogModelFactory(
            Repo.getInstance(
                RemoteSource(), LocaleSource(requireContext())
            )!!
        )

        ViewModelProvider(this, factory)[AlertDialogViewModel::class.java]
    }

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
            WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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
            handleAlertClick()
        }

    }

    private fun handleAlertClick(){
        if(Settings.canDrawOverlays(requireContext())) {
            if (checkAlertTerms()) {
                alert = AlertModel(
                    latitude = mySharedPref.readLat(),
                    longitude = mySharedPref.readLon(),
                    startDate = fullStartDate.toString(),
                    endDate = fullEndDate.toString(),
                    address = mySharedPref.readAddress()
                )
                setUpAlertWorker()
                viewModel.insertAlert(alert)
                dismiss()
            }
        }else{
            checkOverlayPermission()
        }
    }

    private fun setUpAlertWorker() {
//        val data = Data.Builder()
//        data.putString("","")
//        data.build()

        var delay = getDelay(fullStartDate!!)
        Log.i(TAG, "setUpAlertWorker: alertttt $delay")

        var workRequest = PeriodicWorkRequestBuilder<AlertWorker>(
            1, TimeUnit.DAYS
        ).setInitialDelay(delay,TimeUnit.MILLISECONDS)

            //  .setInputData(data.build())
            .build()

        WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
            "$id",
            ExistingPeriodicWorkPolicy.REPLACE,
            workRequest
        )

        val workInfo =
            WorkManager.getInstance(requireContext()).getWorkInfoById(workRequest.id).get()


/*
        WorkManager.getInstance(requireContext()).getWorkInfoByIdLiveData(request.id).observe(requireActivity()
        ) {
            when (it.state) {
                WorkInfo.State.SUCCEEDED -> {
                    val myObjectGson = it.outputData.getString("output")
                    val myObject = Gson().fromJson(myObjectGson, MyObject::class.java)
                    productsList = myObject.products
                    productAdapter.submitList(productsList)
                }
                else -> {

                }
            }
        }

 */

    }

    private fun showDateDialog(dateTextView: TextView) {

        val c: Calendar = Calendar.getInstance()
        mYear = c.get(Calendar.YEAR)
        mMonth = c.get(Calendar.MONTH)
        mDay = c.get(Calendar.DAY_OF_MONTH)


        val datePickerDialog = DatePickerDialog(
            requireActivity(),
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->

                val strDate = dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year
                dateTextView.text = strDate

                when (dateTextView.id) {
                    R.id.from_date_textview -> startDate = strDate
                    else -> endDate = strDate
                }

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
            requireActivity(), { _, hourOfDay, minute ->

                timeTextView.text = "$hourOfDay:$minute"

                when (timeTextView.id) {
                    R.id.from_time_textview -> startTime = "$hourOfDay:$minute"
                    else -> endTime = "$hourOfDay:$minute"
                }

            }, mHour!!, mMinute!!, false
        )
        timePickerDialog.show()
    }



    private fun getDelay(hour : Int, minute:Int) : Long{
        val calendar: Calendar = Calendar.getInstance()
        val nowMillis: Long = calendar.timeInMillis

        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.DAY_OF_MONTH, 0)
        calendar.set(Calendar.MONTH, 0)
        calendar.set(Calendar.YEAR, 0)

        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1)
        }

        val diff: Long = calendar.timeInMillis - nowMillis

        Log.i(TAG, "getDelay: $diff")

        return diff
    }

    private fun getDelay(date: Date): Long {
        val calendar: Calendar = Calendar.getInstance()
        return date.time - calendar.timeInMillis
    }

    private fun checkOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(requireContext())) {
                Toast.makeText(requireContext(),"Give app permission to display alert over apps",Toast.LENGTH_LONG).show()
                // send user to the device settings
                val myIntent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + requireContext().packageName)
                )
                startActivity(myIntent)
            }
        }
    }

    private fun checkAlertTerms(): Boolean {
        if (startDate == null) {
            Toast.makeText(requireContext(), "Select start date", Toast.LENGTH_LONG).show()
            return false
        } else if (endDate == null) {
            Toast.makeText(requireContext(), "Select end date", Toast.LENGTH_LONG).show()
            return false
        } else if (startTime == null) {
            Toast.makeText(requireContext(), "Select start time", Toast.LENGTH_LONG).show()
            return false
        } else if (endTime == null) {
            Toast.makeText(requireContext(), "Select end time", Toast.LENGTH_LONG).show()
            return false
        }
        checkAlertDurationValidation()
        return true
    }

    private fun checkAlertDurationValidation(): Boolean {
        fullStartDate = getDate("$startDate $startTime")
        fullEndDate = getDate("$endDate $endTime")

        if (fullEndDate != null && fullStartDate != null) {
            if (fullEndDate!! >= fullStartDate) {
                return true
            } else {
                Toast.makeText(
                    requireContext(),
                    "End date should be after start date",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        return false
    }


    override fun onResume() {
        super.onResume()
      ///////////  startService()
    }

}