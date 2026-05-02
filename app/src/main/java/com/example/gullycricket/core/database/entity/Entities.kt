package com.example.gullycricket.core.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "matches")
data class MatchEntity(
    @PrimaryKey(autoGenerate = true) val matchId: Long = 0,
    val team1Name: String,
    val team2Name: String,
    val totalOvers: Int,
    val team1Players: Int,
    val team2Players: Int,
    val lastManStandingAllowed: Boolean,
    val currentInnings: Int = 1,
    val targetScore: Int? = null,
    val isComplete: Boolean = false
)

@Entity(
    tableName = "ball_events",
    foreignKeys = [
        ForeignKey(
            entity = MatchEntity::class,
            parentColumns = ["matchId"],
            childColumns = ["matchId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("matchId")]
)
data class BallEventEntity(
    @PrimaryKey(autoGenerate = true) val eventId: Long = 0,
    val matchId: Long,
    val overNumber: Int,      
    val ballNumberInOver: Int, 
    val strikerId: Long,
    val nonStrikerId: Long,
    val bowlerId: Long,
    val runsScored: Int,       
    val extraType: String,  // NONE, WIDE, NO_BALL
    val extraRuns: Int,        
    val wicketType: String, // NONE, BOWLED, CAUGHT, RUN_OUT, etc.
    val playerOutId: Long?,    
    val innings: Int = 1,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "players",
    foreignKeys = [
        ForeignKey(
            entity = MatchEntity::class,
            parentColumns = ["matchId"],
            childColumns = ["matchId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("matchId")]
)
data class PlayerEntity(
    @PrimaryKey(autoGenerate = true) val playerId: Long = 0,
    val matchId: Long,
    val teamNumber: Int, // 1 or 2
    val playerName: String
)