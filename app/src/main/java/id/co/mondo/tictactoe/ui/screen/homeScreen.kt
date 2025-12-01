package id.co.mondo.tictactoe.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import id.co.mondo.tictactoe.UiState
import id.co.mondo.tictactoe.ui.TicTacToeApp


@Composable
fun homeScreen(navController: NavController, viewModel: gRpcViewModel) {

    val state by viewModel.roomUpdate.collectAsState()
    val goPlay by viewModel.navigateToPlay.collectAsState()

    LaunchedEffect(goPlay) {
        if (goPlay == "playing") {
            Log.d("homeScreen", "Navigating to play screen...")
            navController.navigate("play") {
                popUpTo("home") { inclusive = true }
            }
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "TicTacToe", style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )
        radioButtonRoom(viewModel)
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp)) {
            when (val state = state) {
                UiState.Empty -> Text("Belum ada room")

                UiState.Loading -> Text("Loading...")

                is UiState.Error -> Text("Error: ${state.errorMessage}")
                is UiState.Success -> {
                    val roomId = state.data?.room?.roomId ?: ""
                    val players1 = state.data?.room?.playersList?.getOrNull(0) ?: ""
                    val player2 = state.data?.room?.playersList?.getOrNull(1) ?: ""
                    Text(text = "ID Room: $roomId")
                    Text("Player 1: $players1")
                    Text("Player 2: $player2")
                }
            }
            Spacer(Modifier.padding(16.dp))
            Button(modifier = Modifier.fillMaxWidth(), onClick = { viewModel.startGame() }) {
                Text("Mulai permainan")
            }
        }
    }
}

@Composable
fun radioButtonRoom(viewModel: gRpcViewModel) {

    var name by remember { mutableStateOf("") }
    val roomLocked by viewModel.roomLocked.collectAsState()
    var selecOption by remember { mutableStateOf("create") }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row {
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = selecOption == "create",
                    onClick = { selecOption = "create" }
                )
                Text("Buat Room")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = selecOption == "join",
                    onClick = { selecOption = "join" }
                )
                Text("Join Room")
            }
        }
        Spacer(Modifier.padding(8.dp))
        TextField(
            value = name,
            enabled = !roomLocked,
            singleLine = true,
            onValueChange = {
                name = it
            },
            label = { Text("Masukan nama anda") }
        )
        Spacer(Modifier.padding(16.dp))
        when (selecOption) {
            "create" -> Button(
                enabled = !roomLocked,
                contentPadding = PaddingValues(start = 35.dp, end = 35.dp),
                onClick = {
                    viewModel.createRoom(name)
                    name = " "
                }) { Text("Buat Room") }

            "join" -> searchRoom(viewModel = viewModel, name)
        }
    }


}

@Composable
fun searchRoom(viewModel: gRpcViewModel, name: String) {

    var roomId by remember { mutableStateOf("") }
    val roomLocked by viewModel.roomLocked.collectAsState()

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        TextField(
            value = roomId,
            enabled = !roomLocked,
            singleLine = true,
            onValueChange = {
                roomId = it
            },
            label = { Text("Masukan ID Room") }
        )
        Spacer(Modifier.padding(16.dp))
        Button(
            enabled = !roomLocked,
            contentPadding = PaddingValues(start = 35.dp, end = 35.dp),
            onClick = {
                viewModel.joinRoom(name, roomId)
            }) {
            Text("Masuk Room")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun previewTTTApp() {
    MaterialTheme {
        TicTacToeApp()
    }
}

@Preview(showBackground = true)
@Composable
fun previewSearchRoom() {
    MaterialTheme {
//        searchRoom()
    }
}


@Preview(showBackground = true)
@Composable
fun previeSelectOption() {
    MaterialTheme {
//        radioButtonRoom()
    }
}