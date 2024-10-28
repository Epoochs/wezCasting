package com.example.wezcasting.View.Settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.wezcasting.R
import com.example.wezcasting.View.HomeSettingsSharedVM

class SettingsFragment : Fragment() {

    private lateinit var sharedVM: HomeSettingsSharedVM

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedVM = ViewModelProvider(requireActivity()).get(HomeSettingsSharedVM::class.java)
        sharedVM.setLang("en")
        sharedVM.setTempUnit("k")
        sharedVM.setWindUnit("mph")
        sharedVM.setUnit("")
    }
}