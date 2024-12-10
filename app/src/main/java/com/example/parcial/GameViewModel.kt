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

    fun initializeGame() {
        conexion.updatePlayerPosition(gameId, "player1", 0)
        conexion.updatePlayerPosition(gameId, "player2", 0)

        // Inicializar los contadores de seis a 0
        conexion.incrementSixCount(gameId, "player1", 0)
        conexion.incrementSixCount(gameId, "player2", 0)

        conexion.updateCurrentTurn(gameId, 1)
    }

    fun rollDice() {
        val dice1 = (1..6).random()
        val dice2 = (1..6).random()
        dice1Result.value = dice1
        dice2Result.value = dice2

        val total = dice1 + dice2

        if (isPlayer1Turn.value == true) {
            player1Position += total
            // Verifica si el jugador cae en una escalera o serpiente
            player1Position = checkForLadderOrSnake(player1Position)
            conexion.updatePlayerPosition(gameId, "player1", player1Position)
            if (dice1 == 6 || dice2 == 6) {
                player1SixCount++
                conexion.incrementSixCount(gameId, "player1", player1SixCount)
            }
        } else if (isPlayer2Turn.value == true) {
            player2Position += total
            // Verifica si el jugador cae en una escalera o serpiente
            player2Position = checkForLadderOrSnake(player2Position)
            conexion.updatePlayerPosition(gameId, "player2", player2Position)
            if (dice1 == 6 || dice2 == 6) {
                player2SixCount++
                conexion.incrementSixCount(gameId, "player2", player2SixCount)
            }
        }
    }

    fun checkForLadderOrSnake(position: Int): Int {
        // Verificamos las escaleras y serpientes usando la instancia de LogicGame
        logicGame.getEscaleras().forEach { (rango, _) ->
            if (position == rango.first) return rango.second // Subir por la escalera
        }

        logicGame.getSerpientes().forEach { (rango, _) ->
            if (position == rango.first) return rango.second // Bajar por la serpiente
        }

        return position // Si no es escalera ni serpiente, se mantiene la posición original
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

    fun updateBoard(player1Pos: Int, player2Pos: Int) {
        player1Position = player1Pos
        player2Position = player2Pos
    }
}