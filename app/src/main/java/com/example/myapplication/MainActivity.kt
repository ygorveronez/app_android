package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    RgbColorMapScreen(
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
fun RgbColorMapScreen(modifier: Modifier = Modifier) {
    var selectedColor by remember { mutableStateOf(Color.White) }
    var mapSize by remember { mutableStateOf(IntSize.Zero) }
    var selectedPoint by remember { mutableStateOf(Offset.Zero) }

    fun updateColor(position: Offset) {
        if (mapSize.width == 0 || mapSize.height == 0) return

        val clampedX = position.x.coerceIn(0f, mapSize.width.toFloat())
        val clampedY = position.y.coerceIn(0f, mapSize.height.toFloat())

        val hue = (clampedX / mapSize.width) * 360f
        val value = 1f - (clampedY / mapSize.height)

        selectedPoint = Offset(clampedX, clampedY)
        selectedColor = Color.hsv(hue = hue, saturation = 1f, value = value)
    }

    val hexCode = selectedColor.toHexCode()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Mapa de Cores RGB",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Arraste o dedo no mapa para mudar a cor em tempo real.",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .background(Color(0xFF1F1F1F), RoundedCornerShape(12.dp))
                .padding(8.dp)
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .onSizeChanged {
                        mapSize = it
                        if (selectedPoint == Offset.Zero) {
                            selectedPoint = Offset(it.width / 2f, it.height / 2f)
                            updateColor(selectedPoint)
                        }
                    }
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = { tapPosition ->
                            updateColor(tapPosition)
                        })
                    }
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = { startPosition ->
                                updateColor(startPosition)
                            },
                            onDrag = { change, _ ->
                                updateColor(change.position)
                            }
                        )
                    }
            ) {
                drawRect(
                    brush = Brush.horizontalGradient(
                        listOf(
                            Color.Red,
                            Color.Yellow,
                            Color.Green,
                            Color.Cyan,
                            Color.Blue,
                            Color.Magenta,
                            Color.Red
                        )
                    )
                )
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black)
                    )
                )

                if (selectedPoint != Offset.Zero) {
                    drawCircle(
                        color = Color.White,
                        radius = 14f,
                        center = selectedPoint
                    )
                    drawCircle(
                        color = Color.Black,
                        radius = 10f,
                        center = selectedPoint
                    )
                    drawCircle(
                        color = selectedColor,
                        radius = 7f,
                        center = selectedPoint
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Cor selecionada: $hexCode",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(selectedColor, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = hexCode,
                color = if (selectedColor.luminance() < 0.4f) Color.White else Color.Black,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

private fun Color.toHexCode(): String {
    val red = (this.red * 255).roundToInt().coerceIn(0, 255)
    val green = (this.green * 255).roundToInt().coerceIn(0, 255)
    val blue = (this.blue * 255).roundToInt().coerceIn(0, 255)
    return String.format("#%02X%02X%02X", red, green, blue)
}

@Preview(showBackground = true)
@Composable
fun RgbColorMapScreenPreview() {
    MyApplicationTheme {
        RgbColorMapScreen(modifier = Modifier.padding(16.dp))
    }
}
