package com.example.tic_tac_toe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tic_tac_toe.logic.Board
import com.example.tic_tac_toe.logic.makeCpuMove
import com.example.tic_tac_toe.ui.theme.GameResultDialog
import com.example.tic_tac_toe.ui.theme.TictactoeTheme
import com.example.tic_tac_toe.ui.theme.ticTacToeBoardDrawing
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TictactoeTheme {
                MainContent()
            }
        }
    }
}

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    val alpha = remember { Animatable(1f) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.initial_background),
            contentDescription = "Initial Screen",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }

    LaunchedEffect(Unit) {
        delay(2000L)
        alpha.animateTo(0f, animationSpec = tween(durationMillis = 500))
        onTimeout()
    }
}

@Composable
fun MainContent() {
    var isSplashScreenVisible by remember { mutableStateOf(true) }

    if (isSplashScreenVisible) {
        SplashScreen {
            isSplashScreenVisible = false
        }
    } else {
        GameScreen()
    }
}

@Composable
fun GameScreen() {
    var gameMode by remember { mutableStateOf<GameMode?>(null) }
    val board by remember { mutableStateOf(Board()) }
    var currentPlayer by remember { mutableStateOf("X") }
    var isDrawing by remember { mutableStateOf(true) }
    var winner by remember { mutableStateOf<String?>(null) }
    var isGameOver by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var cpuDelay by remember { mutableStateOf(false) }

    // Background animatable during games, change color by players
    val backgroundColor = remember { Animatable(Color.Transparent) }
    LaunchedEffect(currentPlayer) {
        val targetColor = if (currentPlayer == "X") Color(0xFFde8f70) else Color(0xFF3f7cab)
        backgroundColor.animateTo(targetColor, animationSpec = tween(durationMillis = 500))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor.value),
        contentAlignment = Alignment.Center
    ) {

        if (gameMode == null) {
            Box(
                modifier = Modifier.fillMaxSize().background(Color.White)
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .drawBehind {
                        val width = size.width
                        val height = size.height

                        val path = androidx.compose.ui.graphics.Path().apply {
                            moveTo(0f, 0f)
                            lineTo(width, 0f)
                            lineTo(0f, height)
                            close()
                        }

                        drawPath(path, Color(0xFFde8f70))

                        val pathBlue = androidx.compose.ui.graphics.Path().apply {
                            moveTo(width, 0f)
                            lineTo(width, height)
                            lineTo(0f, height)
                            close()
                        }

                        drawPath(pathBlue, Color(0xFF3f7cab))
                    }
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.tic_tac_toe_home),
                    contentDescription = "Tic Tac Toe Home icon",
                    modifier = Modifier.size(150.dp)
                )

                Text(
                    "Choose Game Mode:",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { gameMode = GameMode.PLAYER_VS_PLAYER },
                    modifier = Modifier
                        .size(width = 250.dp, height = 60.dp)
                        .border(BorderStroke(4.dp, Color.Black), shape = MaterialTheme.shapes.extraLarge),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF02ADF5),
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        "Player 1 vs Player 2",
                        fontSize = 18.sp
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { gameMode = GameMode.PLAYER_VS_CPU },
                    modifier = Modifier
                        .size(width = 250.dp, height = 60.dp)
                        .border(BorderStroke(4.dp, Color.Black), shape = MaterialTheme.shapes.extraLarge),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF5E59),
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        "Player vs CPU",
                        fontSize = 18.sp
                    )
                }
            }
        } else {
            // GAME MODES
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor.value)
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // LOGO ON TOP
                Image(
                    painter = painterResource(id = R.drawable.tic_tac_toe_home),
                    contentDescription = "Tic Tac Toe Logo",
                    modifier = Modifier.size(100.dp)
                        .clickable {
                            gameMode = null
                            board.reset()
                            currentPlayer = "X"
                            winner = null
                            isGameOver = false
                            showDialog = false
                        }
                )
                Spacer(modifier = Modifier.height(32.dp))
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(300.dp)
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    ticTacToeBoardDrawing(modifier = Modifier.fillMaxSize(),
                        onDrawingComplete = {isDrawing = false})

                    Column {
                        for (row in 0..2) {
                            Row {
                                for (col in 0..2) {
                                    Box(
                                        modifier = Modifier
                                            .size(90.dp)
                                            .clickable(
                                                enabled = board.getCell(row, col).isEmpty() && winner == null && !cpuDelay
                                            )
                                            {
                                                if (board.getCell(row, col).isEmpty() && !isDrawing) {
                                                    board.setCell(row, col, currentPlayer)
                                                    winner = board.checkWinner()

                                                    // Draw verify
                                                    if (board.isFull()) {
                                                        board.reset()
                                                        currentPlayer =
                                                            if (currentPlayer == "X") "O" else "X" // player relay
                                                        winner = null // winner relay
                                                        isGameOver = false // end game state relay
                                                    } else if (winner != null) {
                                                        showDialog = true
                                                        isGameOver = true
                                                    } else {
                                                        currentPlayer = if (currentPlayer == "X") "O" else "X"
                                                        if (gameMode == GameMode.PLAYER_VS_CPU && currentPlayer == "O") {
                                                            cpuDelay = true
                                                        }
                                                    }
                                                }


                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        when (board.getCell(row, col)) {
                                            "X" -> Image(
                                                painter = painterResource(id = R.drawable.new_cross),
                                                contentDescription = "X",
                                                modifier = Modifier.size(80.dp)
                                                    .graphicsLayer(rotationZ = 45f)
                                            )
                                            "O" -> Image(
                                                painter = painterResource(id = R.drawable.new_circle),
                                                contentDescription = "O",
                                                modifier = Modifier.size(80.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (showDialog) {
                    GameResultDialog(
                        winner = winner,
                        onRestart = {
                            board.reset()
                            currentPlayer = "X"
                            winner = null
                            isGameOver = false
                            showDialog = false
                        },
                        onReturnToMenu = {
                            gameMode = null
                            board.reset()
                            currentPlayer = "X"
                            winner = null
                            isGameOver = false
                            showDialog = false
                        }
                    )
                }

                if (cpuDelay && currentPlayer == "O") {
                    LaunchedEffect(cpuDelay) {
                        makeCpuMove(board)
                        winner = board.checkWinner()
                        if (winner == null && !board.isFull()) {
                            currentPlayer = "X"
                        } else {
                            showDialog = true
                            isGameOver = true
                        }
                        cpuDelay = false
                    }
                }
            }
        }
    }
}

enum class GameMode {
    PLAYER_VS_PLAYER, PLAYER_VS_CPU
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TictactoeTheme {
        GameScreen()
    }
}