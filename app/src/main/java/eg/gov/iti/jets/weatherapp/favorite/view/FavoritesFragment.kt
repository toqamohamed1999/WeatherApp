package eg.gov.iti.jets.weatherapp.favorite.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import eg.gov.iti.jets.weatherapp.R
import eg.gov.iti.jets.weatherapp.alert.view.AlertDialogFragment
import eg.gov.iti.jets.weatherapp.databinding.FragmentFavoritesBinding
import eg.gov.iti.jets.weatherapp.map.MapsFragment


class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

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

            MapsFragment.newInstance()
                .show(requireActivity().supportFragmentManager, MapsFragment.TAG)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}