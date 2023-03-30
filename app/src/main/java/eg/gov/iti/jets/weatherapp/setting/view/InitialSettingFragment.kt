package eg.gov.iti.jets.weatherapp.setting.view

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.CompoundButton
import androidx.fragment.app.DialogFragment
import eg.gov.iti.jets.weatherapp.MainActivity
import eg.gov.iti.jets.weatherapp.MySharedPref
import eg.gov.iti.jets.weatherapp.R
import eg.gov.iti.jets.weatherapp.databinding.InitialSettingDialogBinding
import eg.gov.iti.jets.weatherapp.home.view.MapListener
import eg.gov.iti.jets.weatherapp.map.view.MapsFragment
import eg.gov.iti.jets.weatherapp.model.*
import java.util.*


class InitialSettingFragment : DialogFragment() {

    private var _binding: InitialSettingDialogBinding? = null
    private val binding get() = _binding!!

    private val mySharedPref by lazy {
        MySharedPref.getMyPref(requireContext())
    }
    private var location = Location.GPS
    private var language = Language.English
    private var notification = Notification.Enable


    companion object {

        const val TAG = "AlertDialogFragment"

        private lateinit var mapListener : MapListener

        fun newInstance(mapListener1: MapListener): InitialSettingFragment {
            val args = Bundle()
            mapListener = mapListener1
            val fragment = InitialSettingFragment()
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
        _binding = InitialSettingDialogBinding.inflate(inflater, container, false)
        dialog?.setCancelable(false);
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initSharedPref()
        handleLanguageSelected()
        handleLocationSelected()
        handleNotificationSelected()

        binding.okTv.setOnClickListener {
            updatePref()
            handleInitialSetting()
            dismiss()
            mapListener.confirmInitialSetting()
        }


    }

    private fun handleLanguageSelected() {
        _binding?.languagRadioGroup?.setOnCheckedChangeListener { _, optionId ->
            run {
                when (optionId) {
                    R.id.english_radio_button -> {
                        language = Language.English
                    }
                    R.id.arabic_radio_button -> {
                        language = Language.Arabic
                    }
                }
            }
        }

    }

    private fun handleLocationSelected() {
        _binding?.locationRadioGroup?.setOnCheckedChangeListener { _, optionId ->
            run {
                when (optionId) {
                    R.id.gps_radio_button -> {
                        location = Location.GPS
                    }
                    R.id.map_radio_button -> {
                        location = Location.Map
                    }
                }
            }
        }
    }

    private fun handleNotificationSelected() {
        _binding?.notificationSwitch?.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { _, isChecked ->

            notification = if (isChecked) {
                Notification.Enable
            } else {
                Notification.Disable
            }
        })
    }

    private fun updatePref(){
        mySharedPref.writeIsFirst(false)
        mySharedPref.writeLanguage(language)
        mySharedPref.writeLocation(location)
        mySharedPref.writeNotification(notification)
    }

    private fun initSharedPref(){
        mySharedPref.writeIsFirst(true)
        mySharedPref.writeLanguage(Language.English)
        mySharedPref.writeLocation(Location.GPS)
        mySharedPref.writeTemperature(Temperature.Celsius)
        mySharedPref.writeWindSpeed(WindSpeed.Meter)
        mySharedPref.writeNotification(Notification.Enable)
    }

    private fun handleInitialSetting(){
        if(language == Language.Arabic)
            setLanguage("ar")

        if(location == Location.Map)
            MapsFragment.newInstance("initialSetting", mapListener)
                .show(requireActivity().supportFragmentManager, MapsFragment.TAG)
    }
    private fun setLanguage(lang: String?) {
        val myLocale = Locale(lang)
        val res = resources
        val dm: DisplayMetrics = res.displayMetrics
        val conf: Configuration = res.configuration
        conf.locale = myLocale
        res.updateConfiguration(conf, dm)

        refresh()

        //  onConfigurationChanged(conf)
    }


/*
    private fun setLan(language: String) {
        val metric = resources.displayMetrics
        val configuration = resources.configuration
        configuration.locale = Locale(language)
        Locale.setDefault(Locale(language))
        configuration.setLayoutDirection(Locale(language))
        // update configuration
        resources.updateConfiguration(configuration, metric)
        // notify configuration
        onConfigurationChanged(configuration)
       // requireActivity().recreate()
    }

*/

    private fun refresh() {
        val refresh = Intent(requireActivity(), MainActivity::class.java)
        requireActivity().finish()
        startActivity(refresh)
    }
}


