package id.co.mondo.tictactoe.ui.screen.play

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.activity.compose.BackHandler
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import id.co.mondo.tictactoe.data.local.model.Cell
import id.co.mondo.tictactoe.data.local.model.GamePlay
import id.co.mondo.tictactoe.data.local.model.GameRoom
import id.co.mondo.tictactoe.data.local.model.Winner
import id.co.mondo.tictactoe.data.local.model.WinningLine
import id.co.mondo.tictactoe.ui.component.GameBoard
import id.co.mondo.tictactoe.ui.component.PlayerCard
import id.co.mondo.tictactoe.ui.component.ResultSheet
import id.co.mondo.tictactoe.ui.navigation.Screen
import id.co.mondo.tictactoe.ui.theme.TicTacToeTheme
import id.co.mondo.tictactoe.util.Constants
import id.co.mondo.tictactoe.util.UiState


@Composable
fun PlayScreen(
    navController: NavController,
    viewModel: PlayViewModel = hiltViewModel()
) {

    val roomState by viewModel.roomState.collectAsStateWithLifecycle()
    val game by viewModel.gameState.collectAsStateWithLifecycle()
    val isResultVisible by viewModel.isResultVisible.collectAsStateWithLifecycle()
    val resultText by viewModel.resultText.collectAsStateWithLifecycle()

    val room = (roomState as? UiState.Success)?.data
    if (room == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(if (roomState is UiState.Error) (roomState as UiState.Error).errorMessage else "Loading...")
        }
        return
    }

    var isExitDialogVisible by remember { mutableStateOf(false) }
    val isBoardEmpty = game.board.flatten().all { it == Cell.EMPTY }
    val navigateHome = {
        navController.navigate(Screen.Home.route) {
            popUpTo(0) { inclusive = true }
        }
    }

    BackHandler(enabled = game.winner == null) {
        if (isBoardEmpty) navigateHome() else isExitDialogVisible = true
    }

    PlayScreenContent(
        room = room,
        game = game,
        isResultVisible = isResultVisible,
        resultText = resultText,
        onCellClick = { r, c -> viewModel.makeMove(r, c) },
        onDismissRequest = { viewModel.dismissResult() },
        onPlayAgainClick = { viewModel.playAgain() },
        onFinishClick = {
            viewModel.finishSession()
            navigateHome()
        },
        onBackClick = {
            if (isBoardEmpty || game.winner != null) navigateHome() else isExitDialogVisible = true
        }
    )

    if (isExitDialogVisible) {
        AlertDialog(
            onDismissRequest = { isExitDialogVisible = false },
            title = { Text("Keluar Permainan?") },
            text = { Text("Skor sudah tersimpan, progress board akan hilang.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        isExitDialogVisible = false
                        navigateHome()
                    }
                ) { Text("Keluar") }
            },
            dismissButton = {
                TextButton(onClick = { isExitDialogVisible = false }) { Text("Batal") }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun PlayScreenContent(
    room: GameRoom,
    game: GamePlay,
    isResultVisible: Boolean,
    resultText: String,
    onCellClick: (Int, Int) -> Unit,
    onDismissRequest: () -> Unit,
    onPlayAgainClick: () -> Unit,
    onFinishClick: () -> Unit,
    onBackClick: () -> Unit = {}
) {
    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Room ${room.roomId.take(6)}") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors()
            )
        }
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
        ) {
            val isWideScreen = maxWidth >= 600.dp

            if (isWideScreen) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Column(
                        modifier = Modifier.weight(0.38f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        ScoreCard(
                            room.playerX,
                            room.playerO,
                            room.winAsX,
                            room.winAsO,
                            room.drawCount
                        )
                        PlayerCardsRow(room.playerX, room.playerO, game.turn, game.winner == null)
                    }
                    Box(
                        modifier = Modifier.weight(0.62f),
                        contentAlignment = Alignment.Center
                    ) {
                        GameBoard(
                            board = game.board,
                            winningLine = game.winningLine,
                            isMyTurn = game.winner == null,
                            onCellClick = onCellClick
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ScoreCard(room.playerX, room.playerO, room.winAsX, room.winAsO, room.drawCount)
                    PlayerCardsRow(room.playerX, room.playerO, game.turn, game.winner == null)
                    GameBoard(
                        board = game.board,
                        winningLine = game.winningLine,
                        isMyTurn = game.winner == null,
                        onCellClick = onCellClick
                    )
                }
            }
        }
    }

    if (isResultVisible) {
        ResultSheet(
            resultText = resultText,
            onDismissRequest = onDismissRequest,
            onPlayAgainClick = onPlayAgainClick,
            onFinishClick = onFinishClick
        )
    }
}

