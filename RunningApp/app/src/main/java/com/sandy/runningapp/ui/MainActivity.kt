package com.sandy.runningapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.sandy.runningapp.R
import com.sandy.runningapp.other.ACTION_SHOW_TRACKING_FRAGMENT
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @set:Inject
    var name = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigateToTrackingFragmentIfNeeded(intent)
        toolbarText.text = "Let's go, $name !"
        with(bottomNavigationView) {
            setupWithNavController(getNavController())
            setOnItemReselectedListener { /* NO-OP */ }
        }
    }

    private fun getNavController() = navHostFragment.findNavController().apply{
        graph.startDestination = R.id.runFragment
        addOnDestinationChangedListener { _, destination, _ ->
            when(destination.id) {
                R.id.settingsFragment, R.id.runFragment, R.id.statisticsFragment-> {
                    bottomNavigationView.visibility = View.VISIBLE
                    mainToolbar.visibility = View.VISIBLE
                }
                else -> {
                    bottomNavigationView.visibility = View.GONE
                    mainToolbar.visibility = View.GONE
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navigateToTrackingFragmentIfNeeded(intent)
    }

    private fun navigateToTrackingFragmentIfNeeded(intent: Intent?) {
        if(intent?.action == ACTION_SHOW_TRACKING_FRAGMENT) {
            navHostFragment.findNavController().navigate(R.id.action_global_trackingFragment)
        }
    }

}
