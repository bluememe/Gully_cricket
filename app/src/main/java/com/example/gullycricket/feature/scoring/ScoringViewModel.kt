package com.example.gullycricket.feature.scoring

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gullycricket.core.database.dao.MatchDao
import com.example.gullycricket.core.database.entity.BallEventEntity
import com.example.gullycricket.core.database.entity.MatchEntity
import com.example.gullycricket.core.database.entity.PlayerEntity
import com.example.gullycricket.core.network.LiveScoreDto
import com.example.gullycricket.core.network.LocalScoringServer
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

data class MatchState(
    val match: MatchEntity? = null,
    val players: List<PlayerEntity> = emptyList(),
    val events: List<BallEventEntity> = emptyList(), // All events for current innings
    val currentOver: Int = 0,
    val ballsBowledInOver: Int = 0,
    val totalScore: Int = 0,
    val totalWickets: Int = 0,
    val strikerId: Long? = null,
    val nonStrikerId: Long? = null,
    val currentBowlerId: Long? = null,
    val outPlayerIds: Set<Long> = emptySet(),
    val target: Int? = null,
    val isGameOver: Boolean = false,
    val inningsEnded: Boolean = false
)

class ScoringViewModel(private val matchDao: MatchDao) : ViewModel() {

    private val _allMatches = MutableStateFlow<List<MatchEntity>>(emptyList())
    val allMatches: StateFlow<List<MatchEntity>> = _allMatches.asStateFlow()

    private val _matchState = MutableStateFlow(MatchState())
    val matchState: StateFlow<MatchState> = _matchState.asStateFlow()

    private val _liveScore = MutableStateFlow<LiveScoreDto?>(null)
    private val localScoringServer = LocalScoringServer(_liveScore)

    private var matchObservationJob: Job? = null

    init {
        localScoringServer.startServer(8080)
        loadAllMatches()
    }

