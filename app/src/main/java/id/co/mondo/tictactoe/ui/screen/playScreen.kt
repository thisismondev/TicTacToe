package id.co.mondo.tictactoe.ui.screen

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import id.co.mondo.tictactoe.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayScreen(navController: NavController, viewModel: gRpcViewModel) {

    val state by viewModel.roomUpdate.collectAsState()

    LaunchedEffect(state) {
        Log.d("PlayScreen", "State changed: $state")
    }

    val localPlayer by viewModel.localPlayer.collectAsState()

    val room = (state as? UiState.Success)?.data?.room

    if (room == null) {
        Log.e("PlayScreen", "Room is null")
        return
    }

    val players = room.playersList

    val isMyTurn = room.turn == localPlayer

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showSheet = remember { mutableStateOf(false) }
    var resultText = remember { mutableStateOf("") }

    // Deteksi kalau ada pemenang atau seri
    LaunchedEffect(room.winner) {
        if (!room.winner.isNullOrEmpty()) {

            resultText.value = when(room.winner) {
                localPlayer -> "🎉 Kamu Menang!"
                "draw" -> "🤝 Seri!"
                else -> "😢 Kamu Kalah!"
            }

            showSheet.value = true
        }
    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .padding(start = 24.dp, end = 24.dp)
                .fillMaxWidth()
                .height(100.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = players.getOrNull(0) ?: "Player 1",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "X",
                        color = Color.Red,
                        style = MaterialTheme.typography.headlineMedium
                    )

                    if (room.turn == players.getOrNull(0))
                        Text("Giliran", color = Color.Green)
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = players.getOrNull(1) ?: "Player 2",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "O",
                        color = Color.Blue,
                        style = MaterialTheme.typography.headlineMedium
                    )

                    if (room.turn == players.getOrNull(1))
                        Text("Giliran", color = Color.Green)
                }
            }
        }
        Spacer(modifier = Modifier.padding(32.dp))

        Box(
            modifier = Modifier
                .size(315.dp),
            contentAlignment = Alignment.Center,
        ) {
            LazyVerticalGrid(
                modifier = Modifier
                    .size(300.dp),
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                items( 9) { index ->
                    val row = index / 3
                    val col = index % 3
                    val cellValue = room.boardList[index]

                    Box(
                        modifier = Modifier
                            .aspectRatio(1F)
                            .border(2.dp, Color.Black)
                            .clickable(enabled = isMyTurn && cellValue.isEmpty()) {
                                viewModel.makeMove(
                                    row = row,
                                    col = col,
                                    playerName = localPlayer,
                                    roomId = room.roomId
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = cellValue)
                    }

                }

            }
        }
    }

    if (showSheet.value) {
        ModalBottomSheet(
            onDismissRequest = { showSheet.value = false },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = resultText.value,
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        showSheet.value = false
                        navController.navigate("home")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Keluar")
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun previewPlayScreen() {
    MaterialTheme {
//        PlayScreen(navController =  rememberNavController())
    }
}
