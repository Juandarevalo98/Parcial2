package com.example.parcial

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.widget.GridLayout
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var tvDice1: TextView
    private lateinit var tvDice2: TextView
    private lateinit var btnPlayer1: Button
    private lateinit var btnPlayer2: Button

    private lateinit var boardGrid: GridLayout
    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board)

        tvDice1 = findViewById(R.id.tvDice1)
        tvDice2 = findViewById(R.id.tvDice2)
        btnPlayer1 = findViewById(R.id.btnPlayer1)
        btnPlayer2 = findViewById(R.id.btnPlayer2)

        boardGrid = findViewById(R.id.boardGrid)

        val gameId = "gameId123"
        val conexion = Conexion()
        conexion.listenToPlayerPositions(gameId) { player1Position, player2Position ->
            // Mostrar las posiciones de los jugadores con un Toast
            Toast.makeText(this, "Player 1 está en la posición: $player1Position", Toast.LENGTH_SHORT).show()
            Toast.makeText(this, "Player 2 está en la posición: $player2Position", Toast.LENGTH_SHORT).show()

            // Actualizar el tablero con las nuevas posiciones
            viewModel.updateBoard(player1Position, player2Position)
        }

        viewModel.initializeGame()

        viewModel.boardData.observe(this) { board ->
            viewModel.drawBoard(boardGrid, board)
        }

        viewModel.generateBoard(8, 8)

        viewModel.dice1Result.observe(this) { result ->
            tvDice1.text = result.toString() // Actualiza el dado 1
        }

        viewModel.dice2Result.observe(this) { result ->
            tvDice2.text = result.toString() // Actualiza el dado 2
        }

        viewModel.isPlayer1Turn.observe(this) { isPlayer1Turn ->
            btnPlayer1.isEnabled = isPlayer1Turn
            btnPlayer2.isEnabled = !isPlayer1Turn
        }

        viewModel.isPlayer2Turn.observe(this) { isPlayer2Turn ->
            btnPlayer1.isEnabled = !isPlayer2Turn
            btnPlayer2.isEnabled = isPlayer2Turn
        }

        btnPlayer1.setOnClickListener {
            if (viewModel.isPlayer1Turn.value == true) {
                viewModel.animateDice(tvDice1)
                viewModel.animateDice(tvDice2)

                viewModel.rollDice()

                viewModel.updateTurn()
            }
        }

        btnPlayer2.setOnClickListener {
            if (viewModel.isPlayer2Turn.value == true) {
                viewModel.animateDice(tvDice1)
                viewModel.animateDice(tvDice2)

                viewModel.rollDice()

                viewModel.updateTurn()
            }
        }
    }
}