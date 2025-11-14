package id.co.mondo.tictactoe.ui.screen

import android.widget.Toast
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
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun PlayScreen() {

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Player()
        Spacer(modifier = Modifier.padding(32.dp))

        Box(
            modifier = Modifier
                .size(315.dp)
            ,
            contentAlignment = Alignment.Center,
        ) {
            LazyVerticalGrid(
                modifier = Modifier
                    .size(300.dp)
                    .border(2.dp, Color.Black)
                    .clip(RoundedCornerShape(20.dp)),
                columns = GridCells.Fixed(3),
            ) {

                items(9) { index ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .aspectRatio(1F)
                            .clip(RoundedCornerShape(20.dp))
                            .border(2.dp, Color.Black)
                            .clickable {
                                Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show()
                            }
                    ) {
                        Text(text = "O", modifier = Modifier.align(Alignment.Center))
                    }

                }

            }
        }


    }
}


@Composable
fun Player() {
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
                .weight(1F),
            shape = RoundedCornerShape(16.dp),

            ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Player 1")
                Text("X")
            }
        }

        Card(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1F),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Player 2")
                Text("O")
            }
        }

    }
}


@Preview(showBackground = true)
@Composable
fun previewPlayScreen() {
    MaterialTheme {
        PlayScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun previewProfile() {
    Player()
}