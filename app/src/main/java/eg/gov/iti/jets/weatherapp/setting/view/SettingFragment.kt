package eg.gov.iti.jets.weatherapp.setting.view

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import eg.gov.iti.jets.weatherapp.MainActivity
import eg.gov.iti.jets.weatherapp.MySharedPref
import eg.gov.iti.jets.weatherapp.R
import eg.gov.iti.jets.weatherapp.databinding.FragmentSettingBinding
import eg.gov.iti.jets.weatherapp.model.*
import java.util.*


class SettingFragment : Fragment() {

    private val TAG = "SettingFragment"

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    private val mySharedPref by lazy {
        MySharedPref.getMyPref(requireContext())
    }
    private lateinit var location : Location
    private lateinit var language : Language
    private lateinit var notification : Notification
    private lateinit var windSpeed : WindSpeed
    private lateinit var temperature : Temperature


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

        binding.saveTv.setOnClickListener {
            updatePref()
            applyLanguageChanges()
        }
    }

    private fun getMyPref(){
        location = mySharedPref.getSetting().location
        language = mySharedPref.getSetting().language
        notification = mySharedPref.getSetting().notification
        windSpeed = mySharedPref.getSetting().windSpeed
        temperature = mySharedPref.getSetting().temperature
    }

    private fun init() {
        getMyPref()
        setSetting()
        handleLanguageSelected()
        handleLocationSelected()
        handleNotificationSelected()
        handleTemperatureSelected()
        handleWindSpeedSelected()
    }


    private fun handleLanguageSelected() {
        _binding?.languageRadioGroup?.setOnCheckedChangeListener { _, optionId ->
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

    private fun handleWindSpeedSelected() {
        _binding?.windRadioGroup?.setOnCheckedChangeListener { _, optionId ->
            run {
                when (optionId) {
                    R.id.meter_radio_button -> {
                        windSpeed = WindSpeed.Meter
                    }
                    R.id.mile_radio_button -> {
                        windSpeed = WindSpeed.Mile
                    }
                }
            }
        }
    }

    private fun handleTemperatureSelected() {
        _binding?.tempRadioGroup?.setOnCheckedChangeListener { _, optionId ->
            run {
                when (optionId) {
                    R.id.celsius_radio_button -> {
                        temperature = Temperature.Celsius
                    }
                    R.id.kelvin_radio_button -> {
                        temperature = Temperature.Kelvin
                    }
                    R.id.fahrenheit_radio_button -> {
                        temperature = Temperature.Fahrenheit
                    }
                }
            }
        }
    }

    private fun handleNotificationSelected() {
        _binding?.notifRadioGroup?.setOnCheckedChangeListener { _, optionId ->
            run {
                when (optionId) {
                    R.id.enable_radio_button -> {
                        notification = Notification.Enable
                    }
                    R.id.disable_radio_button -> {
                        notification = Notification.Disable
                    }
                }
            }
        }
    }

    private fun updatePref() {
        mySharedPref.writeLanguage(language)
        mySharedPref.writeLocation(location)
        mySharedPref.writeTemperature(temperature)
        mySharedPref.writeWindSpeed(windSpeed)
        mySharedPref.writeNotification(notification)
    }

    private fun applyLanguageChanges() {
        if (language == Language.English)
            setLanguage("eng")
        else
            setLanguage("ar")
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

    private fun setSetting() {
        val setting = mySharedPref.getSetting()
        if (setting.language == Language.English)
            binding.englishRadioButton.isChecked = true
        else binding.arabicRadioButton.isChecked = true

        if (setting.notification == Notification.Enable)
            binding.enableRadioButton.isChecked = true
        else binding.disableRadioButton.isChecked = true

        if (setting.windSpeed == WindSpeed.Meter)
            binding.meterRadioButton.isChecked = true
        else binding.mileRadioButton.isChecked = true

        if (setting.location == Location.GPS)
            binding.gpsRadioButton.isChecked = true
        else binding.mapRadioButton.isChecked = true

        when (setting.temperature) {
            Temperature.Celsius -> binding.celsiusRadioButton.isChecked = true
            Temperature.Kelvin -> binding.kelvinRadioButton.isChecked = true
            else -> binding.fahrenheitRadioButton.isChecked = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}