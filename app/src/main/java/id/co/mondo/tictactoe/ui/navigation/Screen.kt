package id.co.mondo.tictactoe.ui.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object OfflineSetup : Screen("offline_setup")
    data object Play : Screen("play/{roomId}") {
        fun createRoute(roomId: String): String {
            return "play/$roomId"
        }
    }
    data object History : Screen("history")
}
