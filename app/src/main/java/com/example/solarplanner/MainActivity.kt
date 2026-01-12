package com.example.solarplanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.solarplanner.infrastructure.SolarCalculationViewmodel
import com.example.solarplanner.ui.theme.SolarPlannerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SolarPlannerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    SolarCalculationPage(innerPadding)
                }
            }
        }
    }
}

@Composable
fun SolarCalculationPage(
    paddingValues: PaddingValues,
    viewmodel: SolarCalculationViewmodel = viewModel()
) {


    val energyConsumptionUnit = viewmodel.energyInput.collectAsStateWithLifecycle()

    val solarPanelRequired = viewmodel.panelRequired.collectAsStateWithLifecycle()

    val spaceRequired = viewmodel.areaRequired.collectAsStateWithLifecycle()

    val showData = viewmodel.showData.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {

        val keyboardController = LocalSoftwareKeyboardController.current

        TextField(
            value = energyConsumptionUnit.value,
            onValueChange = { input ->
                viewmodel.onInputChange(input)
            },
            label = {
                Text(
                    text = "Energy Consumption Units"
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            )
        )

        Button(
            onClick = {
                viewmodel.calculatePanelRequired()
                viewmodel.calculateAreaRequired()
            }
        ) {
            Text(
                text = "Calculate"
            )
        }

        if(showData.value){

            Text(
                text = "Panels Required: ${solarPanelRequired.value}"
            )

            Text(
                text = "Space Required: ${spaceRequired.value}"
            )
        }

    }
}

