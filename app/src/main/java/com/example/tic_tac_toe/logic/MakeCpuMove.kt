package com.example.tic_tac_toe.logic

import kotlinx.coroutines.delay

suspend fun makeCpuMove(board: List<MutableList<String>>) {
    // 1,1 second delay
    delay(1100L)

    val winMove = findWinningMove(board, "O")
    if (winMove != null) {
        board[winMove.first][winMove.second] = "O"
        return
    }

    // Bloquear al jugador si está a punto de ganar
    val blockMove = findWinningMove(board, "X")
    if (blockMove != null) {
        board[blockMove.first][blockMove.second] = "O"
        return
    }

    // Si no puede ganar ni bloquear, elige una celda aleatoria
    val emptyCells = mutableListOf<Pair<Int, Int>>()
    for (row in 0..2) {
        for (col in 0..2) {
            if (board[row][col].isEmpty()) {
                emptyCells.add(Pair(row, col))
            }
        }
    }
    if (emptyCells.isNotEmpty()) {
        val randomCell = emptyCells.random()
        board[randomCell.first][randomCell.second] = "O"
    }
}

// Función auxiliar para encontrar un movimiento ganador
fun findWinningMove(board: List<MutableList<String>>, player: String): Pair<Int, Int>? {
    for (row in 0..2) {
        for (col in 0..2) {
            if (board[row][col].isEmpty()) {
                // Simular el movimiento
                board[row][col] = player
                if (checkWinner(board) == player) {
                    // Si el jugador puede ganar, devuelve la posición
                    board[row][col] = ""
                    return Pair(row, col)
                }
                // Si no es ganador, deshacer el movimiento
                board[row][col] = ""
            }
        }
    }
    return null
}