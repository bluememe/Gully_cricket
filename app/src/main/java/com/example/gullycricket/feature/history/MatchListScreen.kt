package com.example.gullycricket.feature.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gullycricket.feature.scoring.ScoringViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchListScreen(viewModel: ScoringViewModel, onStartNewMatch: () -> Unit) {
    val matches by viewModel.allMatches.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Match History") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onStartNewMatch) {
                Icon(Icons.Default.Add, contentDescription = "New Match")
            }
        }
    ) { padding ->
        if (matches.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("No matches yet. Tap + to start!")
            }
        } else {
            var matchToDelete by remember { mutableStateOf<com.example.gullycricket.core.database.entity.MatchEntity?>(null) }

            if (matchToDelete != null) {
                AlertDialog(
                    onDismissRequest = { matchToDelete = null },
                    title = { Text("Delete Match") },
                    text = { Text("Are you sure you want to delete this match history?") },
                    confirmButton = {
                        TextButton(onClick = {
                            viewModel.deleteMatch(matchToDelete!!)
                            matchToDelete = null
                        }) { Text("Delete", color = MaterialTheme.colorScheme.error) }
                    },
                    dismissButton = {
                        TextButton(onClick = { matchToDelete = null }) { Text("Cancel") }
                    }
                )
            }

            LazyColumn(modifier = Modifier.fillMaxSize().padding(padding)) {
                items(matches) { match ->
                    ListItem(
                        headlineContent = { Text("${match.team1Name} vs ${match.team2Name}") },
                        supportingContent = { Text("Overs: ${match.totalOvers} | Players: ${match.team1Players}v${match.team2Players}") },
                        trailingContent = {
                            IconButton(onClick = { matchToDelete = match }) {
                                Icon(androidx.compose.material.icons.Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                            }
                        },
                        modifier = Modifier.clickable { viewModel.loadMatch(match.matchId) }
                    )
                    Divider()
                }
            }
        }
    }
}
