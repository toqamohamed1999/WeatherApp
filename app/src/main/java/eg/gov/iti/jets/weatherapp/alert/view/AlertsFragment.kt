package eg.gov.iti.jets.weatherapp.alert.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import eg.gov.iti.jets.mymvvm.datatbase.LocaleSource
import eg.gov.iti.jets.mymvvm.model.Repo
import eg.gov.iti.jets.mymvvm.network.RemoteSource
import eg.gov.iti.jets.weatherapp.R
import eg.gov.iti.jets.weatherapp.alert.viewModel.AlertFragmentViewModel
import eg.gov.iti.jets.weatherapp.alert.viewModel.AlertViewModelFactory
import eg.gov.iti.jets.weatherapp.databinding.FragmentAlertsBinding


class AlertsFragment : Fragment() {

    private var _binding: FragmentAlertsBinding? = null
    private val binding get() = _binding!!


    private val viewModel: AlertFragmentViewModel by lazy {

        val factory = AlertViewModelFactory(
            Repo.getInstance(
                RemoteSource(), LocaleSource(requireContext())
            )!!
        )

        ViewModelProvider(this, factory)[AlertFragmentViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlertsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.alertFab.setOnClickListener {

            AlertDialogFragment.newInstance(getString(R.string.alert),
                getString(R.string.action_settings))
                .show(requireActivity().supportFragmentManager, AlertDialogFragment.TAG)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()