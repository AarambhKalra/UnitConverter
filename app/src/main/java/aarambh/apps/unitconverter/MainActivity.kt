package aarambh.apps.unitconverter
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import aarambh.apps.unitconverter.ui.theme.UnitConverterTheme
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UnitConverterTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    UnitConverter()
                }
            }
        }
    }
}

@Composable
fun UnitConverter() {
    var inputValue by remember { mutableStateOf("") }
    var outputValue by remember { mutableStateOf("") }
    var inputUnit by remember { mutableStateOf("Meters") }
    var outputUnit by remember { mutableStateOf("Meters") }
    var iExpanded by remember { mutableStateOf(false) }
    var oExpanded by remember { mutableStateOf(false) }
    val conversionFactor = remember { mutableStateOf(1.00) }
    val oConversionFactor = remember { mutableStateOf(1.00) }

    fun convertUnits() {
        val inputValueDouble = inputValue.toDoubleOrNull() ?: 0.0
        val result = (inputValueDouble * conversionFactor.value * 100.0 / oConversionFactor.value).roundToInt() / 100.0
        outputValue = result.toString()
    }

    // Background with a gradient
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF5B86E5), Color(0xFFEAB8E4)),
                    startY = 0f,
                    endY = 1000f
                )
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Unit Converter", style = MaterialTheme.typography.headlineLarge, color = Color.White)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = inputValue,
                onValueChange = {
                    inputValue = it
                    convertUnits()
                },
                label = { Text("Enter Value") },
                modifier = Modifier.shadow(4.dp, shape = MaterialTheme.shapes.medium)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row {
                UnitDropdownMenu(
                    expanded = iExpanded,
                    onExpandChange = { iExpanded = !iExpanded },
                    selectedUnit = inputUnit,
                    onUnitSelected = { unit, factor ->
                        inputUnit = unit
                        conversionFactor.value = factor
                        convertUnits()
                    }
                )
                Spacer(modifier = Modifier.width(16.dp))
                UnitDropdownMenu(
                    expanded = oExpanded,
                    onExpandChange = { oExpanded = !oExpanded },
                    selectedUnit = outputUnit,
                    onUnitSelected = { unit, factor ->
                        outputUnit = unit
                        oConversionFactor.value = factor
                        convertUnits()
                    }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Result: $outputValue $outputUnit",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )
        }
    }
}

@Composable
fun UnitDropdownMenu(
    expanded: Boolean,
    onExpandChange: () -> Unit,
    selectedUnit: String,
    onUnitSelected: (String, Double) -> Unit
) {
    Box {
        Button(
            onClick = onExpandChange,
            modifier = Modifier.shadow(4.dp, shape = MaterialTheme.shapes.medium)
        ) {
            Text(text = selectedUnit)
            Icon(Icons.Default.ArrowDropDown, contentDescription = "Arrow Down")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandChange() }
        ) {
            DropdownMenuItem(
                text = { Text("Centimeters") },
                onClick = {
                    onExpandChange()
                    onUnitSelected("Centimeters", 0.01)
                }
            )
            DropdownMenuItem(
                text = { Text("Meters") },
                onClick = {
                    onExpandChange()
                    onUnitSelected("Meters", 1.00)
                }
            )
            DropdownMenuItem(
                text = { Text("Feet") },
                onClick = {
                    onExpandChange()
                    onUnitSelected("Feet", 0.3048)
                }
            )
            DropdownMenuItem(
                text = { Text("Milimeters") },
                onClick = {
                    onExpandChange()
                    onUnitSelected("Milimeters", 0.001)
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UnitConverterPreview() {
    UnitConverter()
}
