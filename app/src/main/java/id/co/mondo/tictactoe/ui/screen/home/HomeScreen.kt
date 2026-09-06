package id.co.mondo.tictactoe.ui.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import id.co.mondo.tictactoe.ui.navigation.Screen
import id.co.mondo.tictactoe.ui.theme.TicTacToeTheme
import id.co.mondo.tictactoe.util.UiState

@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {

    val roomState by homeViewModel.roomState.collectAsStateWithLifecycle()

    var playerX by remember { mutableStateOf("") }
    var playerO by remember { mutableStateOf("") }
    var isOnlineMode by remember { mutableStateOf(true) }

    LaunchedEffect(roomState) {
        val state = roomState
        if (state is UiState.Success) {
            val game = state.data
            navController.navigate(
                Screen.Play.createRoute(
                    roomId = game.roomId
                )
            ) { popUpTo(Screen.Home.route) { inclusive = true } }
        }
    }

    val isLoading = roomState is UiState.Loading
    val errorMessage = (roomState as? UiState.Error)?.errorMessage

    HomeScreenContent(
        isMode = isOnlineMode,
        onModeChange = { isOnlineMode = it },
        playerX = playerX,
        onPlayerXChange = { playerX = it },
        playerO = playerO,
        onPlayerOChange = { playerO = it },
        disabled = isLoading,
        errorMessage = errorMessage,
        onStartGame = { homeViewModel.startOfflineGame(playerX, playerO) },
        onOpenLeaderboard = { navController.navigate(Screen.History.route) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    isMode: Boolean,
    onModeChange: (Boolean) -> Unit,
    playerX: String,
    onPlayerXChange: (String) -> Unit,
    playerO: String,
    onPlayerOChange: (String) -> Unit,
    disabled: Boolean,
    errorMessage: String?,
    onStartGame: () -> Unit,
    onOpenLeaderboard: () -> Unit
) {
    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "TicTacToe",
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Mode Selection
                    Text(
                        text = "Mode Permainan",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    SingleChoiceSegmentedButtonRow(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        SegmentedButton(
                            selected = isMode,
                            onClick = { onModeChange(true) },
                            shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(SegmentedButtonDefaults.IconSize)
                                )
                            }
                        ) {
                            Text("Online")
                        }
                        SegmentedButton(
                            selected = !isMode,
                            onClick = { onModeChange(false) },
                            shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = null,
                                    modifier = Modifier.size(SegmentedButtonDefaults.IconSize)
                                )
                            }
                        ) {
                            Text("Offline")
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = playerX,
                        enabled = !disabled,
                        singleLine = true,
                        onValueChange = onPlayerXChange,
                        label = { Text("Nama Player X") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium
                    )
                    OutlinedTextField(
                        value = playerO,
                        enabled = !disabled,
                        singleLine = true,
                        onValueChange = onPlayerOChange,
                        label = { Text("Nama Player O") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium
                    )

                    if (errorMessage != null) {
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Button(
                        enabled = !disabled && playerX.isNotBlank() && playerO.isNotBlank(),
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onStartGame
                    ) {
                        Text("Mulai Permainan")
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // History Button - adaptive: min 48dp touch target, History icon
            Button(
                onClick = onOpenLeaderboard,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 48.dp),
                colors = ButtonDefaults.filledTonalButtonColors()
            ) {
                Icon(Icons.Default.DateRange, contentDescription = null)
                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                Text("Riwayat & Leaderboard")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewHomeScreenOnline() {
    TicTacToeTheme {
        HomeScreenContent(
            isMode = true,
            onModeChange = {},
            playerX = "",
            onPlayerXChange = {},
            playerO = "",
            onPlayerOChange = {},
            disabled = false,
            errorMessage = null,
            onStartGame = {},
            onOpenLeaderboard = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewHomeScreenOffline() {
    TicTacToeTheme {
        HomeScreenContent(
            isMode = false,
            onModeChange = {},
            playerX = "",
            onPlayerXChange = {},
            playerO = "",
            onPlayerOChange = {},
            disabled = false,
            errorMessage = null,
            onStartGame = {},
            onOpenLeaderboard = {}
        )
    }
}