package eg.gov.iti.jets.weatherapp.favorite.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import eg.gov.iti.jets.mymvvm.datatbase.LocaleSource
import eg.gov.iti.jets.mymvvm.model.Repo
import eg.gov.iti.jets.mymvvm.network.RemoteSource
import eg.gov.iti.jets.weatherapp.R
import eg.gov.iti.jets.weatherapp.databinding.FragmentFavoritesBinding
import eg.gov.iti.jets.weatherapp.favorite.viewModel.FavouriteFragmentViewModel
import eg.gov.iti.jets.weatherapp.favorite.viewModel.FavouriteViewModelFactory
import eg.gov.iti.jets.weatherapp.map.view.MapsFragment
import eg.gov.iti.jets.weatherapp.model.Favourite
import eg.gov.iti.jets.weatherapp.utils.RoomState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class FavoritesFragment : Fragment(),FavListener {

    private val TAG = "FavoritesFragment"

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private lateinit var favAdapter: FavAdapter

    private val viewModel: FavouriteFragmentViewModel by lazy {

        val factory = FavouriteViewModelFactory(
            Repo.getInstance(
                RemoteSource(), LocaleSource(requireContext())
            )!!
        )

        ViewModelProvider(this, factory)[FavouriteFragmentViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpFavRecyclerView()
        getStoredFavList()

        binding.favFab.setOnClickListener {
            MapsFragment.newInstance("fav",null)
                .show(requireActivity().supportFragmentManager, MapsFragment.TAG)
        }
    }

    private fun setUpFavRecyclerView() {
        favAdapter = FavAdapter(this)

        _binding?.favRecyclerView?.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = favAdapter
        }
    }

    private fun getStoredFavList() {

        lifecycleScope.launch {
            viewModel.favStateFlow.collectLatest {
                when (it) {
                    is RoomState.Loading -> {
//                        binding.progressbar.visibility = View.VISIBLE
//                        binding.homeConstraintLayout.visibility = View.GONE
                    }
                    is RoomState.SuccessFavourite -> {
//                        binding.progressbar.visibility = View.GONE
//                        binding.homeConstraintLayout.visibility = View.VISIBLE

                        favAdapter.submitList(it.favouriteList)

                    }
                    else -> {
                //        binding.progressbar.visibility = View.GONE

                    }

                }
            }
        }
    }

    override fun deleteFav(favourite: Favourite) {
        lifecycleScope.launch {
            viewModel.deleteFavourite(favourite)
        }
    }

    override fun navigateToDetails(favourite: Favourite) {
        var bundle = Bundle()
        bundle.putSerializable("fav",favourite)
        findNavController().navigate(R.id.action_nav_favorites_to_favDetailsFragment2,bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}