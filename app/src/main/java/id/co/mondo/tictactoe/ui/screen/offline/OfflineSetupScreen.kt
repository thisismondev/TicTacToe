package id.co.mondo.tictactoe.ui.screen.offline

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.navigation.compose.rememberNavController
import id.co.mondo.tictactoe.ui.navigation.Screen
import id.co.mondo.tictactoe.ui.theme.TicTacToeTheme
import id.co.mondo.tictactoe.util.UiState

@Composable
fun OfflineSetupScreen(
    navController: NavController,
    viewModel: OfflineSetupViewModel = hiltViewModel()
) {
    val roomState by viewModel.roomState.collectAsStateWithLifecycle()

    var playerX by remember { mutableStateOf("") }
    var playerO by remember { mutableStateOf("") }

    LaunchedEffect(roomState) {
        val state = roomState
        if (state is UiState.Success) {
            navController.navigate(Screen.Play.createRoute(roomId = state.data.roomId)) {
                popUpTo(Screen.Home.route) { inclusive = false }
            }
        }
    }

    val isLoading = roomState is UiState.Loading
    val errorMessage = (roomState as? UiState.Error)?.errorMessage

    OfflineSetupContent(
        playerX = playerX,
        onPlayerXChange = { playerX = it },
        playerO = playerO,
        onPlayerOChange = { playerO = it },
        isLoading = isLoading,
        errorMessage = errorMessage,
        onBackClick = { navController.popBackStack() },
        onStartClick = { viewModel.startGame(playerX, playerO) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OfflineSetupContent(
    playerX: String,
    onPlayerXChange: (String) -> Unit,
    playerO: String,
    onPlayerOChange: (String) -> Unit,
    isLoading: Boolean,
    errorMessage: String?,
    onBackClick: () -> Unit,
    onStartClick: () -> Unit
) {
    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Main Offline") },
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
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
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
                    Text(
                        text = "Nama Pemain",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = playerX,
                        enabled = !isLoading,
                        singleLine = true,
                        onValueChange = onPlayerXChange,
                        label = { Text("Nama Player X") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium
                    )
                    OutlinedTextField(
                        value = playerO,
                        enabled = !isLoading,
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
                        enabled = !isLoading && playerX.isNotBlank() && playerO.isNotBlank(),
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onStartClick
                    ) {
                        Text(if (isLoading) "Membuat Room..." else "Mulai Permainan")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OfflineSetupContentPreview() {
    TicTacToeTheme {
        OfflineSetupContent(
            playerX = "Raqhib",
            onPlayerXChange = {},
            playerO = "Mondo",
            onPlayerOChange = {},
            isLoading = false,
            errorMessage = null,
            onBackClick = {},
            onStartClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OfflineSetupContentLoadingPreview() {
    TicTacToeTheme {
        OfflineSetupContent(
            playerX = "Raqhib",
            onPlayerXChange = {},
            playerO = "",
            onPlayerOChange = {},
            isLoading = true,
            errorMessage = null,
            onBackClick = {},
            onStartClick = {}
        )
    }
}
