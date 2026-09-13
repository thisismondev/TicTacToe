package id.co.mondo.tictactoe.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "game_history",
    indices = [
        Index(value = ["playerX"]),
        Index(value = ["playerO"]),
        Index(value = ["playedAt"])
    ]
)
data class GameHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val roomId: String,
    val playerX: String,
    val playerO: String,
    val winAsX: Int = 0,
    val winAsO: Int = 0,
    val drawCount: Int = 0,
    val playedAt: Long
)

data class LeaderboardTop(
    val player: String,
    val totalWin: Int,
    val totalLoss: Int,
    val totalDraw: Int
)