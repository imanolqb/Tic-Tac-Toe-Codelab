package com.example.tic_tac_toe.logic

import kotlinx.coroutines.delay

suspend fun makeCpuMove(board: Board) {

    // CPU's thinking
    delay(1100L)

    val winMove = findWinningMove(board, "O")

    if (winMove != null) {
        board.setCell(winMove.first, winMove.second, "O")
        return
    }

    // same as human player
    val blockMove = findWinningMove(board, "X")
    if (blockMove != null) {
        board.setCell(blockMove.first, blockMove.second, "O")
        return
    }

    // random cell not winning case
    val emptyCells = mutableListOf<Pair<Int, Int>>()
    for (row in 0..2) {
        for (col in 0..2) {
            if (board.getCell(row, col).isEmpty()) {
                emptyCells.add(Pair(row, col))
            }
        }
    }
    if (emptyCells.isNotEmpty()) {
        val randomCell = emptyCells.random()
        board.setCell(randomCell.first, randomCell.second, "O")
    }
}

// Find a winner move
fun findWinningMove(board: Board, player: String): Pair<Int, Int>? {
    val temporaryBoard = board.copy()
    for (row in 0..2) {
        for (col in 0..2) {
            if (temporaryBoard.getCell(row, col).isEmpty()) {
                temporaryBoard.setCell(row, col, player)
                if (temporaryBoard.checkWinner() == player) {
                    return Pair(row, col)
                }
                temporaryBoard.setCell(row, col, "")
            }
        }
    }
    return null
}
