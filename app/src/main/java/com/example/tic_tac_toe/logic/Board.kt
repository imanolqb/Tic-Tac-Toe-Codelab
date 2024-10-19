package com.example.tic_tac_toe.logic

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
}
