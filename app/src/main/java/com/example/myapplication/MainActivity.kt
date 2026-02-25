package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardOptions
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    RgbColorCreator(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun RgbColorCreator(modifier: Modifier = Modifier) {
    val redChannel = remember { mutableStateOf("") }
    val greenChannel = remember { mutableStateOf("") }
    val blueChannel = remember { mutableStateOf("") }
    val displayColor = remember { mutableStateOf(Color.White) }
    var colorHexCode by remember { mutableStateOf("#FFFFFF") }
    var showError by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Criador de Cores RGB",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Digite dois caracteres hexadecimais (0-9, A-F ou a-f) para cada canal.",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))
        HexChannelInput(
            label = "Canal Vermelho",
            value = redChannel.value,
            onValueChange = { redChannel.value = it.uppercase() }
        )
        Spacer(modifier = Modifier.height(8.dp))
        HexChannelInput(
            label = "Canal Verde",
            value = greenChannel.value,
            onValueChange = { greenChannel.value = it.uppercase() }
        )
        Spacer(modifier = Modifier.height(8.dp))
        HexChannelInput(
            label = "Canal Azul",
            value = blueChannel.value,
            onValueChange = { blueChannel.value = it.uppercase() }
        )

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                showError = false
                if (isValidHexInput(redChannel.value) &&
                    isValidHexInput(greenChannel.value) &&
                    isValidHexInput(blueChannel.value)
                ) {
                    colorHexCode = "#${redChannel.value}${greenChannel.value}${blueChannel.value}"
                    displayColor.value = Color(colorHexCode.toColorInt())
                } else {
                    showError = true
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Criar Cor RGB")
        }

        if (showError) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Preencha todos os canais com 2 caracteres hexadecimais válidos.",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Cor criada: $colorHexCode",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(displayColor.value, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = colorHexCode,
                color = if (displayColor.value == Color.Black) Color.White else Color.Black,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun HexChannelInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = { input ->
            if (isValidPartialHexInput(input)) {
                onValueChange(input)
            }
        },
        label = { Text(label) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters),
        modifier = Modifier.fillMaxWidth()
    )
}

private fun isValidPartialHexInput(input: String): Boolean {
    return input.length <= 2 && input.all {
        it in '0'..'9' || it in 'A'..'F' || it in 'a'..'f'
    }
}

fun isValidHexInput(input: String): Boolean {
    return input.filter {
        it in '0'..'9' ||
            it in 'A'..'F' ||
            it in 'a'..'f'
    }.length == 2
}

@Preview(showBackground = true)
@Composable
fun RgbColorCreatorPreview() {
    MyApplicationTheme {
        RgbColorCreator(modifier = Modifier.padding(16.dp))
    }
}