    private fun loadAllMatches() {
        viewModelScope.launch {
            matchDao.getAllMatches().collect {
                _allMatches.value = it
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        localScoringServer.stopServer()
    }

    fun startNewMatch(
        team1: String, 
        team2: String, 
        overs: Int, 
        team1P: Int, 
        team2P: Int, 
        team1Names: List<String>,
        team2Names: List<String>,
        lastManStanding: Boolean
    ) {
        viewModelScope.launch {
            val match = MatchEntity(
                team1Name = team1,
                team2Name = team2,
                totalOvers = overs,
                team1Players = team1P,
                team2Players = team2P,
                lastManStandingAllowed = lastManStanding
            )
            val matchId = matchDao.insertMatch(match)
            
            team1Names.forEach { name ->
                matchDao.insertPlayer(PlayerEntity(matchId = matchId, teamNumber = 1, playerName = name))
            }
            team2Names.forEach { name ->
                matchDao.insertPlayer(PlayerEntity(matchId = matchId, teamNumber = 2, playerName = name))
            }
            
            loadMatch(matchId)
        }
    }

    fun loadMatch(matchId: Long) {
        matchObservationJob?.cancel()
        matchObservationJob = viewModelScope.launch {
            combine(
                matchDao.getMatch(matchId),
                matchDao.getPlayersForMatch(matchId),
                matchDao.getBallEventsForMatch(matchId)
            ) { match, players, events ->
                Triple(match, players, events)
            }.collect { (match, players, events) ->
                recalculateState(match, players, events)
            }
        }
    }
    
    fun closeMatch() {
        matchObservationJob?.cancel()
        _matchState.value = MatchState()
        _liveScore.value = null
    }

    private fun recalculateState(match: MatchEntity, players: List<PlayerEntity>, allEvents: List<BallEventEntity>) {
        val currentInnings = match.currentInnings
        val inningsEvents = allEvents.filter { it.innings == currentInnings }
        
        var currentOver = 0
        var ballsBowled = 0
        var totalScore = 0
        var totalWickets = 0
        var outPlayerIds = mutableSetOf<Long>()
        
        var strikerId: Long? = inningsEvents.lastOrNull()?.strikerId
        var nonStrikerId: Long? = inningsEvents.lastOrNull()?.nonStrikerId
        var currentBowlerId: Long? = inningsEvents.lastOrNull()?.bowlerId

        for (event in inningsEvents) {
            totalScore += event.runsScored + event.extraRuns
            if (event.extraType != "NONE") {
                totalScore += 1
            } else {
                ballsBowled += 1
            }

            if (event.wicketType != "NONE") {
                totalWickets += 1
                event.playerOutId?.let { outPlayerIds.add(it) }
            }

            val rotateOnRuns = (event.runsScored + event.extraRuns) % 2 != 0
            if (rotateOnRuns) {
                val temp = event.strikerId
                strikerId = event.nonStrikerId
                nonStrikerId = temp
            } else {
                strikerId = event.strikerId
                nonStrikerId = event.nonStrikerId
            }

            if (ballsBowled == 6) {
                currentOver += 1
                ballsBowled = 0
                val temp = strikerId
                strikerId = nonStrikerId
                nonStrikerId = temp
            }
        }

        val totalBattingPlayers = if (currentInnings == 1) match.team1Players else match.team2Players
        val inningsLimitReached = currentOver >= match.totalOvers || totalWickets >= (totalBattingPlayers - 1)
        
        var isGameOver = false
        var inningsEnded = inningsLimitReached

        if (currentInnings == 2) {
            val target = match.targetScore ?: 0
            if (totalScore >= target) {
                isGameOver = true
                inningsEnded = true
            } else if (inningsLimitReached) {
                isGameOver = true
                inningsEnded = true
            }
        }

        _matchState.value = MatchState(
            match = match,
            players = players,
            events = inningsEvents,
            currentOver = currentOver,
            ballsBowledInOver = ballsBowled,
            totalScore = totalScore,
            totalWickets = totalWickets,
            strikerId = strikerId,
            nonStrikerId = nonStrikerId,
            currentBowlerId = currentBowlerId,
            outPlayerIds = outPlayerIds,
            target = match.targetScore,
            isGameOver = isGameOver || (match.isComplete),
            inningsEnded = inningsEnded
        )

        _liveScore.value = LiveScoreDto(
            team1Name = match.team1Name,
            team2Name = match.team2Name,
            currentScore = totalScore,
            wickets = totalWickets,
            currentOver = currentOver,
            ballsBowled = ballsBowled
        )
    }

    fun startSecondInnings() {
        val state = _matchState.value
        val match = state.match ?: return
        if (match.currentInnings == 2) return

        viewModelScope.launch {
            val updatedMatch = match.copy(
                currentInnings = 2,
                targetScore = state.totalScore + 1
            )
            matchDao.updateMatch(updatedMatch)
            // Reset local state will happen via flow collection
        }
    }

    fun setInitialPlayers(striker: Long, nonStriker: Long, bowler: Long) {
        _matchState.value = _matchState.value.copy(
            strikerId = striker,
            nonStrikerId = nonStriker,
            currentBowlerId = bowler
        )
    }
    
    fun setNextBatsman(playerId: Long) {
        val current = _matchState.value
        _matchState.value = current.copy(strikerId = playerId)
    }
    
    fun setNextBowler(playerId: Long) {
        _matchState.value = _matchState.value.copy(currentBowlerId = playerId)
    }

    fun addRun(runs: Int) {
        val state = _matchState.value
        val match = state.match ?: return
        val sId = state.strikerId ?: return
        val nsId = state.nonStrikerId ?: return
        val bId = state.currentBowlerId ?: return
        
        if (state.isGameOver || state.inningsEnded) return

        viewModelScope.launch {
            val event = BallEventEntity(
                matchId = match.matchId,
                overNumber = state.currentOver,
                ballNumberInOver = state.ballsBowledInOver + 1,
                strikerId = sId,
                nonStrikerId = nsId,
                bowlerId = bId,
                runsScored = runs,
                extraType = "NONE",
                extraRuns = 0,
                wicketType = "NONE",
                playerOutId = null,
                innings = match.currentInnings
            )
            matchDao.insertBallEvent(event)
        }
    }

    fun addExtra(type: String, runs: Int) {
        val state = _matchState.value
        val match = state.match ?: return
        val sId = state.strikerId ?: return
        val nsId = state.nonStrikerId ?: return
        val bId = state.currentBowlerId ?: return
        
        if (state.isGameOver || state.inningsEnded) return

        viewModelScope.launch {
            val batRuns = if (type == "NO_BALL") runs else 0
            val extraRuns = if (type == "WIDE") runs else 0
            
            val event = BallEventEntity(
                matchId = match.matchId,
                overNumber = state.currentOver,
                ballNumberInOver = state.ballsBowledInOver,
                strikerId = sId,
                nonStrikerId = nsId,
                bowlerId = bId,
                runsScored = batRuns,
                extraType = type,
                extraRuns = extraRuns,
                wicketType = "NONE",
                playerOutId = null,
                innings = match.currentInnings
            )
            matchDao.insertBallEvent(event)
        }
    }

    fun addWicket(type: String, extraType: String = "NONE") {
        val state = _matchState.value
        val match = state.match ?: return
        val sId = state.strikerId ?: return
        val nsId = state.nonStrikerId ?: return
        val bId = state.currentBowlerId ?: return
        
        if (state.isGameOver || state.inningsEnded) return

        viewModelScope.launch {
            val ballNum = if (extraType == "NONE") state.ballsBowledInOver + 1 else state.ballsBowledInOver
            val event = BallEventEntity(
                matchId = match.matchId,
                overNumber = state.currentOver,
                ballNumberInOver = ballNum,
                strikerId = sId,
                nonStrikerId = nsId,
                bowlerId = bId,
                runsScored = 0,
                extraType = extraType,
                extraRuns = 0,
                wicketType = type,
                playerOutId = sId,
                innings = match.currentInnings
            )
            matchDao.insertBallEvent(event)
        }
    }

    fun undoLastBall() {
        val state = _matchState.value
        val match = state.match ?: return
        viewModelScope.launch {
            matchDao.deleteLastBallEventIfInCurrentOver(match.matchId, state.currentOver)
        }
    }

    fun deleteMatch(match: MatchEntity) {
        viewModelScope.launch {
            matchDao.deleteMatch(match)
        }
    }
}
