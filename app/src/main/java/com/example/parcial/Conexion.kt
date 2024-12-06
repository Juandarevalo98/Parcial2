package com.example.parcial

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Conexion {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference

    // Actualizar la posición del jugador
    fun updatePlayerPosition(gameId: String, playerId: String, position: Int) {
        val playerPath = "games/$gameId/$playerId/position"
        database.child(playerPath).setValue(position)
    }

    // Incrementar el contador de seis
    fun incrementSixCount(gameId: String, playerId: String, currentSixCount: Int) {
        val sixCountPath = "games/$gameId/$playerId/sixCount"
        database.child(sixCountPath).setValue(currentSixCount + 1)
    }

    // Actualizar el turno actual
    fun updateCurrentTurn(gameId: String, currentTurn: Int) {
        val turnPath = "games/$gameId/currentTurn"
        database.child(turnPath).setValue(currentTurn)
    }
}
