package com.example.wezcasting.View.Settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.lifecycle.ViewModelProvider
import com.example.wezcasting.R
import com.example.wezcasting.HomeSettingsSharedVM

class SettingsFragment : Fragment() {

    private lateinit var sharedVM: HomeSettingsSharedVM
    lateinit var tempRadioGroup : RadioGroup
    lateinit var windRadioGroup : RadioGroup
    lateinit var langRadioGroup: RadioGroup

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
        tempRadioGroup = view.findViewById(R.id.rgTempUnits)
        windRadioGroup = view.findViewById(R.id.rgWindUnits)
        langRadioGroup = view.findViewById(R.id.rgLang)

        tempRadioGroup.setOnCheckedChangeListener{group, checked ->
            val selectedTempRadioButton = view.findViewById<RadioButton>(checked)
            val selectedTempUnit = selectedTempRadioButton.text
            if(selectedTempUnit.equals("Fahrenheit")){
                sharedVM.setTempUnit("f")
                sharedVM.setUnit("imperial")
            }else{
                if(selectedTempUnit.equals("Celsius")){
                    sharedVM.setTempUnit("c")
                    sharedVM.setUnit("metric")
                }else{
                    sharedVM.setTempUnit("k")
                    sharedVM.setUnit("")
                }
            }
        }

        windRadioGroup.setOnCheckedChangeListener{group, checked ->
            val selectedWindRadioButton = view.findViewById<RadioButton>(checked)
            val selectedWindUnit = selectedWindRadioButton.text
            if(selectedWindUnit.equals("KM")){
                sharedVM.setWindUnit("km")
            }else{
                sharedVM.setWindUnit("mph")
            }
        }

        langRadioGroup.setOnCheckedChangeListener{group, checked ->
            val selectedLangRadioButton = view.findViewById<RadioButton>(checked)
            val selectLang = selectedLangRadioButton.text
            if (selectLang.equals("Arabic")){
                sharedVM.setLang("ar")
            }else{
                sharedVM.setLang("en")
            }
        }
    }
}