package com.example.solarplanner.infrastructure

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.ceil

class SolarCalculationViewmodel: ViewModel() {

    private var _panelRequired = MutableStateFlow(1);
    val panelRequired = _panelRequired.asStateFlow()

    private var _energyInput = MutableStateFlow("")

    val energyInput: StateFlow<String> = _energyInput.asStateFlow()

    private var _areaRequired = MutableStateFlow(1)
    val areaRequired: StateFlow<Int> = _areaRequired.asStateFlow()

    private var _showData = MutableStateFlow(false)
    val showData: StateFlow<Boolean> = _showData.asStateFlow()


    fun stringToInt(input:String):Int{
        if(input.isNotBlank()){
            if(input.all { it.isDigit() }){

               return input.toInt()
            }
        }
        return  0
    }

    fun calculatePanelRequired() {

        _showData.value = true

        //energy required by house in a month
        val energyRequired = stringToInt(_energyInput.value).toDouble()

        //single panel energy generation in a month
        val singlePanelGeneration = 4*30;

        //total panels required
        //suppose x panels are required
        // single panel generates 4 kw and house need 10Kw
        //so  " 4x = 10
        //x = 10/4 or energy required/single panel energy

        var totalPanelRequired = 1;

        if(energyRequired>120){
            totalPanelRequired = ceil(energyRequired / singlePanelGeneration).toInt();
        }

        _panelRequired.value = totalPanelRequired
    }

    fun onInputChange(input:String){

        if(input.isBlank()){
            _showData.value = false
        }

        _energyInput.value = input
    }

    fun calculateAreaRequired(){

        //single panel required 80 to 100 square feet
        val singlePanelArea = 100;

        val totalAreaRequired = singlePanelArea * _panelRequired.value;

        _areaRequired.value = totalAreaRequired
    }


}