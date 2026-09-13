package id.co.mondo.tictactoe.ui.screen.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import id.co.mondo.tictactoe.data.local.entity.GameHistoryEntity
import id.co.mondo.tictactoe.ui.theme.TicTacToeTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    navController: NavController,
    viewModel: HistoryViewModel = hiltViewModel()
) {
//    val topPlayers by viewModel.topPlayers.collectAsStateWithLifecycle()
    val history by viewModel.history.collectAsStateWithLifecycle()

    HistoryScreenContent(
//        topPlayers = topPlayers,
        history = history,
        onBackClick = { navController.popBackStack() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreenContent(
//    topPlayers: List<TopPlayer>,
    history: List<GameHistoryEntity>,
    onBackClick: () -> Unit
) {
//    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
//    val tabs = listOf("Leaderboard", "History")

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Riwayat") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
        ) {
//            PrimaryTabRow(selectedTabIndex = selectedTab) {
//                tabs.forEachIndexed { index, title ->
//                    Tab(
//                        selected = selectedTab == index,
//                        onClick = { selectedTab = index },
//                        text = { Text(title) },
//                        icon = {
//                            Icon(
//                                imageVector = if (index == 0) Icons.Filled.Star else Icons.Filled.DateRange,
//                                contentDescription = null
//                            )
//                        }
//                    )
//                }
//            }
            HistoryTab(history = history, modifier = Modifier.weight(1f))

//            when (selectedTab) {
//                0 -> LeaderboardTab(topPlayers = topPlayers, modifier = Modifier.weight(1f))
//                1 ->
//            }
        }
    }
}

//@Composable
//private fun LeaderboardTab(
//    topPlayers: List<TopPlayer>,
//    modifier: Modifier = Modifier
//) {
//    if (topPlayers.isEmpty()) {
//        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//            Text("Belum ada data", style = MaterialTheme.typography.bodyLarge)
//        }
//        return
//    }
//    LazyColumn(
//        modifier = modifier.fillMaxSize(),
//        verticalArrangement = Arrangement.spacedBy(8.dp),
//        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp)
//    ) {
//        itemsIndexed(topPlayers) { _, player ->
//            LeaderboardItem(player = player)
//        }
//    }
//}

@Composable
private fun HistoryTab(
    history: List<GameHistoryEntity>,
    modifier: Modifier = Modifier
) {
    if (history.isEmpty()) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Belum ada riwayat", style = MaterialTheme.typography.bodyLarge)
        }
        return
    }
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp)
    ) {
        items(history) { item ->
            HistoryItem(item = item)
        }
    }
}

//@Composable
//private fun LeaderboardItem(player: TopPlayer) {
//    Card(
//        modifier = Modifier.fillMaxWidth(),
//        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
//                Text(
//                    text = "#${player.rank}",
//                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
//                    color = when (player.rank) {
//                        1 -> MaterialTheme.colorScheme.primary
//                        2 -> MaterialTheme.colorScheme.secondary
//                        3 -> MaterialTheme.colorScheme.tertiary
//                        else -> MaterialTheme.colorScheme.onSurface
//                    }
//                )
//                Column {
//                    Text(text = player.name, style = MaterialTheme.typography.titleMedium)
//                    Text(
//                        text = "Win rate: ${(player.winRate * 100).toInt()}%",
//                        style = MaterialTheme.typography.bodySmall,
//                        color = MaterialTheme.colorScheme.onSurfaceVariant
//                    )
//                }
//            }
//            Text(
//                text = "W:${player.wins} L:${player.losses} D:${player.draws}",
//                style = MaterialTheme.typography.bodyMedium,
//                fontWeight = FontWeight.SemiBold
//            )
//        }
//    }
//}

@Composable
private fun HistoryItem(item: GameHistoryEntity) {
    val date = formatPlayedAt(item.playedAt)
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "${item.playerX} (X) vs ${item.playerO} (O)", style = MaterialTheme.typography.titleSmall)
                Text(text = date, style = MaterialTheme.typography.labelSmall)
            }
            Text(
                text = "Skor X:${item.winAsX} O:${item.winAsO} Draw:${item.drawCount}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Room: ${item.roomId}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun formatPlayedAt(timestamp: Long): String {
    return SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault()).format(Date(timestamp))
}

@Preview(showBackground = true)
@Composable
private fun HistoryContentLeaderboardPreview() {
    TicTacToeTheme {
        HistoryScreenContent(
//            topPlayers = listOf(
//                TopPlayer(1, "Raqhib", 5, 1, 2),
//                TopPlayer(2, "Mondo", 3, 2, 1),
//                TopPlayer(3, "Alex", 1, 4, 0)
//            ),
            history = emptyList(),
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HistoryContentHistoryPreview() {
    TicTacToeTheme {
        HistoryScreenContent(
//            topPlayers = emptyList(),
            history = listOf(
                GameHistoryEntity(roomId = "off_ABC123", playerX = "Raqhib", playerO = "Mondo", winAsX = 2, winAsO = 1, drawCount = 1, playedAt = System.currentTimeMillis()),
                GameHistoryEntity(roomId = "off_XYZ789", playerX = "Alex", playerO = "Raqhib", winAsX = 0, winAsO = 3, drawCount = 0, playedAt = System.currentTimeMillis() - 86400000)
            ),
            onBackClick = {}
        )
    }
}
