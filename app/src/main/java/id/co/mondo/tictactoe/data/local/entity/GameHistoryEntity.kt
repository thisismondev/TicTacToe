package id.co.mondo.tictactoe.data.local.entity

import androidx.room.ColumnInfo
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
    val result: String,
    val playedAt: Long,
    val duration: Long? = null,
)

data class GameStateResult(
    @ColumnInfo(name = "winAsX") val winAsX: Int,
    @ColumnInfo(name = "winAsO") val winAsO: Int,
    @ColumnInfo(name = "drawCount") val drawCount: Int,
)
