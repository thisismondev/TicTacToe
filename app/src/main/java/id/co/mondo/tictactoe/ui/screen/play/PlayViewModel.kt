package id.co.mondo.tictactoe.ui.screen.play

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.mondo.tictactoe.data.local.model.Cell
import id.co.mondo.tictactoe.data.local.model.GamePlay
import id.co.mondo.tictactoe.data.local.model.GameRoom
import id.co.mondo.tictactoe.data.local.model.Winner
import id.co.mondo.tictactoe.data.local.model.WinningLine
import id.co.mondo.tictactoe.data.repository.GameRepository
import id.co.mondo.tictactoe.util.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val roomId: String = savedStateHandle.get<String>("roomId") ?: ""
    private var starter: Cell = Cell.X

    private val _roomState = MutableStateFlow<UiState<GameRoom>>(UiState.Loading)
    val roomState: StateFlow<UiState<GameRoom>> = _roomState.asStateFlow()

    private val _gameState = MutableStateFlow(GamePlay(roomId = roomId, turn = starter))
    val gameState: StateFlow<GamePlay> = _gameState.asStateFlow()

    private val _showSheet = MutableStateFlow(false)
    val showSheet: StateFlow<Boolean> = _showSheet.asStateFlow()

    private val _resultText = MutableStateFlow("")
    val resultText: StateFlow<String> = _resultText.asStateFlow()

    init {
        loadRoom(roomId)
    }

    private fun loadRoom(roomId: String) {
        viewModelScope.launch {
            gameRepository.getGameRoom(roomId).collect { entity ->
                if (entity != null) {
                    _roomState.value = UiState.Success(
                        GameRoom(
                            roomId = entity.roomId,
                            playerX = entity.playerX,
                            playerO = entity.playerO,
                            winAsX = entity.winAsX,
                            winAsO = entity.winAsO,
                            drawCount = entity.drawCount
                        )
                    )
                } else _roomState.value = UiState.Error("Room not found")
            }
        }
    }

    fun makeMove(row: Int, col: Int) {
        val cur = _gameState.value
        if (cur.winner != null || cur.board[row][col] != Cell.EMPTY) return
        val newBoard = cur.board.mapIndexed { r, rowList ->
            rowList.mapIndexed { c, cell ->
                if (r == row && c == col) cur.turn else cell
            }
        }
        val winResult = checkWinner(newBoard)
        val nextTurn = if (cur.turn == Cell.X) Cell.O else Cell.X
        val newGame = if (winResult != null) {
            cur.copy(board = newBoard, winner = winResult.first, winningLine = winResult.second)
        } else {
            cur.copy(board = newBoard, turn = nextTurn)
        }
        _gameState.value = newGame
        if (winResult != null) onGameFinished(winResult.first)
    }

    private fun checkWinner(board: List<List<Cell>>): Pair<Winner, WinningLine?>? {
        for (r in 0..2) {
            if (board[r][0] != Cell.EMPTY && board[r][0] == board[r][1] && board[r][1] == board[r][2]) {
                val winner = if (board[r][0] == Cell.X) Winner.X else Winner.O
                val line = when (r) { 0 -> WinningLine.ROW_0; 1 -> WinningLine.ROW_1; else -> WinningLine.ROW_2 }
                return winner to line
            }
        }
        for (c in 0..2) {
            if (board[0][c] != Cell.EMPTY && board[0][c] == board[1][c] && board[1][c] == board[2][c]) {
                val winner = if (board[0][c] == Cell.X) Winner.X else Winner.O
                val line = when (c) { 0 -> WinningLine.COL_0; 1 -> WinningLine.COL_1; else -> WinningLine.COL_2 }
                return winner to line
            }
        }
        if (board[0][0] != Cell.EMPTY && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            val winner = if (board[0][0] == Cell.X) Winner.X else Winner.O
            return winner to WinningLine.DIAG_TL_BR
        }
        if (board[0][2] != Cell.EMPTY && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            val winner = if (board[0][2] == Cell.X) Winner.X else Winner.O
            return winner to WinningLine.DIAG_TR_BL
        }
        if (board.flatten().none { it == Cell.EMPTY }) return Winner.DRAW to null
        return null
    }

    private fun onGameFinished(winner: Winner) {
        val room = (_roomState.value as? UiState.Success)?.data
        viewModelScope.launch {
            gameRepository.updateGameResult(roomId, winner.name)
        }
        _resultText.value = when (winner) {
            Winner.X -> "${room?.playerX ?: "Player X"} Menang!"
            Winner.O -> "${room?.playerO ?: "Player O"} Menang!"
            Winner.DRAW -> "Seri!"
        }
        _showSheet.value = true
    }

    fun onMainLagi() {
        _showSheet.value = false
        starter = if (starter == Cell.X) Cell.O else Cell.X
        _gameState.value = GamePlay(roomId = roomId, turn = starter, winningLine = null)
    }

    fun onSelesai() {
        _showSheet.value = false
    }

    fun dismissSheet() {
        _showSheet.value = false
    }
}
