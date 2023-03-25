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
import eg.gov.iti.jets.mymvvm.datatbase.LocaleSource
import eg.gov.iti.jets.mymvvm.model.Repo
import eg.gov.iti.jets.mymvvm.network.RemoteSource
import eg.gov.iti.jets.weatherapp.R
import eg.gov.iti.jets.weatherapp.databinding.FragmentMapsBinding
import eg.gov.iti.jets.weatherapp.map.viewModel.MapFragmentViewModel
import eg.gov.iti.jets.weatherapp.map.viewModel.MapViewModelFactory
import eg.gov.iti.jets.weatherapp.model.Favourite
import kotlinx.coroutines.launch
import java.util.*

class MapsFragment : DialogFragment() {

    private var myAddress: String = ""
    private var lat: Double = 0.0
    private var lon: Double = 0.0
    private lateinit var favourite: Favourite

    private val geocoder by lazy {
        Geocoder(requireContext(), Locale.getDefault())
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
        fun newInstance(): MapsFragment {
            val args = Bundle()
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

        binding.saveTv.setOnClickListener {
            if (favourite != null) {
                insertFav(favourite)
                dismiss()
            } else {
                Toast.makeText(requireContext(), "Select city from map", Toast.LENGTH_LONG).show()
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

            favourite = Favourite(latitude = lat, longitude = lon, address = myAddress)
        }
    }

    private fun handleSearchView(googleMap: GoogleMap) {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {

                val location: String = binding.searchView.query.toString()

                var latLng: LatLng? = getAddress(location)

                if (latLng != null) {
                    googleMap.addMarker(MarkerOptions().position(latLng).title(location))
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10F))
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

            LatLng(address.latitude, address.longitude)

        } else {
            null
        }
    }

    private fun getAddress() {
        val addressList = geocoder.getFromLocation(lat, lon, 1)!!
        myAddress = addressList[0].getAddressLine(0)
    }

    private fun insertFav(favourite: Favourite) {
        lifecycleScope.launch {
            viewModel.insertFavourite(favourite)
        }
        Toast.makeText(requireContext(), "Inserted to favourites", Toast.LENGTH_LONG).show()
    }


}