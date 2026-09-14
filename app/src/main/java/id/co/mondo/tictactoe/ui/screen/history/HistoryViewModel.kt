package id.co.mondo.tictactoe.ui.screen.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.mondo.tictactoe.data.local.entity.GameHistoryEntity
import id.co.mondo.tictactoe.data.repository.GameRepository
import id.co.mondo.tictactoe.util.UiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val gameRepository: GameRepository
) : ViewModel() {

//    val topPlayers: StateFlow<List<TopPlayer>> = gameRepository.getLeaderboard(limit = 5)
//        .map { list -> list.mapIndexed { index, entity -> entity.toTopPlayer(rank = index + 1) } }
//        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val retryTrigger = MutableStateFlow(0)

    @OptIn(ExperimentalCoroutinesApi::class)
    val historyState: StateFlow<UiState<List<GameHistoryEntity>>> = retryTrigger
        .flatMapLatest {
            gameRepository.getHistory(limit = 25)
                .map { list ->
                    if (list.isEmpty()) UiState.Empty
                    else UiState.Success(list)
                }
        }
        .onStart { emit(UiState.Loading) }
        .catch { e -> emit(UiState.Error(e.message ?: "Gagal memuat riwayat.")) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UiState.Loading)

    fun retry() {
        retryTrigger.value += 1
    }
}
