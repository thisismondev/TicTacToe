package id.co.mondo.tictactoe.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import id.co.mondo.tictactoe.ui.screen.PlayScreen
import id.co.mondo.tictactoe.ui.screen.gRpcViewModel
import id.co.mondo.tictactoe.ui.screen.homeScreen

@Composable
fun TicTacToeApp() {

    val navController = rememberNavController()
    val viewModel: gRpcViewModel = viewModel()


    NavHost(navController, startDestination = "home") {
        composable("home") {
            homeScreen(navController,viewModel)
        }
        composable("play") {
            PlayScreen(navController, viewModel)
        }
    }
}