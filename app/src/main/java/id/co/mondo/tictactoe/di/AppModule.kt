package id.co.mondo.tictactoe.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import id.co.mondo.tictactoe.data.local.AppDatabase
import id.co.mondo.tictactoe.data.local.dao.GameHistoryDao
import id.co.mondo.tictactoe.util.Constants
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

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
    @Singleton
    fun provideFirebaseDatabase(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }

    @Provides
    fun provideGameHistoryDao(database: AppDatabase): GameHistoryDao {
        return database.gameHistoryDao()
    }
    @Provides
    @Singleton
    fun provideGamesRef(database: FirebaseDatabase): DatabaseReference {
        return database.getReference(Constants.PATH_GAMES)
    }
}
