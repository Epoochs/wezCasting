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

    var currentTempUnit : String = ""
    var currentWindUnit : String = ""
    var currentLang : String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(savedInstanceState != null){
            currentTempUnit = savedInstanceState.getString("TempUnit").toString()
            currentLang = savedInstanceState.getString("Lang").toString()
            currentWindUnit = savedInstanceState.getString("WindUnit").toString()
        }

        sharedVM = ViewModelProvider(requireActivity()).get(HomeSettingsSharedVM::class.java)
        tempRadioGroup = view.findViewById(R.id.rgTempUnits)
        windRadioGroup = view.findViewById(R.id.rgWindUnits)
        langRadioGroup = view.findViewById(R.id.rgLang)

        if (currentTempUnit.equals("Fahrenheit")){
            tempRadioGroup.check(R.id.F)
            sharedVM.setTempUnit("f")
            sharedVM.setUnit("imperial")
        }else{
            if (currentTempUnit.equals("Celsius")){
                tempRadioGroup.check(R.id.C)
                sharedVM.setTempUnit("c")
                sharedVM.setUnit("metric")
            }else{
                tempRadioGroup.check(R.id.K)
                sharedVM.setTempUnit("k")
                sharedVM.setUnit("")
            }
        }

        if (currentLang.equals("Arabic")){
            langRadioGroup.check(R.id.ar)
            sharedVM.setLang("ar")
        }else{
            langRadioGroup.check(R.id.en)
            sharedVM.setLang("en")
        }

        tempRadioGroup.setOnCheckedChangeListener{group, checked ->
            val selectedTempRadioButton = view.findViewById<RadioButton>(checked)
            val selectedTempUnit = selectedTempRadioButton.text
            if(selectedTempUnit.equals("Fahrenheit")){
                currentTempUnit = selectedTempUnit.toString()
                sharedVM.setTempUnit("f")
                sharedVM.setUnit("imperial")
            }else{
                if(selectedTempUnit.equals("Celsius")){
                    currentTempUnit = selectedTempUnit.toString()
                    sharedVM.setTempUnit("c")
                    sharedVM.setUnit("metric")
                }else{
                    currentTempUnit = selectedTempUnit.toString()
                    sharedVM.setTempUnit("k")
                    sharedVM.setUnit("")
                }
            }
        }

        windRadioGroup.setOnCheckedChangeListener{group, checked ->
            val selectedWindRadioButton = view.findViewById<RadioButton>(checked)
            val selectedWindUnit = selectedWindRadioButton.text
            if(selectedWindUnit.equals("KM")){
                currentWindUnit = selectedWindUnit.toString()
                sharedVM.setWindUnit("km")
            }else{
                currentWindUnit = selectedWindUnit.toString()
                sharedVM.setWindUnit("mph")
            }
        }

        langRadioGroup.setOnCheckedChangeListener{group, checked ->
            val selectedLangRadioButton = view.findViewById<RadioButton>(checked)
            val selectLang = selectedLangRadioButton.text
            if (selectLang.equals("Arabic")){
                currentLang = selectLang.toString()
                sharedVM.setLang("ar")
            }else{
                currentLang = selectLang.toString()
                sharedVM.setLang("en")
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("TempUnit", currentTempUnit)
        outState.putString("WindUnit", currentWindUnit)
        outState.putString("Lang", currentLang)
    }
}