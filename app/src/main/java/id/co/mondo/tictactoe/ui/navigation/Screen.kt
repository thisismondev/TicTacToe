package id.co.mondo.tictactoe.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Play : Screen("play/{roomId}") {
        fun createRoute(roomId: String): String {
            return "play/$roomId"
        }
    }
    object History : Screen("history")
}
