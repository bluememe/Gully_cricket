package com.example.gullycricket.feature.scoring

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import com.example.gullycricket.feature.setup.MatchSetupScreen
import com.example.gullycricket.feature.leaderboard.LeaderboardScreen
import com.example.gullycricket.util.CanvasExportUtil
import com.example.gullycricket.feature.history.MatchListScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoringDashboardScreen(viewModel: ScoringViewModel) {
    val matchState by viewModel.matchState.collectAsState()
    var isCreatingNewMatch by remember { mutableStateOf(false) }
    var currentTab by remember { mutableStateOf(0) }
    val context = LocalContext.current

    if (matchState.match == null) {
        if (isCreatingNewMatch) {
            MatchSetupScreen(viewModel)
        } else {
            MatchListScreen(viewModel, onStartNewMatch = { isCreatingNewMatch = true })
        }
        return
    }

    var showInitialSelection by remember { mutableStateOf(false) }
    var showNextBatsmanSelection by remember { mutableStateOf(false) }
    var showNextBowlerSelection by remember { mutableStateOf(false) }

    LaunchedEffect(matchState.match, matchState.strikerId, matchState.currentBowlerId) {
        if ((matchState.strikerId == null || matchState.nonStrikerId == null || matchState.currentBowlerId == null) && !matchState.inningsEnded) {
            showInitialSelection = true
        }
    }

    LaunchedEffect(matchState.totalWickets) {
        if (matchState.totalWickets > 0 && !matchState.isGameOver && !matchState.inningsEnded) {
            showNextBatsmanSelection = true
        }
    }
    
    LaunchedEffect(matchState.currentOver) {
        if (matchState.currentOver > 0 && matchState.ballsBowledInOver == 0 && !matchState.isGameOver && !matchState.inningsEnded) {
            showNextBowlerSelection = true
        }
    }

    if (showInitialSelection) {
        val battingTeam = if (matchState.match?.currentInnings == 1) 1 else 2
        val bowlingTeam = if (matchState.match?.currentInnings == 1) 2 else 1
        PlayerSelectionDialog(
            title = "Innings ${matchState.match?.currentInnings} - Select Players",
            battingPlayers = matchState.players.filter { it.teamNumber == battingTeam },
            bowlingPlayers = matchState.players.filter { it.teamNumber == bowlingTeam },
            onSelected = { s, ns, b ->
                viewModel.setInitialPlayers(s, ns, b)
                showInitialSelection = false
            }
        )
    }

    if (showNextBatsmanSelection) {
        val battingTeam = if (matchState.match?.currentInnings == 1) 1 else 2
        val available = matchState.players.filter { it.teamNumber == battingTeam && it.playerId !in matchState.outPlayerIds && it.playerId != matchState.nonStrikerId && it.playerId != matchState.strikerId }
        SinglePlayerSelectionDialog(
            title = "Select Next Batsman",
            players = available,
            onSelected = {
                viewModel.setNextBatsman(it)
                showNextBatsmanSelection = false
            }
        )
    }

    if (showNextBowlerSelection) {
        val bowlingTeam = if (matchState.match?.currentInnings == 1) 2 else 1
        val available = matchState.players.filter { it.teamNumber == bowlingTeam }
        SinglePlayerSelectionDialog(
            title = "Select Next Bowler",
            players = available,
            onSelected = {
                viewModel.setNextBowler(it)
                showNextBowlerSelection = false
            }
        )
    }

    if (matchState.inningsEnded && !matchState.isGameOver && matchState.match?.currentInnings == 1) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("Innings Ended") },
            text = { Text("Team 1 scored ${matchState.totalScore}. Team 2 needs ${matchState.totalScore + 1} to win.") },
            confirmButton = {
                Button(onClick = { viewModel.startSecondInnings() }) { Text("Start 2nd Innings") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gully Cricket") },
                navigationIcon = {
                    IconButton(onClick = { viewModel.closeMatch() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { currentTab = if (currentTab == 0) 1 else 0 }) {
                        Icon(Icons.Default.List, contentDescription = "Leaderboard")
                    }
                    IconButton(onClick = { 
                        val uri = CanvasExportUtil.exportMatchSummary(context, matchState)
                        if (uri != null) {
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "image/png"
                                putExtra(Intent.EXTRA_STREAM, uri)
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            }
                            context.startActivity(Intent.createChooser(intent, "Share Scorecard"))
                        }
                    }) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (currentTab == 1) {
                LeaderboardScreen(viewModel)
            } else {
                ScoringDashboard(viewModel, matchState)
            }
        }
    }
}

