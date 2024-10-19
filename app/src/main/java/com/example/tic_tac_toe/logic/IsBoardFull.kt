package com.example.tic_tac_toe.logic

fun isBoardFull(board: List<List<String>>): Boolean {
    return board.flatten().none { it.isEmpty() }
}