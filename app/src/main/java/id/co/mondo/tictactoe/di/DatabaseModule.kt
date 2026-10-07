package id.co.mondo.tictactoe.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import id.co.mondo.tictactoe.data.local.AppDatabase
import id.co.mondo.tictactoe.data.local.dao.GameHistoryDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "tictactoe_database"
        ).fallbackToDestructiveMigration(true).build()
    }

    @Provides
    fun provideGameHistoryDao(database: AppDatabase): GameHistoryDao {
        return database.gameHistoryDao()
    }
}
