package com.example.tic_tac_toe.logic

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.RecomposeScope
import androidx.compose.runtime.mutableStateOf

/**
 * The Board class represents a Tic-Tac-Toe board consisting of a 3x3 grid of cells.
 * It provides methods for interacting with the board, such as retrieving and setting cell values,
 * checking for a full board, resetting the board, and determining a winner.
 *
 * [getCell] provides the state of a specific cell.
 * [setCell] sets the value of a specific cell if the cell is empty.
 * [isFull] checks if the board is fully occupied.
 * [reset] clears all cells, resetting the board to an empty state.
 * [checkWinner] checks if there is a winner by evaluating rows, columns, and diagonals.
 * [copy] creates a duplicate of the current board with the same state.
 */

class Board {
    private val board: MutableList<MutableList<String>> = MutableList(3) { MutableList(3) { "" } }

    fun getCell(row: Int, col: Int): String {
        return board[row][col]
    }

    fun setCell(row: Int, col: Int, player: String): Boolean {
        if (board[row][col].isEmpty()) {
            board[row][col] = player
            return true
        }
        return false
    }

    fun isFull(): Boolean {
        return board.all { row -> row.all { it.isNotEmpty() } }
    }

    fun reset() {
        for (i in 0..2) {
            for (j in 0..2) {
                board[i][j] = ""
            }
        }
    }

    fun checkWinner(): String? {
        // Verify rows
        for (row in board) {
            if (row[0].isNotEmpty() && row[0] == row[1] && row[1] == row[2]) {
                return row[0]
            }
        }

        // Verify cols
        for (col in 0..2) {
            if (board[0][col].isNotEmpty() && board[0][col] == board[1][col] && board[1][col] == board[2][col]) {
                return board[0][col]
            }
        }

        // Verify diagonal
        if (board[0][0].isNotEmpty() && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            return board[0][0]
        }
        if (board[0][2].isNotEmpty() && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            return board[0][2]
        }

        // No winner
        return null
    }

    fun copy(): Board {
        val newBoard = Board()
        for (row in 0..2) {
            for (col in 0..2) {
                newBoard.setCell(row, col, this.getCell(row, col))
            }
        }
        return newBoard
    }
}
