package com.example.wezcasting.View

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeSettingsSharedVM : ViewModel() {
    private val _lang = MutableLiveData<String>("en")
    val lang : LiveData<String> = _lang

    private val _tempUnit = MutableLiveData<String>("c")
    val tempUnit : LiveData<String> = _tempUnit

    private val _windUnit = MutableLiveData<String>("km")
    val windUnit : LiveData<String> = _windUnit

    private val _pressureUnit = MutableLiveData<String>("mbar")
    val pressureUnit : LiveData<String> = _pressureUnit

    private val _unit = MutableLiveData<String>("metric")
    val unit : LiveData<String> = _unit

    fun setLang(langSelected : String){
        _lang.value = langSelected
    }

    fun setTempUnit(unitSelected : String){
        _tempUnit.value = unitSelected
    }

    fun setWindUnit(unitSelected: String){
        _windUnit.value = unitSelected
    }

    fun setPressureUnit(unitSelected: String){
        _pressureUnit.value = unitSelected
    }

    fun setUnit(unitSelected: String){
        _unit.value = unitSelected
    }

}