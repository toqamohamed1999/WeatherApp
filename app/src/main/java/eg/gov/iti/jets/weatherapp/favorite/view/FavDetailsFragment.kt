package eg.gov.iti.jets.weatherapp.favorite.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import eg.gov.iti.jets.foodplanner.NetworkChecker
import eg.gov.iti.jets.mymvvm.Utilites.ApiState
import eg.gov.iti.jets.mymvvm.datatbase.LocaleSource
import eg.gov.iti.jets.mymvvm.model.Repo
import eg.gov.iti.jets.mymvvm.network.RemoteSource
import eg.gov.iti.jets.weatherapp.utils.MySharedPref
import eg.gov.iti.jets.weatherapp.R
import eg.gov.iti.jets.weatherapp.databinding.FragmentFavDetailsBinding
import eg.gov.iti.jets.weatherapp.favorite.viewModel.FavDetailsModelFactory
import eg.gov.iti.jets.weatherapp.favorite.viewModel.FavDetailsViewModel
import eg.gov.iti.jets.weatherapp.home.view.DayAdapter
import eg.gov.iti.jets.weatherapp.home.view.HourAdapter
import eg.gov.iti.jets.weatherapp.model.Favourite
import eg.gov.iti.jets.weatherapp.model.Language
import eg.gov.iti.jets.weatherapp.model.WeatherRoot
import eg.gov.iti.jets.weatherapp.utils.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FavDetailsFragment : Fragment() {

    private val TAG = "FavDetailsFragment"

    private var _binding: FragmentFavDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var dayAdapter: DayAdapter
    private lateinit var hourAdapter: HourAdapter

    private var weatherRoot: WeatherRoot? = null
    private var favourite: Favourite? = null
    private lateinit var latitude: String
    private lateinit var longitude: String
    private lateinit var lang: String
    private var unit: String = "metric"


    private val mySharedPref by lazy {
        MySharedPref.getMyPref(requireContext())
    }

    private val viewModel: FavDetailsViewModel by lazy {

        val factory = FavDetailsModelFactory(
            Repo.getInstance(
                RemoteSource(), LocaleSource(requireContext())
            )!!
        )

        ViewModelProvider(this, factory)[FavDetailsViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavDetailsBinding.inflate(inflater, container, false)
        binding.favConstraintLayout.visibility = View.GONE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getFav()
    }

    private fun init() {
        setUpDayRecyclerView()
        setUpHourRecyclerView()
        getSharedInfo()
        observeWeatherData()
        getWeather()
    }

    private fun getFav(){
        favourite = arguments?.getSerializable("fav") as Favourite
        if(favourite != null) {
            latitude = favourite?.latitude.toString()
            longitude = favourite?.longitude.toString()
            init()
        }
    }

    private fun setUpDayRecyclerView() {
        dayAdapter = DayAdapter(mySharedPref)

        _binding?.favDaysRecyclerView?.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = dayAdapter
        }
    }

    private fun setUpHourRecyclerView() {
        hourAdapter = HourAdapter(mySharedPref)
        val linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL

        _binding?.favHoursRecyclerView?.apply {
            layoutManager = linearLayoutManager
            adapter = hourAdapter
        }
    }

    private fun observeWeatherData() {

        lifecycleScope.launch {
            viewModel.favDetailsStateFlow.collectLatest {
                when (it) {
                    is ApiState.Loading -> {
                        binding.progressbar.visibility = View.VISIBLE
                        binding.lottieMapRound.visibility = View.VISIBLE
                        binding.favConstraintLayout.visibility = View.GONE
                        Log.i(TAG, "observeWeatherData: loading")
                    }
                    is ApiState.Success -> {
                        binding.progressbar.visibility = View.GONE
                        binding.lottieMapRound.visibility = View.GONE
                        binding.favConstraintLayout.visibility = View.VISIBLE

                        Log.i(TAG, "observeWeatherData: success")

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


    private fun getWeather() {
        if (NetworkChecker.isNetworkAvailable(requireContext())) {
            viewModel.getFavDetailsWeather(latitude, longitude, unit, lang)
        } else {
            binding.lottieNoInternet.visibility = View.VISIBLE
            binding.lottieMapRound.visibility = View.GONE
            showSnackBar()
        }
    }

    private fun showSnackBar() {
        showSnackBar(requireActivity(), "No network Connection!", R.drawable.baseline_wifi_off_24)
    }

    private fun getSharedInfo() {
        val setting = mySharedPref.getSetting()
        lang = when (setting.language) {
            Language.English -> "eng"
            Language.Arabic -> "ar"
        }
    }

    private fun updateUI(weatherRoot: WeatherRoot) {
        dayAdapter.submitList(weatherRoot.daily)
        hourAdapter.submitList(weatherRoot.hourly)

        Picasso.get().load(getIcon(weatherRoot.daily[0].weather[0].icon))
            .placeholder(eg.gov.iti.jets.weatherapp.R.drawable.ic_launcher_foreground)
            .resize(200, 200)
            .into(binding.favWeatherImageView)

        _binding?.favStateTextView?.text = weatherRoot.daily[0].weather[0].description
        _binding?.favLocationTextView?.text = weatherRoot.timezone
        _binding?.favTimeDateTextView?.text = getCurrentDate()
        // getTime("hh:mm a   E, MMM dd, yyyy", weatherRoot.daily[0].dt)
        _binding?.favAdditional?.humidityValueTextview?.text = "${weatherRoot.daily[0].humidity} %"
        _binding?.favAdditional?.pressureValueTextview?.text = "${weatherRoot.daily[0].pressure} hpa"

        _binding?.favAdditional?.cloudsValueTextview?.text = "${weatherRoot.daily[0].clouds} %"
        _binding?.favAdditional?.sunriseValueTextView?.text =
            getTime("hh:mm a", weatherRoot.daily[0].sunrise)
        _binding?.favAdditional?.sunsetValueTextview?.text =
            getTime("hh:mm a", weatherRoot.daily[0].sunset)


        _binding?.favTempTextView?.text =
            getSplitString(getTemp(weatherRoot.daily[0].temp.max, mySharedPref))
        _binding?.favTempTextView?.append(
            " / " + getSplitString(
                getTemp(
                    weatherRoot.daily[0].temp.min,
                    mySharedPref
                )
            ) + Temp_Unit
        )

        _binding?.favAdditional?.windValueTextView?.text =
            getWindSpeed(weatherRoot.daily[0].windSpeed, mySharedPref) + WindSpeed_Unit
    }

    override fun onResume() {
        super.onResume()

        if (weatherRoot != null) {
            updateUI(weatherRoot!!)
        }
    }


}