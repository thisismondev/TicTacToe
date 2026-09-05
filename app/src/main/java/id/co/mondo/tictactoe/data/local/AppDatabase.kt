package id.co.mondo.tictactoe.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import id.co.mondo.tictactoe.data.local.dao.GameHistoryDao
import id.co.mondo.tictactoe.data.local.entity.GameHistoryEntity

@Database(
    entities = [GameHistoryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun gameHistoryDao(): GameHistoryDao
}
