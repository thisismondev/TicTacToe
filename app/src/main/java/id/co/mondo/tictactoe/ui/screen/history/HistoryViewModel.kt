package id.co.mondo.tictactoe.ui.screen.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.mondo.tictactoe.data.local.entity.GameHistoryEntity
import id.co.mondo.tictactoe.data.local.entity.toTopPlayer
import id.co.mondo.tictactoe.data.local.model.TopPlayer
import id.co.mondo.tictactoe.data.repository.GameRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    gameRepository: GameRepository
) : ViewModel() {

    val topPlayers: StateFlow<List<TopPlayer>> = gameRepository.getLeaderboard(limit = 5)
        .map { list -> list.mapIndexed { index, entity -> entity.toTopPlayer(rank = index + 1) } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val history: StateFlow<List<GameHistoryEntity>> = gameRepository.getHistory(limit = 25)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
