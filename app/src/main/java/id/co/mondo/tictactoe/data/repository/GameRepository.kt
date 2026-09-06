package id.co.mondo.tictactoe.data.repository

import id.co.mondo.tictactoe.data.local.dao.GameHistoryDao
import id.co.mondo.tictactoe.data.local.entity.GameHistoryEntity
import id.co.mondo.tictactoe.data.local.entity.LeaderboardTop
import id.co.mondo.tictactoe.util.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GameRepository @Inject constructor(private val dao: GameHistoryDao) {

    suspend fun createOfflineSession(playerX: String, playerO: String): Result<GameHistoryEntity> {
        return try {
            require(playerX.isNotBlank() && playerO.isNotBlank()) { "Nama player tidak boleh kosong" }
            require(playerX.trim() != playerO.trim()) { "Nama Player X dan O harus berbeda" }
            val roomId = "off_" + (1..6)
                .map { ('A'..'Z') + ('0'..'9') }
                .map { it.random() }
                .joinToString("")
            val playedAt = System.currentTimeMillis()
            val entity = GameHistoryEntity(
                roomId = roomId,
                playerX = playerX,
                playerO = playerO,
                playedAt = playedAt
            )
            dao.insertGame(entity)
            Result.Success(entity)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun updateGameResult(roomId: String, win: String): Result<String> {
        return try {
            require(roomId.isNotBlank()) { "Room ID tidak boleh kosong" }
            require(win.isNotBlank()) { "Hasil game tidak boleh kosong" }
            dao.updateResult(roomId, win)
            Result.Success("Hasil game untuk room $roomId berhasil diperbarui")
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    fun getGameRoom(roomId: String): Flow<GameHistoryEntity?> {
        return dao.getGameByRoomId(roomId)
    }

    fun getHistory(limit: Int = 10): Flow<List<GameHistoryEntity>> {
        return dao.getHistoryGames(limit)
    }

    fun getLeaderboard(limit: Int = 10): Flow<List<LeaderboardTop>> {
        return dao.getLeaderboard(limit)
    }


}