package id.co.mondo.tictactoe.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import id.co.mondo.tictactoe.ui.navigation.Screen
import id.co.mondo.tictactoe.ui.screen.history.HistoryScreen
import id.co.mondo.tictactoe.ui.screen.home.HomeScreen
import id.co.mondo.tictactoe.ui.screen.offline.OfflineSetupScreen
import id.co.mondo.tictactoe.ui.screen.play.PlayScreen

@Composable
fun TicTacToeApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }

        composable(Screen.OfflineSetup.route) {
            OfflineSetupScreen(navController = navController)
        }

        composable(
            route = Screen.Play.route,
            arguments = listOf(
                navArgument("roomId") { type = NavType.StringType }
            )
        ) {
            PlayScreen(
                navController = navController,
            )
        }

        composable(Screen.History.route) {
            HistoryScreen(navController = navController)
        }
    }
}