@Composable
fun PlayerSelectionDialog(
    title: String,
    battingPlayers: List<com.example.gullycricket.core.database.entity.PlayerEntity>,
    bowlingPlayers: List<com.example.gullycricket.core.database.entity.PlayerEntity>,
    onSelected: (Long, Long, Long) -> Unit
) {
    var striker by remember { mutableStateOf<Long?>(null) }
    var nonStriker by remember { mutableStateOf<Long?>(null) }
    var bowler by remember { mutableStateOf<Long?>(null) }

    AlertDialog(
        onDismissRequest = {},
        title = { Text(title) },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Select Striker", style = MaterialTheme.typography.titleSmall)
                battingPlayers.forEach { p ->
                    RadioButtonOption(p.playerName, striker == p.playerId) { striker = p.playerId }
                }
                
                Text("Select Non-Striker", style = MaterialTheme.typography.titleSmall)
                battingPlayers.forEach { p ->
                    if (p.playerId != striker) {
                        RadioButtonOption(p.playerName, nonStriker == p.playerId) { nonStriker = p.playerId }
                    }
                }

                Text("Select Bowler", style = MaterialTheme.typography.titleSmall)
                bowlingPlayers.forEach { p ->
                    RadioButtonOption(p.playerName, bowler == p.playerId) { bowler = p.playerId }
                }
            }
        },
        confirmButton = {
            Button(
                enabled = striker != null && nonStriker != null && bowler != null,
                onClick = { onSelected(striker!!, nonStriker!!, bowler!!) }
            ) { Text("Start") }
        }
    )
}

@Composable
fun SinglePlayerSelectionDialog(
    title: String,
    players: List<com.example.gullycricket.core.database.entity.PlayerEntity>,
    onSelected: (Long) -> Unit
) {
    var selected by remember { mutableStateOf<Long?>(null) }

    AlertDialog(
        onDismissRequest = {},
        title = { Text(title) },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                players.forEach { p ->
                    RadioButtonOption(p.playerName, selected == p.playerId) { selected = p.playerId }
                }
            }
        },
        confirmButton = {
            Button(enabled = selected != null, onClick = { onSelected(selected!!) }) { Text("Select") }
        }
    )
}

