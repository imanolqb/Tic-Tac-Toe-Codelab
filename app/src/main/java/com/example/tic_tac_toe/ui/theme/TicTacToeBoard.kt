package com.example.tic_tac_toe.ui.theme

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Composable as Composable1

@Composable1
fun ticTacToeBoard(modifier: Modifier = Modifier,
                   onDrawingComplete: () -> Unit): Boolean {

    val lineLength1 = remember { Animatable(0f) }
    val lineLength2 = remember { Animatable(0f) }
    val lineLength3 = remember { Animatable(0f) }
    val lineLength4 = remember { Animatable(0f) }

    var isDrawing by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        lineLength1.animateTo(1f, animationSpec = tween(500))
        lineLength2.animateTo(1f, animationSpec = tween(500))
        lineLength3.animateTo(1f, animationSpec = tween(500))
        lineLength4.animateTo(1f, animationSpec = tween(500))

        isDrawing = false
        onDrawingComplete()
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        val lineWidth = 10f
        val thirdWidth = width / 3
        val thirdHeight = height / 3

        drawLine(
            color = Color.White,
            start = Offset(thirdWidth, 0f),
            end = Offset(thirdWidth, height * lineLength1.value),
            strokeWidth = lineWidth
        )
        drawLine(
            color = Color.White,
            start = Offset(2 * thirdWidth, 0f),
            end = Offset(2 * thirdWidth, height * lineLength2.value),
            strokeWidth = lineWidth
        )
        drawLine(
            color = Color.White,
            start = Offset(0f, thirdHeight),
            end = Offset(width * lineLength3.value, thirdHeight),
            strokeWidth = lineWidth
        )
        drawLine(
            color = Color.White,
            start = Offset(0f, 2 * thirdHeight),
            end = Offset(width * lineLength4.value, 2 * thirdHeight),
            strokeWidth = lineWidth
        )
    }
    return isDrawing
}
