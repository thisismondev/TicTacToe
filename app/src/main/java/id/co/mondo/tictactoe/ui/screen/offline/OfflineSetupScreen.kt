package id.co.mondo.tictactoe.ui.screen.offline

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import id.co.mondo.tictactoe.ui.component.ErrorInlineContent
import id.co.mondo.tictactoe.ui.navigation.Screen
import id.co.mondo.tictactoe.ui.theme.ComponentStyles
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
                title = { Text("Main Offline", style = MaterialTheme.typography.titleLarge) },
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
                .consumeWindowInsets(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 520.dp),
                    shape = ComponentStyles.cardShape,
                    elevation = CardDefaults.cardElevation(defaultElevation = ComponentStyles.cardElevation)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        Text(
                            text = "Masukkan Nama Pemain",
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
                            shape = ComponentStyles.inputShape
                        )

                        OutlinedTextField(
                            value = playerO,
                            enabled = !isLoading,
                            singleLine = true,
                            onValueChange = onPlayerOChange,
                            label = { Text("Nama Player O") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = ComponentStyles.inputShape
                        )

                        if (errorMessage != null) {
                            ErrorInlineContent(
                                message = errorMessage,
                                onRetryClick = onStartClick
                            )
                        }

                        Button(
                            enabled = !isLoading && playerX.isNotBlank() && playerO.isNotBlank(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = ComponentStyles.buttonShape,
                            onClick = onStartClick
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    strokeWidth = 2.dp,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                                Text(
                                    text = "Membuat Room...",
                                    style = MaterialTheme.typography.labelLarge,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            } else {
                                Text(
                                    text = "Mulai Permainan",
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(name = "Phone Portrait", device = Devices.PHONE, showBackground = true)
@Preview(name = "Tablet Landscape", device = Devices.TABLET, showBackground = true)
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

@Preview(name = "Loading", showBackground = true)
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

@Preview(name = "Error", showBackground = true)
@Composable
private fun OfflineSetupContentErrorPreview() {
    TicTacToeTheme {
        OfflineSetupContent(
            playerX = "Raqhib",
            onPlayerXChange = {},
            playerO = "Raqhib",
            onPlayerOChange = {},
            isLoading = false,
            errorMessage = "Nama Player X dan O harus berbeda",
            onBackClick = {},
            onStartClick = {}
        )
    }
}