@Composable
fun RadioButtonOption(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().clickable { onClick() }) {
        RadioButton(selected = isSelected, onClick = onClick)
        Text(text)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoringDashboard(viewModel: ScoringViewModel, matchState: MatchState) {
    var showNoBallDialog by remember { mutableStateOf(false) }
    var showWideBallDialog by remember { mutableStateOf(false) }
    var showWicketDialog by remember { mutableStateOf(false) }

    val currentStriker = matchState.players.find { it.playerId == matchState.strikerId }?.playerName ?: "Striker"
    val currentNonStriker = matchState.players.find { it.playerId == matchState.nonStrikerId }?.playerName ?: "Non-Striker"
    val currentBowler = matchState.players.find { it.playerId == matchState.currentBowlerId }?.playerName ?: "Bowler"

    if (showNoBallDialog) {
        AlertDialog(
            onDismissRequest = { showNoBallDialog = false },
            title = { Text("No Ball - Runs from Bat?") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("How many runs did the batsman score?")
                    val runOptions = listOf(0, 1, 2, 3, 4, 6)
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        runOptions.chunked(3).forEach { rowRuns ->
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                rowRuns.forEach { runs ->
                                    Button(
                                        onClick = {
                                            viewModel.addExtra("NO_BALL", runs)
                                            showNoBallDialog = false
                                        },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(8.dp)
                                    ) { Text("$runs") }
                                }
                            }
                        }
                    }
                    
                    Divider(modifier = Modifier.padding(vertical = 4.dp))
                    
                    Button(
                        onClick = {
                            viewModel.addWicket("RUN_OUT", "NO_BALL")
                            showNoBallDialog = false
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("OUT (Run Out)")
                    }
                }
            },
            confirmButton = { TextButton(onClick = { showNoBallDialog = false }) { Text("Cancel") } }
        )
    }

    if (showWideBallDialog) {
        AlertDialog(
            onDismissRequest = { showWideBallDialog = false },
            title = { Text("Wide Ball - Extra Runs?") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("How many extra runs (running/boundary)?")
                    val wideOptions = listOf(0, 1, 2, 3, 4)
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        wideOptions.chunked(3).forEach { rowRuns ->
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                rowRuns.forEach { runs ->
                                    Button(
                                        onClick = {
                                            viewModel.addExtra("WIDE", runs)
                                            showWideBallDialog = false
                                        },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(8.dp)
                                    ) { Text("$runs") }
                                }
                                if (rowRuns.size < 3) {
                                    Spacer(modifier = Modifier.weight(3f - rowRuns.size))
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = { TextButton(onClick = { showWideBallDialog = false }) { Text("Cancel") } }
        )
    }

    if (showWicketDialog) {
        AlertDialog(
            onDismissRequest = { showWicketDialog = false },
            title = { Text("Select Wicket Type") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    val lastEvent = matchState.events.lastOrNull()
                    val types = if (lastEvent?.extraType == "NO_BALL") listOf("RUN_OUT") else listOf("BOWLED", "CAUGHT", "RUN_OUT", "STUMPED", "LBW")
                    types.forEach { type ->
                        Button(onClick = { viewModel.addWicket(type); showWicketDialog = false }, modifier = Modifier.fillMaxWidth()) { Text(type) }
                    }
                }
            },
            confirmButton = { TextButton(onClick = { showWicketDialog = false }) { Text("Cancel") } }
        )
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())) {
        Card(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer), shape = RoundedCornerShape(16.dp)) {
            Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(if (matchState.match?.currentInnings == 1) "Innings 1" else "Innings 2 - Chase", style = MaterialTheme.typography.labelLarge)
                Text("${matchState.match?.team1Name} vs ${matchState.match?.team2Name}", fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "${matchState.totalScore}/${matchState.totalWickets}", fontSize = 64.sp, color = MaterialTheme.colorScheme.onPrimaryContainer)
                Text(text = "Overs: ${matchState.currentOver}.${matchState.ballsBowledInOver} / ${matchState.match?.totalOvers}", fontSize = 20.sp)
                
                matchState.target?.let {
                    Text("Target: $it", fontSize = 18.sp, color = Color.Red, fontWeight = FontWeight.Bold)
                    Text("Need ${it - matchState.totalScore} from ${(matchState.match!!.totalOvers * 6) - (matchState.currentOver * 6 + matchState.ballsBowledInOver)} balls", fontSize = 14.sp)
                }

                Divider(modifier = Modifier.padding(vertical = 12.dp))
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text("Batsmen:", style = MaterialTheme.typography.labelMedium)
                        Text("* $currentStriker", style = MaterialTheme.typography.bodyLarge, color = Color.Blue)
                        Text("  $currentNonStriker", style = MaterialTheme.typography.bodyLarge)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Bowler:", style = MaterialTheme.typography.labelMedium)
                        Text(currentBowler, style = MaterialTheme.typography.bodyLarge, color = Color.Red)
                    }
                }

                if (matchState.isGameOver) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("MATCH OVER", color = Color.Red, fontSize = 24.sp, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    
                    val resultText = remember(matchState.totalScore, matchState.target) {
                        if (matchState.target != null) {
                            when {
                                matchState.totalScore >= matchState.target -> "${matchState.match?.team2Name} WON!"
                                matchState.totalScore < matchState.target - 1 -> "${matchState.match?.team1Name} WON!"
                                else -> "MATCH TIED!"
                            }
                        } else "Innings Ended"
                    }
                    Text(resultText, color = MaterialTheme.colorScheme.primary, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        // Ball History Summary
        Text("Current Over History:", style = MaterialTheme.typography.labelMedium, modifier = Modifier.padding(bottom = 8.dp))
        LazyRow(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            val overEvents = matchState.events.filter { it.overNumber == matchState.currentOver }
            items(overEvents) { event ->
                val label = when {
                    event.wicketType != "NONE" -> "W"
                    event.extraType == "WIDE" -> "Wd"
                    event.extraType == "NO_BALL" -> "Nb"
                    event.runsScored == 0 -> "•"
                    else -> "${event.runsScored + event.extraRuns}"
                }
                val bgColor = when {
                    event.wicketType != "NONE" -> Color.Red
                    event.runsScored == 4 -> Color(0xFF4CAF50)
                    event.runsScored == 6 -> Color(0xFF9C27B0)
                    else -> Color.LightGray
                }
                Box(
                    modifier = Modifier.size(40.dp).background(bgColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(label, color = if (bgColor == Color.LightGray) Color.Black else Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }

        val buttons = listOf("0", "1", "2", "3", "4", "6")
        LazyVerticalGrid(columns = GridCells.Fixed(3), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.height(180.dp)) {
            items(buttons.size) { index ->
                val run = buttons[index].toInt()
                Button(onClick = { viewModel.addRun(run) }, modifier = Modifier.fillMaxSize(), shape = RoundedCornerShape(8.dp), enabled = !matchState.isGameOver && matchState.strikerId != null) {
                    Text(buttons[index], fontSize = 28.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = { showWideBallDialog = true }, modifier = Modifier.weight(1f).height(80.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)), shape = RoundedCornerShape(12.dp), enabled = !matchState.isGameOver) { Text("WIDE", fontSize = 20.sp) }
            Button(onClick = { showNoBallDialog = true }, modifier = Modifier.weight(1f).height(80.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF57C00)), shape = RoundedCornerShape(12.dp), enabled = !matchState.isGameOver) { Text("NO BALL", fontSize = 20.sp) }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = { showWicketDialog = true }, modifier = Modifier.weight(1f).height(80.dp), colors = ButtonDefaults.buttonColors(containerColor = Color.Black), shape = RoundedCornerShape(12.dp), enabled = !matchState.isGameOver) { Text("WICKET", fontSize = 20.sp) }
            Button(onClick = { viewModel.undoLastBall() }, modifier = Modifier.weight(1f).height(80.dp), colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray), shape = RoundedCornerShape(12.dp), enabled = matchState.events.isNotEmpty() && !matchState.isGameOver) { Text("UNDO", fontSize = 20.sp) }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}
