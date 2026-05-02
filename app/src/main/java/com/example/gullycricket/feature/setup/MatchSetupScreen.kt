package com.example.gullycricket.feature.setup

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.gullycricket.feature.scoring.ScoringViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchSetupScreen(viewModel: ScoringViewModel) {
    var team1Name by remember { mutableStateOf("Team A") }
    var team2Name by remember { mutableStateOf("Team B") }
    var totalOvers by remember { mutableStateOf("5") }
    var team1PlayerCount by remember { mutableStateOf("11") }
    var team2PlayerCount by remember { mutableStateOf("11") }
    var lastManStanding by remember { mutableStateOf(true) }
    
    var showPlayerNames by remember { mutableStateOf(false) }
    
    val t1Count = team1PlayerCount.toIntOrNull() ?: 0
    val t2Count = team2PlayerCount.toIntOrNull() ?: 0
    
    val team1PlayerNames = remember(t1Count) { mutableStateListOf(*Array(t1Count) { "Player ${it + 1}" }) }
    val team2PlayerNames = remember(t2Count) { mutableStateListOf(*Array(t2Count) { "Player ${it + 1}" }) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Match Setup", style = MaterialTheme.typography.headlineLarge)

        OutlinedTextField(
            value = team1Name,
            onValueChange = { team1Name = it },
            label = { Text("Team 1 Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = team2Name,
            onValueChange = { team2Name = it },
            label = { Text("Team 2 Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = totalOvers,
            onValueChange = { totalOvers = it },
            label = { Text("Total Overs") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = team1PlayerCount,
                onValueChange = { team1PlayerCount = it },
                label = { Text("T1 Players") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            OutlinedTextField(
                value = team2PlayerCount,
                onValueChange = { team2PlayerCount = it },
                label = { Text("T2 Players") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Last Man Standing Rule")
            Switch(
                checked = lastManStanding,
                onCheckedChange = { lastManStanding = it }
            )
        }

        Button(onClick = { showPlayerNames = !showPlayerNames }) {
            Text(if (showPlayerNames) "Hide Player Names" else "Set Player Names")
        }

        if (showPlayerNames) {
            Text("Team 1 Players", style = MaterialTheme.typography.titleMedium)
            for (i in 0 until t1Count) {
                OutlinedTextField(
                    value = team1PlayerNames.getOrElse(i) { "" },
                    onValueChange = { team1PlayerNames[i] = it },
                    label = { Text("Player ${i + 1}") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Text("Team 2 Players", style = MaterialTheme.typography.titleMedium)
            for (i in 0 until t2Count) {
                OutlinedTextField(
                    value = team2PlayerNames.getOrElse(i) { "" },
                    onValueChange = { team2PlayerNames[i] = it },
                    label = { Text("Player ${i + 1}") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.startNewMatch(
                    team1Name,
                    team2Name,
                    totalOvers.toIntOrNull() ?: 5,
                    t1Count,
                    t2Count,
                    team1PlayerNames.toList(),
                    team2PlayerNames.toList(),
                    lastManStanding
                )
            },
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text("Start Match", style = MaterialTheme.typography.titleMedium)
        }
        
        Spacer(modifier = Modifier.height(48.dp))
    }
}
