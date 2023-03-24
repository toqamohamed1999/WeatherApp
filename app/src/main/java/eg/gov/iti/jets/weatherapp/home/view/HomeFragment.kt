package eg.gov.iti.jets.weatherapp.home.view

import android.R
import android.R.layout
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import eg.gov.iti.jets.mymvvm.Utilites.ApiState
import eg.gov.iti.jets.mymvvm.datatbase.LocaleSource
import eg.gov.iti.jets.mymvvm.model.Repo
import eg.gov.iti.jets.mymvvm.network.RemoteSource
import eg.gov.iti.jets.weatherapp.LOCATION_PERMISSION_CODE
import eg.gov.iti.jets.weatherapp.MySharedPref
import eg.gov.iti.jets.weatherapp.databinding.FragmentHomeBinding
import eg.gov.iti.jets.weatherapp.home.viewModel.HomeViewModel
import eg.gov.iti.jets.weatherapp.home.viewModel.HomeViewModelFactory
import eg.gov.iti.jets.weatherapp.location.MyLocation
import eg.gov.iti.jets.weatherapp.model.WeatherRoot
import eg.gov.iti.jets.weatherapp.setting.view.InitialSettingFragment
import eg.gov.iti.jets.weatherapp.utils.getIcon
import eg.gov.iti.jets.weatherapp.utils.getSplitString
import eg.gov.iti.jets.weatherapp.utils.getTime
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class HomeFragment : Fragment(), LocationListener {

    private val TAG = "HomeFragment"

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!


    private lateinit var dayAdapter: DayAdapter
    private lateinit var hourAdapter: HourAdapter

    private lateinit var latitude: String
    private lateinit var longitude: String
    private lateinit var address: String

    private var unit: String = "metric"
    private var lang: String = "eng"

    private val myLocation by lazy {
        activity?.let { MyLocation(it, this) }
    }


    private val viewModel: HomeViewModel by lazy {

        val factory = HomeViewModelFactory(
            Repo.getInstance(
                RemoteSource(), LocaleSource(requireContext())
            )!!
        )

        ViewModelProvider(this, factory)[HomeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.homeConstraintLayout.visibility = View.GONE
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpDayRecyclerView()
        setUpHourRecyclerView()
        showInitialSetting()
        myLocation?.getLastLocation()
    }

    private fun setUpDayRecyclerView() {
        dayAdapter = DayAdapter()

        _binding?.homeDaysRecyclerView?.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = dayAdapter
        }
    }

    private fun setUpHourRecyclerView() {
        hourAdapter = HourAdapter()
        val linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL

        _binding?.homeHoursRecyclerView?.apply {
            layoutManager = linearLayoutManager
            adapter = hourAdapter
        }
    }


    private fun showInitialSetting() {
        if (MySharedPref(requireContext()).readISFIRST()) {
            InitialSettingFragment.newInstance()
                .show(requireActivity().supportFragmentManager, InitialSettingFragment.TAG)
        }
    }

    private fun getWeatherData() {
        Log.i(TAG, "getWeatherData: ttt")
        viewModel.getCurrentWeather(latitude, longitude, unit, lang)

        lifecycleScope.launch {
            viewModel.weatherStateFlow.collectLatest {
                when (it) {
                    is ApiState.Loading -> {
                        binding.progressbar.visibility = View.VISIBLE
                        binding.homeConstraintLayout.visibility = View.GONE
                    }
                    is ApiState.Success -> {
                        binding.progressbar.visibility = View.GONE
                        binding.homeConstraintLayout.visibility = View.VISIBLE

                        updateUI(it.weatherRoot)
                        viewModel.insertWeather(it.weatherRoot)

                    }
                    else -> {
                        binding.progressbar.visibility = View.GONE

//                        Snackbar.make(
//                            binding.root,
//                            "Connection is not available",
//                            Snackbar.LENGTH_LONG
//                        )
//                            .setAction("Action", null)
//                            .show()
                    }

                }
            }
        }
    }


    private fun updateUI(weatherRoot: WeatherRoot) {
        dayAdapter.submitList(weatherRoot.daily)
        hourAdapter.submitList(weatherRoot.hourly)

        Picasso.get().load(getIcon(weatherRoot.hourly[0].weather[0].icon))
            .placeholder(eg.gov.iti.jets.weatherapp.R.drawable.ic_launcher_background)
            .resize(200, 200)
            .into(binding.homeWeatherImageView)

        _binding?.homeStateTextView?.text = weatherRoot.hourly[0].weather[0].description
        _binding?.homeLocationTextView?.text = weatherRoot.timezone
        _binding?.homeTimeDateTextView?.text =
            getTime("hh:mm a   E, MMM dd, yyyy", weatherRoot.hourly[0].dt)
        _binding?.homeTempTextView?.text = getSplitString(weatherRoot.daily[0].temp.max)
        _binding?.homeTempTextView?.text = getSplitString(weatherRoot.daily[0].temp.min) + "º C"
        _binding?.additional?.humidityValueTextview?.text = weatherRoot.hourly[0].humidity + " %"
        _binding?.additional?.pressureValueTextview?.text = weatherRoot.hourly[0].pressure + " hpa"
        _binding?.additional?.windValueTextView?.text = weatherRoot.hourly[0].windSpeed + " m/s"
    }

    override fun setLocation(locationData: Triple<String, String, String>) {
        latitude = locationData.first
        longitude = locationData.second
        address = locationData.third

        // getWeatherData()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                myLocation?.getLastLocation()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}