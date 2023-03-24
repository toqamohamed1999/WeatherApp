package eg.gov.iti.jets.weatherapp.home.view

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
import eg.gov.iti.jets.weatherapp.R
import eg.gov.iti.jets.weatherapp.databinding.FragmentHomeBinding
import eg.gov.iti.jets.weatherapp.home.viewModel.HomeViewModel
import eg.gov.iti.jets.weatherapp.home.viewModel.HomeViewModelFactory
import eg.gov.iti.jets.weatherapp.utils.MyCustomDialog
import eg.gov.iti.jets.weatherapp.utils.getIcon
import eg.gov.iti.jets.weatherapp.utils.getTime
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private val TAG = "HomeFragment"

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var homeViewModelFactory: HomeViewModelFactory

    private lateinit var dayAdapter: DayAdapter
    private lateinit var hourAdapter: HourAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpDayRecyclerView()
        setUpHourRecyclerView()
        setUpViewModel()
        setUpSettingDialog()
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

    private fun setUpSettingDialog(){
        val alert = MyCustomDialog()
        alert.showResetPasswordDialog(activity)
    }

    private fun setUpViewModel() {

        homeViewModelFactory =
            HomeViewModelFactory(
                Repo.getInstance(
                    RemoteSource(),
                    LocaleSource(requireContext())
                )!!
            )

        homeViewModel = ViewModelProvider(this, homeViewModelFactory).get(HomeViewModel::class.java)


        lifecycleScope.launch {
            homeViewModel.weatherStateFlow.collectLatest {
                when (it) {
                    is ApiState.Loading -> {
//                        allProductsBinding.progressbar.visibility = View.VISIBLE
//                        allProductsBinding.recyclerView.visibility = View.GONE
                    }
                    is ApiState.Success -> {
//                        allProductsBinding.progressbar.visibility = View.GONE
//                        allProductsBinding.recyclerView.visibility = View.VISIBLE
//                        productAdapter.submitList(it.myObject.products)


                        dayAdapter.submitList(it.weatherRoot.daily)
                        hourAdapter.submitList(it.weatherRoot.hourly)

                        Picasso.get().load(getIcon(it.weatherRoot.hourly[0].weather[0].icon))
                            .placeholder(R.drawable.ic_launcher_background)
                            .resize(200, 200)
                            .into(binding.homeWeatherImageView)

                        _binding?.homeStateTextView?.text = it.weatherRoot.hourly[0].weather[0].description
                        _binding?.homeLocationTextView?.text = it.weatherRoot.timezone
                        _binding?.homeTimeDateTextView?.text = getTime("hh:mm a   E, MMM dd, yyyy",it.weatherRoot.hourly[0].dt)
                        _binding?.homeTempTextView?.text = it.weatherRoot.hourly[0].temp+" C"
                        _binding?.additional?.humidityValueTextview?.text = it.weatherRoot.hourly[0].humidity+" %"
                        _binding?.additional?.pressureValueTextview?.text = it.weatherRoot.hourly[0].pressure+" hpa"
                        _binding?.additional?.windValueTextView?.text = it.weatherRoot.hourly[0].windSpeed+" m/s"

                        Log.i(TAG, "setUpViewModel: " + it.weatherRoot)

                        homeViewModel.insertWeather(it.weatherRoot)

                    }
                    else -> {
//                        allProductsBinding.progressbar.visibility = View.GONE
//                        Toast.makeText(this@AllProductsActivity,"check your network connection",
//                            Toast.LENGTH_LONG).show()
                    }

                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}