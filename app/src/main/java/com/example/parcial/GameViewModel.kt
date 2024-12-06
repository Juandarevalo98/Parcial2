package com.example.parcial

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import android.widget.GridLayout
import android.widget.TextView

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val logicGame = LogicGame(application.applicationContext) // Se pasa el contexto de la aplicación
    private val conexion = Conexion()

    val boardData = logicGame.boardData
    val dice1Result = MutableLiveData<Int>()
    val dice2Result = MutableLiveData<Int>()
    val isPlayer1Turn = MutableLiveData<Boolean>(true)
    val isPlayer2Turn = MutableLiveData<Boolean>(false)

    private val gameId = "gameId123"
    private var player1Position = 0
    private var player2Position = 0
    private var player1SixCount = 0
    private var player2SixCount = 0

    fun generateBoard(rows: Int, columns: Int) {
        logicGame.generateBoard(rows, columns)
    }

    fun drawBoard(boardGrid: GridLayout, board: List<Int>) {
        logicGame.drawBoard(boardGrid, board, this) // Aquí se pasa el viewModel como parámetro
    }

    fun rollDice() {
        val dice1 = (1..6).random()
        val dice2 = (1..6).random()
        dice1Result.value = dice1
        dice2Result.value = dice2

        val total = dice1 + dice2

        if (isPlayer1Turn.value == true) {
            player1Position += total
            conexion.updatePlayerPosition(gameId, "player1", player1Position)
            if (dice1 == 6 || dice2 == 6) {
                player1SixCount++
                conexion.incrementSixCount(gameId, "player1", player1SixCount)
            }
        } else if (isPlayer2Turn.value == true) {
            player2Position += total
            conexion.updatePlayerPosition(gameId, "player2", player2Position)
            if (dice1 == 6 || dice2 == 6) {
                player2SixCount++
                conexion.incrementSixCount(gameId, "player2", player2SixCount)
            }
        }
    }

    fun animateDice(diceView: TextView) {
        diceView.animate().rotationBy(360f).setDuration(500).start()
    }

    fun updateTurn() {
        if (isPlayer1Turn.value == true) {
            isPlayer1Turn.value = false
            isPlayer2Turn.value = true
            conexion.updateCurrentTurn(gameId, 2)
        } else {
            isPlayer1Turn.value = true
            isPlayer2Turn.value = false
            conexion.updateCurrentTurn(gameId, 1)
        }
    }
}