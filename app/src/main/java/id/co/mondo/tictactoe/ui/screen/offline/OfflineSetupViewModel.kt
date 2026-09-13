package id.co.mondo.tictactoe.ui.screen.offline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.mondo.tictactoe.data.local.model.GameRoom
import id.co.mondo.tictactoe.data.repository.GameRepository
import id.co.mondo.tictactoe.util.Result
import id.co.mondo.tictactoe.util.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OfflineSetupViewModel @Inject constructor(
    private val gameRepository: GameRepository
) : ViewModel() {
    private val _roomState = MutableStateFlow<UiState<GameRoom>>(UiState.Empty)
    val roomState: StateFlow<UiState<GameRoom>> = _roomState.asStateFlow()

    fun startGame(playerX: String, playerO: String) {
        viewModelScope.launch {
            _roomState.value = UiState.Loading
            val res = gameRepository.createOfflineSession(playerX, playerO)
            when (res) {
                is Result.Success -> {
                    val data = GameRoom(
                        roomId = res.data.roomId,
                        playerX = res.data.playerX,
                        playerO = res.data.playerO,
                        winAsX = res.data.winAsX,
                        winAsO = res.data.winAsO,
                        drawCount = res.data.drawCount
                    )
                    _roomState.value = UiState.Success(data)
                }
                is Result.Error -> _roomState.value = UiState.Error(res.exception.message ?: "Gagal Membuat Room")
                is Result.Loading -> _roomState.value = UiState.Loading
            }
        }
    }
}
