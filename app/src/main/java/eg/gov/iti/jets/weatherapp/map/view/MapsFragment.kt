package eg.gov.iti.jets.weatherapp.map.view


import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import eg.gov.iti.jets.foodplanner.NetworkChecker
import eg.gov.iti.jets.mymvvm.datatbase.LocaleSource
import eg.gov.iti.jets.mymvvm.model.Repo
import eg.gov.iti.jets.mymvvm.network.RemoteSource
import eg.gov.iti.jets.weatherapp.utils.MySharedPref
import eg.gov.iti.jets.weatherapp.R
import eg.gov.iti.jets.weatherapp.databinding.FragmentMapsBinding
import eg.gov.iti.jets.weatherapp.home.view.MapListener
import eg.gov.iti.jets.weatherapp.map.viewModel.MapFragmentViewModel
import eg.gov.iti.jets.weatherapp.map.viewModel.MapViewModelFactory
import eg.gov.iti.jets.weatherapp.model.Favourite
import kotlinx.coroutines.launch
import java.util.*


class MapsFragment : DialogFragment() {

    private var myAddress: String = ""
    private var lat: Double = 0.0
    private var lon: Double = 0.0
    private lateinit var countryCode: String

    private var favourite: Favourite? = null

    private val geocoder by lazy {
        Geocoder(requireContext(), Locale.getDefault())
    }

    private val mySharedPref by lazy {
        MySharedPref.getMyPref(requireContext())
    }

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MapFragmentViewModel by lazy {

        val factory = MapViewModelFactory(
            Repo.getInstance(
                RemoteSource(), LocaleSource(requireContext())
            )!!
        )
        ViewModelProvider(this, factory)[MapFragmentViewModel::class.java]
    }

    companion object {

        const val TAG = "MapsFragment"

        private var callerFragment = "fav"

        private var mapListener: MapListener? = null

        fun newInstance(callerFragment1: String, mapListener1: MapListener?): MapsFragment {
            val args = Bundle()
            callerFragment = callerFragment1
            mapListener = mapListener1
            val fragment = MapsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)


        handleSaveBtnClick()

    }

    private fun handleSaveBtnClick() {
        binding.saveTv.setOnClickListener {
            if (favourite != null) {
                checkCallerFragmentEvent()
                dismiss()
            } else {
                Toast.makeText(requireContext(), "Select city or pin from map", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun checkCallerFragmentEvent() {
        when (callerFragment) {
            "fav" -> {
                insertFav(favourite!!)
            }
            "initialSetting", "setting" -> {
                mySharedPref.writeLat(lat.toString())
                mySharedPref.writeLon(lon.toString())
                mapListener?.mapLocationSelected()
                Toast.makeText(requireContext(), "Apply map location", Toast.LENGTH_LONG).show()
            }
        }
    }

    private val callback = OnMapReadyCallback { googleMap ->

        handleSearchView(googleMap)
        handleOnMapClickListener(googleMap)

        googleMap.mapType
    }

    private fun handleOnMapClickListener(googleMap: GoogleMap) {
        googleMap.setOnMapClickListener {

            if (NetworkChecker.isNetworkAvailable(requireContext())) {

                val marker = MarkerOptions().apply {
                    position(it)

                    lat = it.latitude
                    lon = it.longitude
                    getAddress()
                    title(myAddress)


                    googleMap.clear()
                    googleMap.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            it, 10f
                        )
                    )
                }
                googleMap.addMarker(marker)

                favourite = Favourite(
                    latitude = lat,
                    longitude = lon,
                    address = myAddress,
                    countryCode = countryCode
                )
                showMySnackBar()
            }else{
                Snackbar
                    .make(binding.root, "No internet connection!", 4000)
                    .show()
            }
        }
    }

    private fun handleSearchView(googleMap: GoogleMap) {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {

                if(NetworkChecker.isNetworkAvailable(requireContext())) {

                    val location: String = binding.searchView.query.toString()

                    var latLng: LatLng? = getAddress(location)

                    if (latLng != null) {
                        googleMap.addMarker(MarkerOptions().position(latLng).title(location))
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10F))
                    }
                    favourite = Favourite(
                        latitude = lat,
                        longitude = lon,
                        address = myAddress,
                        countryCode = countryCode
                    )
                    showMySnackBar()
                }else{
                    Snackbar
                        .make(binding.root, "No internet connection!", 4000)
                        .show()
                }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

    }

    private fun getAddress(location: String): LatLng? {

        return if (location.isNotEmpty()) {
            val addressList = geocoder.getFromLocationName(location, 1)!!
            var address: Address = addressList[0]

            myAddress = address.getAddressLine(0)

            countryCode = address.countryCode

            LatLng(address.latitude, address.longitude)

        } else {
            null
        }
    }

    private fun getAddress() {
        Log.i(TAG, "getAddress: $lat  $lon")
        val addressList = geocoder.getFromLocation(lat, lon, 1)!!
        myAddress = addressList[0].getAddressLine(0)
        countryCode = addressList[0].countryCode
    }

    private fun insertFav(favourite: Favourite) {
        lifecycleScope.launch {
            viewModel.insertFavourite(favourite)
        }
        Toast.makeText(requireContext(), "saved to favourites", Toast.LENGTH_LONG).show()
    }

    private fun showMySnackBar() {
        val snackBar = Snackbar
            .make(binding.root, myAddress, 5000)
            .setAction("Save") {
                if (favourite != null) {
                    checkCallerFragmentEvent()
                    dismiss()
                }
            }
        snackBar.show()
    }


}