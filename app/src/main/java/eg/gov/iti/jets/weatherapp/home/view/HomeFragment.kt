package eg.gov.iti.jets.weatherapp.home.view

import android.content.pm.PackageManager
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
import eg.gov.iti.jets.foodplanner.NetworkChecker
import eg.gov.iti.jets.mymvvm.Utilites.ApiState
import eg.gov.iti.jets.mymvvm.datatbase.LocaleSource
import eg.gov.iti.jets.mymvvm.model.Repo
import eg.gov.iti.jets.mymvvm.network.RemoteSource
import eg.gov.iti.jets.weatherapp.LOCATION_PERMISSION_CODE
import eg.gov.iti.jets.weatherapp.MySharedPref
import eg.gov.iti.jets.weatherapp.R
import eg.gov.iti.jets.weatherapp.databinding.FragmentHomeBinding
import eg.gov.iti.jets.weatherapp.home.viewModel.HomeViewModel
import eg.gov.iti.jets.weatherapp.home.viewModel.HomeViewModelFactory
import eg.gov.iti.jets.weatherapp.location.MyLocation
import eg.gov.iti.jets.weatherapp.model.*
import eg.gov.iti.jets.weatherapp.setting.InitialSettingFragment
import eg.gov.iti.jets.weatherapp.utils.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class HomeFragment : Fragment(), LocationListener, MapListener {

    private val TAG = "HomeFragment"

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var dayAdapter: DayAdapter
    private lateinit var hourAdapter: HourAdapter

    private var weatherRoot: WeatherRoot? = null
    private lateinit var latitude: String
    private lateinit var longitude: String
    private lateinit var lang: String
    private var unit: String = "metric"


    private val mySharedPref by lazy {
        MySharedPref.getMyPref(requireContext())
    }

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
        binding.homeConstraintLayout.visibility = View.GONE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (mySharedPref.readISFIRST()) {
            showInitialSetting()
        } else {
            init()
        }

    }

    private fun init() {
        setUpDayRecyclerView()
        setUpHourRecyclerView()
        getSharedInfo()
        handleLocationActions()
        observeWeatherData()
    }

    private fun setUpDayRecyclerView() {
        dayAdapter = DayAdapter(mySharedPref)

        _binding?.homeDaysRecyclerView?.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = dayAdapter
        }
    }

    private fun setUpHourRecyclerView() {
        hourAdapter = HourAdapter(mySharedPref)
        val linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL

        _binding?.homeHoursRecyclerView?.apply {
            layoutManager = linearLayoutManager
            adapter = hourAdapter
        }
    }


    private fun showInitialSetting() {
        InitialSettingFragment.newInstance(this)
            .show(requireActivity().supportFragmentManager, InitialSettingFragment.TAG)
    }


    private fun observeWeatherData() {

        lifecycleScope.launch {
            viewModel.weatherStateFlow.collectLatest {
                when (it) {
                    is ApiState.Loading, RoomState.Loading -> {
                        binding.progressbar.visibility = View.VISIBLE
                        binding.homeConstraintLayout.visibility = View.GONE
                    }
                    is ApiState.Success -> {
                        binding.progressbar.visibility = View.GONE
                        binding.homeConstraintLayout.visibility = View.VISIBLE

                        weatherRoot = it.weatherRoot
                        updateUI(it.weatherRoot)

                    }
                    is RoomState.SuccessWeather -> {
                        binding.progressbar.visibility = View.GONE
                        binding.homeConstraintLayout.visibility = View.VISIBLE

                        weatherRoot = it.weatherRoot
                        updateUI(it.weatherRoot)
                    }
                    else -> {
                        binding.progressbar.visibility = View.GONE
                        Log.i(TAG, "getWeatherDataFromApi: $it")
                    }
                }
            }
        }
    }


    private fun handleLocationActions() {
        if (mySharedPref.getSetting().location == Location.GPS) {
            myLocation?.getLastLocation()
        } else if (mySharedPref.readLat() != "N/F" && mySharedPref.readLon() != "N/F") {
            latitude = mySharedPref.readLat()
            longitude = mySharedPref.readLon()
            getWeather()
        }
    }

    override fun setLocation(locationData: Pair<String, String>) {
        latitude = locationData.first
        longitude = locationData.second

        getWeather()
    }

    private fun getWeather() {
        if (NetworkChecker.isNetworkAvailable(requireContext())) {
            viewModel.getCurrentWeather(latitude, longitude, unit, lang)
        } else {
            showSnackBar()
            viewModel.getStoredWeather()
        }
    }


    private fun showSnackBar() {
        showSnackBar(requireActivity(),"No network Connection!", R.drawable.baseline_wifi_off_24)
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

    private fun updateUI(weatherRoot: WeatherRoot) {
        dayAdapter.submitList(weatherRoot.daily)
        hourAdapter.submitList(weatherRoot.hourly)

        Picasso.get().load(getIcon(weatherRoot.daily[0].weather[0].icon))
            .placeholder(eg.gov.iti.jets.weatherapp.R.drawable.ic_launcher_background)
            .resize(200, 200)
            .into(binding.homeWeatherImageView)

        _binding?.homeStateTextView?.text = weatherRoot.daily[0].weather[0].description
        _binding?.homeLocationTextView?.text = weatherRoot.timezone
        _binding?.homeTimeDateTextView?.text =
            getTime("hh:mm a   E, MMM dd, yyyy", weatherRoot.daily[0].dt)
        _binding?.additional?.humidityValueTextview?.text = weatherRoot.daily[0].humidity + " %"
        _binding?.additional?.pressureValueTextview?.text = weatherRoot.daily[0].pressure + " hpa"

        _binding?.additional?.cloudsValueTextview?.text = weatherRoot.daily[0].clouds
        _binding?.additional?.sunriseValueTextView?.text = getTime("hh:mm a",weatherRoot.daily[0].sunrise)
        _binding?.additional?.sunsetValueTextview?.text = getTime("hh:mm a",weatherRoot.daily[0].sunset)


        _binding?.homeTempTextView?.text =
            getSplitString(getTemp(weatherRoot.daily[0].temp.max, mySharedPref))
        _binding?.homeTempTextView?.append(
            " / " + getSplitString(
                getTemp(
                    weatherRoot.daily[0].temp.min,
                    mySharedPref
                )
            ) + Temp_Unit
        )

        _binding?.additional?.windValueTextView?.text =
            getWindSpeed(weatherRoot.daily[0].windSpeed, mySharedPref) + WindSpeed_Unit
    }

    private fun getSharedInfo() {
        val setting = mySharedPref.getSetting()
        lang = when (setting.language) {
            Language.English -> "eng"
            Language.Arabic -> "ar"
        }
    }


    override fun mapLocationSelected() {
        latitude = mySharedPref.readLat()
        longitude = mySharedPref.readLon()

        getWeather()
    }

    override fun confirmInitialSetting() {
        init()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()

        if (weatherRoot != null) {
            updateUI(weatherRoot!!)
        }
    }

}

//onPause() ---onStop()
//onStart() --- onResume()