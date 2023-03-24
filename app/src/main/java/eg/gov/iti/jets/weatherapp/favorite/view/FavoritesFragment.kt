package eg.gov.iti.jets.weatherapp.favorite.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import eg.gov.iti.jets.mymvvm.datatbase.LocaleSource
import eg.gov.iti.jets.mymvvm.model.Repo
import eg.gov.iti.jets.mymvvm.network.RemoteSource
import eg.gov.iti.jets.weatherapp.databinding.FragmentFavoritesBinding
import eg.gov.iti.jets.weatherapp.favorite.viewModel.FavouriteFragmentViewModel
import eg.gov.iti.jets.weatherapp.favorite.viewModel.FavouriteViewModelFactory
import eg.gov.iti.jets.weatherapp.map.MapFragment


class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!


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
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.favFab.setOnClickListener {

            MapFragment.newInstance()
                .show(requireActivity().supportFragmentManager, MapFragment.TAG)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}