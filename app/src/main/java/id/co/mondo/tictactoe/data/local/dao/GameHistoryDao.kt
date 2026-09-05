package id.co.mondo.tictactoe.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import id.co.mondo.tictactoe.data.local.entity.GameHistoryEntity
import id.co.mondo.tictactoe.data.local.entity.GameStateResult
import kotlinx.coroutines.flow.Flow

@Dao
interface GameHistoryDao {

    @Insert
    suspend fun insertGame(game: GameHistoryEntity)

    @Query("SELECT * FROM game_history ORDER BY playedAt ASC LIMIT 10")
    fun getRecentGames(): Flow<List<GameHistoryEntity>>

    @Query("""
            SELECT 
                SUM(CASE WHEN result = 'X' THEN 1 ELSE 0 END) AS winAsX,
                SUM(CASE WHEN result = 'O' THEN 1 ELSE 0 END) AS winAsO,
                SUM(CASE WHEN result = 'Draw' THEN 1 ELSE 0 END) AS drawCount
        FROM game_history WHERE roomId = :roomId
    """)
    fun getScoreResult(roomId: String): Flow<GameStateResult>

    @Query("SELECT COUNT(*) FROM game_history")
    suspend fun getGameCount(): Int

}
