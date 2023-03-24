package eg.gov.iti.jets.weatherapp.setting.view

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import eg.gov.iti.jets.weatherapp.R
import eg.gov.iti.jets.weatherapp.databinding.FragmentSettingBinding
import java.util.*


class SettingFragment : Fragment() {

    private val TAG = "SettingFragment"

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!
    var myContext: Context? = null
    var myresources: Resources? = null


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

        handleLanguageSelected()
    }

    private fun handleLanguageSelected(){
        _binding?.languageRadioGroup?.setOnCheckedChangeListener { radioGroup, optionId ->
            run {
                when (optionId) {
                    R.id.english_radio_button -> {
                       // LocaleHelper.setLocale(requireContext(),"eng")
//                        myContext = LocaleHelper.setLocale(requireContext(), "en");
//                        myresources = requireContext().resources
                      //  setLan("en")
                        Toast.makeText(requireContext(), "Apply English language", Toast.LENGTH_LONG).show();
                    }
                    R.id.arabic_radio_button -> {
                       // LocaleHelper.setLocale(requireContext(),"ar")
//                        myContext = LocaleHelper.setLocale(requireContext(), "ar");
//                        myresources = requireContext().resources
                      //  setLan("ar")
                      //  setLocale("ar")
                        //setLocale(requireActivity(),"ar")
                        setLan("ar")

                        Toast.makeText(requireContext(), "تنفيذ اللغة العربية", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }

    }


    private fun setLocale(lang: String?) {
        val myLocale = Locale(lang)
        val res = resources
        val dm: DisplayMetrics = res.displayMetrics
        val conf: Configuration = res.configuration
        conf.locale = myLocale
        res.updateConfiguration(conf, dm)
//        val refresh = Intent(requireActivity(), MainActivity::class.java)
//        requireActivity().finish()
//        startActivity(refresh)

        onConfigurationChanged(conf)
    }

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



    override fun onDestroyView() {
        super.onDestroyView()
      //  _binding = null
    }


}