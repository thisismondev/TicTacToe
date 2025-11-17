package id.co.mondo.tictactoe.ui.screen

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
import id.co.mondo.tictactoe.ui.TicTacToeApp


@Composable
fun homeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            text = "TicTacToe", style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )
        radioButtonRoom()
        Column(modifier = Modifier.fillMaxWidth().padding(32.dp)) {
            Text(text = "ID Room: ")
            Text("Player 1: ")
            Text("Player 2: ")
            Spacer(Modifier.padding(16.dp))
            Button(modifier = Modifier.fillMaxWidth(),onClick = { navController.navigate("play") }) {
                Text("Mulai permainan")
            }
        }
    }
}

@Composable
fun radioButtonRoom() {
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
            value = "",
            onValueChange = {},
            label = { Text("Masukan nama anda") }
        )
        Spacer(Modifier.padding(16.dp))
        when (selecOption) {
            "create" -> Button(
                contentPadding = PaddingValues(start = 35.dp, end = 35.dp),
                onClick = { }) { Text("Tunggu lawan") }

            "join" -> searchRoom()
        }
    }


}

@Composable
fun searchRoom() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        TextField(
            value = "",
            onValueChange = {},
            label = { Text("Masukan ID Room") }
        )
        Button(modifier = Modifier.fillMaxWidth(), onClick = { }) {
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
        searchRoom()
    }
}


@Preview(showBackground = true)
@Composable
fun previeSelectOption() {
    MaterialTheme {
        radioButtonRoom()
    }
}