package eg.gov.iti.jets.weatherapp.map

import android.location.Address
import android.location.Geocoder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope


import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import eg.gov.iti.jets.weatherapp.R
import kotlinx.coroutines.launch

class MapFragment : DialogFragment() {
    lateinit var saveTv:TextView
    lateinit var searchBar: SearchView
    lateinit var geocoder:Geocoder


    companion object {

        const val TAG = "MapsFragment"

        fun newInstance(): MapFragment {
            val args = Bundle()
            val fragment = MapFragment()
            fragment.arguments = args
            return fragment
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var v:View = inflater.inflate(R.layout.fragment_map, container, false)
        saveTv = v.findViewById(R.id.save_tv)
        searchBar = v.findViewById(R.id.search_view)
//        favouriteViewModel = ViewModelProvider(this, FavouriteViewModelFactory(Repository(requireContext())))
//            .get(FavouriteViewModel::class.java)
        return v;
    }
    private val callback = OnMapReadyCallback { googleMap ->

        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                val location:String = searchBar.query.toString()
                var addressList : List<Address>

                if(location != null || location != ""){

                    geocoder = Geocoder(requireContext())
                    addressList = geocoder.getFromLocationName(location, 1)!!
                    var address:Address = addressList.get(0)
                     var city = addressList.get(0).getAddressLine(0)
                    var latLng:LatLng = LatLng(address.latitude, address.longitude)
                    googleMap.addMarker(MarkerOptions().position(latLng).title(location))
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10F))

                }
                return false
            }
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        googleMap.setOnMapClickListener {
            var lat: Double = 0.0
            var long: Double = 0.0

            val marker = MarkerOptions().apply {
                position(it)
                lat = it.latitude
                long = it.longitude
                title((lat).plus(long).toString())
                googleMap.clear()
                googleMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        it, 10f
                    )
                )
            }
            googleMap.addMarker(marker)


            saveTv.setOnClickListener {
                lifecycleScope.launch {
                   // favouriteViewModel.insertFavourite(Favourite(latitude = lat, longitude = long, city = city))
                }

                Toast.makeText(requireContext(),"inserted success", Toast.LENGTH_LONG ).show()
            }
        }

        googleMap.mapType

    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }



}