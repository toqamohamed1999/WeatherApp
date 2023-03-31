package eg.gov.iti.jets.weatherapp.setting

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import eg.gov.iti.jets.weatherapp.MySharedPref
import eg.gov.iti.jets.weatherapp.R
import eg.gov.iti.jets.weatherapp.databinding.FragmentSettingBinding
import eg.gov.iti.jets.weatherapp.map.view.MapsFragment
import eg.gov.iti.jets.weatherapp.model.*
import java.util.*


class SettingFragment : Fragment() {

    private val TAG = "SettingFragment"

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    private val mySharedPref by lazy {
        MySharedPref.getMyPref(requireContext())
    }
    private lateinit var location: Location
    private lateinit var language: Language
    private lateinit var notification: Notification
    private lateinit var windSpeed: WindSpeed
    private lateinit var temperature: Temperature


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
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

    private fun getMyPref() {
        location = mySharedPref.getSetting().location
        language = mySharedPref.getSetting().language
        notification = mySharedPref.getSetting().notification
        windSpeed = mySharedPref.getSetting().windSpeed
        temperature = mySharedPref.getSetting().temperature
    }


    private fun handleLocationSelected() {
        _binding?.locationRadioGroup?.setOnCheckedChangeListener { _, optionId ->
            run {
                when (optionId) {
                    R.id.gps_radio_button -> {
                        location = Location.GPS
                    }
                    R.id.map_radio_button -> {
                        showAlertDialog(requireContext())
                    }
                }
                mySharedPref.writeLocation(location)
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
                mySharedPref.writeWindSpeed(windSpeed)
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
                mySharedPref.writeTemperature(temperature)
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
                mySharedPref.writeNotification(notification)
            }
        }
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
                mySharedPref.writeLanguage(language)
                applyLanguageChanges()
            }
        }

    }


    private fun applyLanguageChanges() {
        if (language == Language.English)
            setLanguage("eng")
        else
            setLanguage("ar")
    }


    private fun setLanguage(language: String) {
        val configuration = resources.configuration
        configuration.locale = Locale(language)
        Locale.setDefault(Locale(language))
        configuration.setLayoutDirection(Locale(language))
        // update configuration change
        resources.updateConfiguration(configuration, resources.displayMetrics)
        // notify configuration change
        onConfigurationChanged(configuration)
        requireActivity().recreate()
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


    private fun showAlertDialog(context: Context) {
        AlertDialog.Builder(context)
            .setTitle("Open Map")
            .setMessage("Do you want to choose your location from a map?")
            .setPositiveButton(android.R.string.ok,
                DialogInterface.OnClickListener { _, _ ->
                    openMap()
                    mySharedPref.writeLocation(Location.Map)
                })
            .setNegativeButton(android.R.string.cancel) { _, _ ->
                binding.gpsRadioButton.isChecked = true
            }
            .setIcon(android.R.drawable.ic_dialog_map)
            .show()
    }

    private fun openMap() {
        MapsFragment.newInstance("setting", null)
            .show(requireActivity().supportFragmentManager, MapsFragment.TAG)
    }

}
/*
        private fun setLanguage(lang: String?) {
            val myLocale = Locale(lang)
            val res = resources
            val dm: DisplayMetrics = res.displayMetrics
            val conf: Configuration = res.configuration
            conf.locale = myLocale
            res.updateConfiguration(conf, dm)

            // refresh()

            onConfigurationChanged(conf)
        }
*/
