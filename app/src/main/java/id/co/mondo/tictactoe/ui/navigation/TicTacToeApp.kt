package id.co.mondo.tictactoe.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.EntryPointAccessors
import id.co.mondo.tictactoe.di.AnalyticsEntryPoint
import id.co.mondo.tictactoe.ui.screen.history.HistoryScreen
import id.co.mondo.tictactoe.ui.screen.home.HomeScreen
import id.co.mondo.tictactoe.ui.screen.offline.OfflineSetupScreen
import id.co.mondo.tictactoe.ui.screen.play.PlayScreen

@Composable
fun TicTacToeApp() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val analyticsHelper = remember(context) {
        EntryPointAccessors.fromApplication(
            context.applicationContext,
            AnalyticsEntryPoint::class.java
        ).analyticsHelper()
    }

    LaunchedEffect(navController) {
        navController.currentBackStackEntryFlow.collect { backStackEntry ->
            val route = backStackEntry.destination.route ?: return@collect
            analyticsHelper.logScreenView(route)
        }
    }

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
