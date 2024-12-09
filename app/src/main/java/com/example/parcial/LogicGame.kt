package com.example.parcial

import android.content.Context
import android.graphics.Color
import android.widget.GridLayout
import android.widget.TextView
import androidx.lifecycle.MutableLiveData

class LogicGame(private val context: Context) {

    val boardData = MutableLiveData<List<Int>>()

    private val escaleras = mapOf(
        Pair(8, 39) to Color.parseColor("#556e42"),
        Pair(12, 38) to Color.parseColor("#81bf51"),
        Pair(32, 47) to Color.parseColor("#6bee04"),
        Pair(44, 61) to Color.parseColor("#539321")
    )

    private val serpientes = mapOf(
        Pair(31, 3) to Color.parseColor("#952c2c"),
        Pair(41, 22) to Color.parseColor("#e68e8e"),
        Pair(51, 5) to Color.parseColor("#fa0606"),
        Pair(60, 43) to Color.parseColor("#c44e4e")
    )

    // Métodos para acceder a las escaleras y serpientes
    fun getEscaleras(): Map<Pair<Int, Int>, Int> {
        return escaleras
    }

    fun getSerpientes(): Map<Pair<Int, Int>, Int> {
        return serpientes
    }

    fun generateBoard(rows: Int, columns: Int) {
        val board = mutableListOf<Int>()
        var cellNumber = 1

        for (row in 0 until rows) {
            val isReverseRow = row % 2 != 0

            if (isReverseRow) {
                for (col in columns - 1 downTo 0) {
                    board.add(cellNumber++)
                }
            } else {
                for (col in 0 until columns) {
                    board.add(cellNumber++)
                }
            }
        }
        boardData.value = board
    }

    fun drawBoard(boardGrid: GridLayout, board: List<Int>, viewModel: GameViewModel) {
        boardGrid.removeAllViews()
        boardGrid.columnCount = 8

        val rows = 8
        val columns = 8

        for (row in 0 until rows) {
            val isReverseRow = row % 2 != 0

            for (col in 0 until columns) {
                val actualColumn = if (isReverseRow) columns - 1 - col else col
                val cellNumber = board[row * columns + actualColumn]

                val cell = TextView(context).apply {
                    text = cellNumber.toString()
                    textSize = 18f
                    gravity = android.view.Gravity.CENTER
                    setBackgroundResource(android.R.drawable.dialog_holo_light_frame)
                    setBackgroundColor(getCellColor(cellNumber))
                    val params = GridLayout.LayoutParams().apply {
                        width = 0
                        height = 0
                        columnSpec = GridLayout.spec(actualColumn, 1f)
                        rowSpec = GridLayout.spec(rows - 1 - row, 1f) // Invierte las filas para que empiecen desde abajo
                    }
                    layoutParams = params
                }
                boardGrid.addView(cell)
            }
        }
    }

    fun getCellColor(cellNumber: Int): Int {
        // Si es una escalera, el jugador sube
        escaleras.forEach { (rango, color) ->
            if (cellNumber == rango.first) {
                return color
            } else if (cellNumber == rango.second) {
                return color
            }
        }

        serpientes.forEach { (rango, color) ->
            if (cellNumber == rango.first) {
                return color
            } else if (cellNumber == rango.second) {
                return color
            }
        }

        return Color.WHITE
    }
}
