package com.sandy.runningapp.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.sandy.runningapp.R
import com.sandy.runningapp.other.KEY_NAME
import com.sandy.runningapp.other.KEY_WEIGHT
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.fragment_setup.etName
import kotlinx.android.synthetic.main.fragment_setup.etWeight
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {

    @Inject
    lateinit var sharedPrefs : SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadFieldsFromSharedPrefs()
        btnApplyChanges.setOnClickListener {
            val success = applyChangesToSharedPrefs()
            Snackbar.make(view, if(success) "Saved changes" else "Please fill out all the fields", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun loadFieldsFromSharedPrefs() {
        val name = sharedPrefs.getString(KEY_NAME, "")
        val weight = sharedPrefs.getFloat(KEY_WEIGHT, 60f)
        etName.setText(name)
        etWeight.setText(weight.toInt().toString())
    }

    private fun applyChangesToSharedPrefs() : Boolean {
        val nameText = etName.text.toString()
        val weightText = etWeight.text.toString()
        return if(nameText.isBlank() || weightText.isBlank()) false else {
            sharedPrefs.edit()
                .putString(KEY_NAME, nameText)
                .putFloat(KEY_WEIGHT, weightText.toFloat())
                .apply()
            requireActivity().toolbarText.text = "Let's go, $nameText !"
            true
        }
    }

}