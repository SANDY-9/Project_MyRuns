package com.sandy.runningapp.ui.fragments

import android.Manifest
import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.CompoundButton
import android.widget.RadioButton
import android.widget.ToggleButton
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.sandy.runningapp.R
import com.sandy.runningapp.other.REQUEST_CODE_LOCATION_PERMISSION
import com.sandy.runningapp.other.SortType
import com.sandy.runningapp.other.TrackingUtility
import com.sandy.runningapp.ui.adapters.RunAdapter
import com.sandy.runningapp.ui.viewModels.MainViewModel
import com.sandy.runningapp.ui.viewModels.StatisticsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_run.*
import kotlinx.android.synthetic.main.fragment_run.view.*
import kotlinx.coroutines.delay
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class RunFragment : Fragment(R.layout.fragment_run), EasyPermissions.PermissionCallbacks {

    private val viewModel : MainViewModel by viewModels()
    private lateinit var runAdapter: RunAdapter

    init {
        Log.e("[테스트]", "네")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestPermissions()
        setupRecyclerView()
        viewModel.runs.observe(viewLifecycleOwner, {
            runAdapter.submitList(it)
        })

        fab.setOnClickListener {
            findNavController().navigate(R.id.action_runFragment_to_trackingFragment)
        }

        setupFilter()

    }

    private fun setupRecyclerView() = rvRuns.apply {
        runAdapter = RunAdapter()
        adapter = runAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupFilter() {
        btnSortDate.setOnCheckedChangeListener { buttonView, isChecked ->
            setupToggleState(buttonView, isChecked, SortType.DATE)
        }

        btnSortRT.setOnCheckedChangeListener { buttonView, isChecked ->
            setupToggleState(buttonView, isChecked, SortType.RUNNING_TIME)
        }

        btnSortDist.setOnCheckedChangeListener { buttonView, isChecked ->
            setupToggleState(buttonView, isChecked, SortType.DISTANCE)
        }

        btnSortAS.setOnCheckedChangeListener { buttonView, isChecked ->
            setupToggleState(buttonView, isChecked, SortType.AVG_SPEED)
        }

        btnSortCB.setOnCheckedChangeListener { buttonView, isChecked ->
            setupToggleState(buttonView, isChecked, SortType.CALORIES_BURNED)
        }
    }

    private fun setupToggleState(buttonView: CompoundButton, isChecked: Boolean, sortType: SortType) {
        if(isChecked) {
            buttonView.setTextColor(Color.WHITE)
            viewModel.sortRuns(sortType)
        } else {
            buttonView.setTextColor(Color.parseColor("#099AA1"))
        }
    }

    private fun requestPermissions() {
        if(TrackingUtility.hasRequestPermissions(requireContext())) {
            return
        }
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.requestPermissions(
                this,
                resources.getString(R.string.permission),
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } else {
            EasyPermissions.requestPermissions(
                this,
                resources.getString(R.string.permission),
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {}

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermissions()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}