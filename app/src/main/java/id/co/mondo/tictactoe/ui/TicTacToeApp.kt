package id.co.mondo.tictactoe.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import id.co.mondo.tictactoe.ui.navigation.Screen
import id.co.mondo.tictactoe.ui.screen.home.HomeScreen
import id.co.mondo.tictactoe.ui.screen.leaderboard.LeaderboardScreen
import id.co.mondo.tictactoe.ui.screen.play.PlayScreen

@Composable
fun TicTacToeApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }

        composable(
            route = Screen.Play.route,
            arguments = listOf(
                navArgument("roomId") { type = NavType.StringType },
                navArgument("playerName") { type = NavType.StringType },
                navArgument("isOnline") { type = NavType.BoolType }
            )
        ) { backStackEntry ->
            val roomId = backStackEntry.arguments?.getString("roomId") ?: ""
            val playerName = backStackEntry.arguments?.getString("playerName") ?: ""
            val isOnline = backStackEntry.arguments?.getBoolean("isOnline") ?: true

            PlayScreen(
                navController = navController,
                roomId = roomId,
                playerName = playerName,
                isOnline = isOnline
            )
        }

        composable(Screen.Leaderboard.route) {
            LeaderboardScreen(navController = navController)
        }
    }
}
