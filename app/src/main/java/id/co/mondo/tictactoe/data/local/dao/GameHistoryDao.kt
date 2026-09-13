package id.co.mondo.tictactoe.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import id.co.mondo.tictactoe.data.local.entity.GameHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GameHistoryDao {

    @Insert
    suspend fun insertGame(game: GameHistoryEntity)

    @Query("""
    UPDATE game_history 
    SET 
        winAsX = CASE WHEN :winner = 'X' THEN winAsX + 1 ELSE winAsX END,
        winAsO = CASE WHEN :winner = 'O' THEN winAsO + 1 ELSE winAsO END,
        drawCount = CASE WHEN :winner = 'DRAW' THEN drawCount + 1 ELSE drawCount END
    WHERE roomId = :roomId
""")
    suspend fun updateResult(roomId: String, winner: String)

    @Query(
        "Select * from game_history where roomId = :roomId"
    )
    fun getGameByRoomId(roomId: String): Flow<GameHistoryEntity?>

    @Query("SELECT * FROM game_history ORDER BY playedAt ASC LIMIT :limit")
    fun getHistoryGames(limit: Int = 10): Flow<List<GameHistoryEntity>>

}
