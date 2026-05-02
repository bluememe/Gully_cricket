package com.example.gullycricket.feature.leaderboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gullycricket.feature.scoring.ScoringViewModel

data class BatsmanStat(val name: String, val runs: Int)
data class BowlerStat(val name: String, val wickets: Int, val runsConceded: Int, val balls: Int)

@Composable
fun LeaderboardScreen(viewModel: ScoringViewModel) {
    val matchState by viewModel.matchState.collectAsState()
    
    val batsmanStats = remember(matchState.events, matchState.players) {
        val runsMap = mutableMapOf<Long, Int>()
        matchState.events.forEach { event ->
            runsMap[event.strikerId] = (runsMap[event.strikerId] ?: 0) + event.runsScored
        }
        runsMap.map { (id, runs) ->
            val name = matchState.players.find { it.playerId == id }?.playerName ?: "Player $id"
            BatsmanStat(name, runs)
        }.sortedByDescending { it.runs }
    }

    val bowlerStats = remember(matchState.events, matchState.players) {
        val statsMap = mutableMapOf<Long, BowlerStat>()
        matchState.events.forEach { event ->
            val current = statsMap[event.bowlerId] ?: BowlerStat(
                matchState.players.find { it.playerId == event.bowlerId }?.playerName ?: "Player ${event.bowlerId}",
                0, 0, 0
            )
            
            val extraRuns = if (event.extraType != "NONE") 1 else 0
            val runsConceded = event.runsScored + event.extraRuns + extraRuns
            val wicket = if (event.wicketType != "NONE" && event.wicketType != "RUN_OUT") 1 else 0
            val ballCount = if (event.extraType == "NONE") 1 else 0
            
            statsMap[event.bowlerId] = current.copy(
                wickets = current.wickets + wicket,
                runsConceded = current.runsConceded + runsConceded,
                balls = current.balls + ballCount
            )
        }
        statsMap.values.toList().sortedByDescending { it.wickets }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Match Leaderboard", style = MaterialTheme.typography.headlineMedium)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text("Batting", style = MaterialTheme.typography.titleMedium)
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(batsmanStats) { stat ->
                ListItem(
                    headlineContent = { Text(stat.name) },
                    trailingContent = { Text("${stat.runs} Runs", style = MaterialTheme.typography.titleLarge) }
                )
                Divider()
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        
        Text("Bowling", style = MaterialTheme.typography.titleMedium)
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(bowlerStats) { stat ->
                val overs = "${stat.balls / 6}.${stat.balls % 6}"
                ListItem(
                    headlineContent = { Text(stat.name) },
                    supportingContent = { Text("Overs: $overs | Runs: ${stat.runsConceded}") },
                    trailingContent = { Text("${stat.wickets} Wkts", style = MaterialTheme.typography.titleLarge) }
                )
                Divider()
            }
        }
    }
}
