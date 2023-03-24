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
        MySharedPref(requireContext())
    }
    private var location = Location.GPS
    private var language = Language.English
    private var notification = Notification.Enable
    private var windSpeed = WindSpeed.Meter
    private var temperature = Temperature.Celsius


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

    private fun init() {
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
        _binding?.locationRadioGroup?.setOnCheckedChangeListener { _, optionId ->
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

    private fun applyLanguageChanges(){
        if(language == Language.English)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}