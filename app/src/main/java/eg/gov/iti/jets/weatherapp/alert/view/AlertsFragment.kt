package eg.gov.iti.jets.weatherapp.alert.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.WorkManager
import eg.gov.iti.jets.mymvvm.datatbase.LocaleSource
import eg.gov.iti.jets.mymvvm.model.Repo
import eg.gov.iti.jets.mymvvm.network.RemoteSource
import eg.gov.iti.jets.weatherapp.R
import eg.gov.iti.jets.weatherapp.alert.viewModel.AlertFragmentViewModel
import eg.gov.iti.jets.weatherapp.alert.viewModel.AlertViewModelFactory
import eg.gov.iti.jets.weatherapp.databinding.FragmentAlertsBinding
import eg.gov.iti.jets.weatherapp.favorite.view.FavAdapter
import eg.gov.iti.jets.weatherapp.model.AlertModel
import eg.gov.iti.jets.weatherapp.model.Favourite
import eg.gov.iti.jets.weatherapp.utils.RoomState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class AlertsFragment : Fragment(),DeleteAlertListener {

    private var _binding: FragmentAlertsBinding? = null
    private val binding get() = _binding!!

    private lateinit var alertAdapter: AlertAdapter

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

        setUpFavRecyclerView()
        getStoredFavList()
        handleAlertFabClick()
    }

    private fun handleAlertFabClick(){
        binding.alertFab.setOnClickListener {

            AlertDialogFragment.newInstance(getString(R.string.alert),
                getString(R.string.action_settings))
                .show(requireActivity().supportFragmentManager, AlertDialogFragment.TAG)
        }
    }
    private fun setUpFavRecyclerView() {
        alertAdapter = AlertAdapter(this)

        _binding?.alertRecyclerView?.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = alertAdapter
        }
    }

    private fun getStoredFavList() {

        lifecycleScope.launch {
            viewModel.alertStateFlow.collectLatest {
                when (it) {
                    is RoomState.Loading -> {
//                        binding.progressbar.visibility = View.VISIBLE
//                        binding.homeConstraintLayout.visibility = View.GONE
                    }
                    is RoomState.SuccessAlert -> {
//                        binding.progressbar.visibility = View.GONE
//                        binding.homeConstraintLayout.visibility = View.VISIBLE

                        alertAdapter.submitList(it.alertModelList)

                    }
                    else -> {
                        // binding.progressbar.visibility = View.GONE

                    }

                }
            }
        }
    }

    override fun deleteAlert(alertModel: AlertModel) {
        lifecycleScope.launch {
            viewModel.deleteAlert(alertModel)
        }
        WorkManager.getInstance()?.cancelUniqueWork(id.toString())

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
