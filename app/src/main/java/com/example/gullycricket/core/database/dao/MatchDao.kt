package com.example.gullycricket.core.database.dao

import androidx.room.*
import com.example.gullycricket.core.database.entity.BallEventEntity
import com.example.gullycricket.core.database.entity.MatchEntity
import com.example.gullycricket.core.database.entity.PlayerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MatchDao {
    @Insert
    suspend fun insertMatch(match: MatchEntity): Long

    @Update
    suspend fun updateMatch(match: MatchEntity)

    @Insert
    suspend fun insertPlayer(player: PlayerEntity)

    @Insert
    suspend fun insertBallEvent(ballEvent: BallEventEntity)

    @Query("SELECT * FROM matches WHERE matchId = :matchId")
    fun getMatch(matchId: Long): Flow<MatchEntity>

    @Query("SELECT * FROM players WHERE matchId = :matchId")
    fun getPlayersForMatch(matchId: Long): Flow<List<PlayerEntity>>

    @Query("SELECT * FROM ball_events WHERE matchId = :matchId ORDER BY timestamp ASC")
    fun getBallEventsForMatch(matchId: Long): Flow<List<BallEventEntity>>

    @Query("SELECT * FROM matches ORDER BY matchId DESC")
    fun getAllMatches(): Flow<List<MatchEntity>>

    @Query("DELETE FROM ball_events WHERE eventId = (SELECT MAX(eventId) FROM ball_events WHERE matchId = :matchId) AND overNumber = :currentOver")
    suspend fun deleteLastBallEventIfInCurrentOver(matchId: Long, currentOver: Int)

    @Delete
    suspend fun deleteMatch(match: MatchEntity)
}
