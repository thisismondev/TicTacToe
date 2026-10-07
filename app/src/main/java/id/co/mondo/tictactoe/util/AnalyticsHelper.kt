package id.co.mondo.tictactoe.util

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnalyticsHelper @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics
) {
    fun logScreenView(screenName: String) {
        val bundle = Bundle().apply {
            putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            putString(FirebaseAnalytics.Param.SCREEN_CLASS, screenName)
        }
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
    }

    fun logGameStarted(playerX: String, playerO: String, mode: String = "offline") {
        val bundle = Bundle().apply {
            putString("game_mode", mode)
            putString("player_x", playerX)
            putString("player_o", playerO)
        }
        firebaseAnalytics.logEvent("game_started", bundle)
    }

    fun logGameFinished(winner: String, roomId: String) {
        val bundle = Bundle().apply {
            putString("winner", winner)
            putString("room_id", roomId)
        }
        firebaseAnalytics.logEvent("game_finished", bundle)
    }

    fun logMenuClick(menuName: String) {
        val bundle = Bundle().apply {
            putString("menu_name", menuName)
        }
        firebaseAnalytics.logEvent("menu_click", bundle)
    }
}