@Composable
private fun ScoreCard(
    playerX: String,
    playerO: String,
    winAsX: Int,
    winAsO: Int,
    drawCount: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ScoreItem(
                label = playerX,
                value = winAsX.toString(),
                isX = true,
                modifier = Modifier.weight(1f)
            )
            VerticalDivider(modifier = Modifier.height(40.dp))
            ScoreItem(
                label = "Draw",
                value = drawCount.toString(),
                isX = null,
                modifier = Modifier.weight(1f)
            )
            VerticalDivider(modifier = Modifier.height(40.dp))
            ScoreItem(
                label = playerO,
                value = winAsO.toString(),
                isX = false,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun ScoreItem(
    label: String,
    value: String,
    isX: Boolean?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            maxLines = 1,
            textAlign = TextAlign.Center
        )
        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = when (isX) {
                true -> androidx.compose.ui.graphics.Color.Red
                false -> androidx.compose.ui.graphics.Color.Blue
                else -> MaterialTheme.colorScheme.onSurfaceVariant
            }
        )
    }
}

@Composable
private fun PlayerCardsRow(
    playerX: String,
    playerO: String,
    turn: Cell,
    isPlaying: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        PlayerCard(
            playerName = playerX,
            symbol = Constants.PLAYER_X,
            isYourTurn = isPlaying && turn == Cell.X,
            modifier = Modifier.weight(1f)
        )
        PlayerCard(
            playerName = playerO,
            symbol = Constants.PLAYER_O,
            isYourTurn = isPlaying && turn == Cell.O,
            modifier = Modifier.weight(1f)
        )
    }
}

private fun previewRoom() =
    GameRoom("off_ABC123", "Raqhib", "Mondo", winAsX = 2, winAsO = 1, drawCount = 1)

private fun previewEmptyBoard() = GamePlay(
    roomId = "off_ABC123",
    board = List(3) { List(3) { Cell.EMPTY } },
    turn = Cell.X,
    winner = null
)

private fun previewMidBoard() = GamePlay(
    roomId = "off_ABC123",
    board = listOf(
        listOf(Cell.X, Cell.EMPTY, Cell.EMPTY),
        listOf(Cell.EMPTY, Cell.O, Cell.EMPTY),
        listOf(Cell.EMPTY, Cell.EMPTY, Cell.EMPTY)
    ),
    turn = Cell.X,
    winner = null
)

private fun previewWinningBoard() = GamePlay(
    roomId = "off_ABC123",
    board = listOf(
        listOf(Cell.X, Cell.X, Cell.X),
        listOf(Cell.O, Cell.O, Cell.EMPTY),
        listOf(Cell.EMPTY, Cell.EMPTY, Cell.EMPTY)
    ),
    turn = Cell.O,
    winner = Winner.X,
    winningLine = WinningLine.ROW_0
)

private fun previewDrawBoard() = GamePlay(
    roomId = "off_ABC123",
    board = listOf(
        listOf(Cell.X, Cell.O, Cell.X),
        listOf(Cell.X, Cell.O, Cell.O),
        listOf(Cell.O, Cell.X, Cell.O)
    ),
    turn = Cell.X,
    winner = Winner.DRAW
)

@Preview(name = "Phone - Playing", device = Devices.PHONE, showBackground = true)
@Preview(name = "Tablet - Playing", device = Devices.TABLET, showBackground = true)
@Preview(
    name = "Phone - Dark",
    device = Devices.PHONE,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun PlayContentPlayingPreview() {
    TicTacToeTheme {
        PlayScreenContent(
            room = previewRoom(),
            game = previewEmptyBoard(),
            isResultVisible = false,
            resultText = "",
            onCellClick = { _, _ -> },
            onDismissRequest = {},
            onPlayAgainClick = {},
            onFinishClick = {}
        )
    }
}

@Preview(name = "Mid Game - Back", showBackground = true)
@Composable
private fun PlayContentMidGamePreview() {
    TicTacToeTheme {
        PlayScreenContent(
            room = previewRoom(),
            game = previewMidBoard(),
            isResultVisible = false,
            resultText = "",
            onCellClick = { _, _ -> },
            onDismissRequest = {},
            onPlayAgainClick = {},
            onFinishClick = {}
        )
    }
}

@Preview(name = "Winning - Row", showBackground = true)
@Composable
private fun PlayContentWinningPreview() {
    TicTacToeTheme {
        PlayScreenContent(
            room = previewRoom(),
            game = previewWinningBoard(),
            isResultVisible = true,
            resultText = "Raqhib Menang!",
            onCellClick = { _, _ -> },
            onDismissRequest = {},
            onPlayAgainClick = {},
            onFinishClick = {}
        )
    }
}

@Preview(name = "Draw", showBackground = true)
@Composable
private fun PlayContentDrawPreview() {
    TicTacToeTheme {
        PlayScreenContent(
            room = previewRoom(),
            game = previewDrawBoard(),
            isResultVisible = true,
            resultText = "Seri!",
            onCellClick = { _, _ -> },
            onDismissRequest = {},
            onPlayAgainClick = {},
            onFinishClick = {}
        )
    }
}
