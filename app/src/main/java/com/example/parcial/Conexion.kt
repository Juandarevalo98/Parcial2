package com.example.parcial

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class Conexion {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference

    fun updatePlayerPosition(gameId: String, playerId: String, position: Int) {
        val playerPath = "games/$gameId/$playerId/position"
        database.child(playerPath).setValue(position)
    }

    fun incrementSixCount(gameId: String, playerId: String, currentSixCount: Int) {
        val sixCountPath = "games/$gameId/$playerId/sixCount"
        database.child(sixCountPath).setValue(currentSixCount + 1)
    }

    fun updateCurrentTurn(gameId: String, currentTurn: Int) {
        val turnPath = "games/$gameId/currentTurn"
        database.child(turnPath).setValue(currentTurn)
    }

    fun listenToPlayerPositions(gameId: String, onPositionChanged: (player1Position: Int, player2Position: Int) -> Unit) {
        val player1Ref = database.child("games/$gameId/player1/position")
        val player2Ref = database.child("games/$gameId/player2/position")

        player1Ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val player1Position = snapshot.getValue(Int::class.java) ?: 0
                player2Ref.get().addOnSuccessListener { player2Snapshot ->
                    val player2Position = player2Snapshot.getValue(Int::class.java) ?: 0
                    onPositionChanged(player1Position, player2Position)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        player2Ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val player2Position = snapshot.getValue(Int::class.java) ?: 0
                player1Ref.get().addOnSuccessListener { player1Snapshot ->
                    val player1Position = player1Snapshot.getValue(Int::class.java) ?: 0
                    onPositionChanged(player1Position, player2Position)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}