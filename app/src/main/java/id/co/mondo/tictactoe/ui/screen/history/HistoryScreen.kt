package id.co.mondo.tictactoe.ui.screen.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import id.co.mondo.tictactoe.data.local.entity.GameHistoryEntity
import id.co.mondo.tictactoe.ui.component.ErrorContent
import id.co.mondo.tictactoe.ui.component.LoadingContent
import id.co.mondo.tictactoe.ui.theme.ComponentStyles
import id.co.mondo.tictactoe.ui.theme.TicTacToeTheme
import id.co.mondo.tictactoe.util.UiState
import java.text.SimpleDateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    navController: NavController,
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val historyState by viewModel.historyState.collectAsStateWithLifecycle()

    HistoryScreenContent(
        historyState = historyState,
        onBackClick = { navController.popBackStack() },
        onRetryClick = { viewModel.retry() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreenContent(
    historyState: UiState<List<GameHistoryEntity>>,
    onBackClick: () -> Unit,
    onRetryClick: () -> Unit = {}
) {
    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Riwayat Game", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
        ) {
            when (val state = historyState) {
                is UiState.Loading -> {
                    LoadingContent(
                        message = "Memuat riwayat...",
                        modifier = Modifier.fillMaxSize()
                    )
                }
                is UiState.Empty -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Belum ada riwayat permainan",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                is UiState.Error -> {
                    ErrorContent(
                        message = state.errorMessage,
                        onRetryClick = onRetryClick,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                is UiState.Success -> {
                    HistoryGrid(history = state.data)
                }
            }
        }
    }
}

@Composable
private fun HistoryGrid(
    history: List<GameHistoryEntity>,
    modifier: Modifier = Modifier
) {
    if (history.isEmpty()) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = "Belum ada riwayat permainan",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        return
    }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 300.dp),
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(history, key = { it.roomId + it.playedAt }) { item ->
            HistoryItem(item = item)
        }
    }
}

@Composable
private fun HistoryItem(item: GameHistoryEntity) {
    val dateText = formatPlayedAt(item.playedAt)
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = ComponentStyles.cardShape,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${item.playerX} (X) vs ${item.playerO} (O)",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = dateText,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = "Skor  X: ${item.winAsX}  |  O: ${item.winAsO}  |  Draw: ${item.drawCount}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Room ID: ${item.roomId}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Composable
private fun formatPlayedAt(timestamp: Long): String {
    val locale = LocalConfiguration.current.locales[0]
    return SimpleDateFormat("dd MMM yyyy HH:mm", locale).format(Date(timestamp))
}

@Preview(name = "Phone Portrait", device = Devices.PHONE, showBackground = true)
@Preview(name = "Tablet Landscape", device = Devices.TABLET, showBackground = true)
@Composable
private fun HistoryContentPreview() {
    TicTacToeTheme {
        HistoryScreenContent(
            historyState = UiState.Success(
                listOf(
                    GameHistoryEntity(roomId = "off_ABC123", playerX = "Raqhib", playerO = "Mondo", winAsX = 2, winAsO = 1, drawCount = 1, playedAt = System.currentTimeMillis()),
                    GameHistoryEntity(roomId = "off_XYZ789", playerX = "Alex", playerO = "Raqhib", winAsX = 0, winAsO = 3, drawCount = 0, playedAt = System.currentTimeMillis() - 86400000)
                )
            ),
            onBackClick = {},
            onRetryClick = {}
        )
    }
}
