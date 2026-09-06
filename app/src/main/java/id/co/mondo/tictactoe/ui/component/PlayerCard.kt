package id.co.mondo.tictactoe.ui.component

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.co.mondo.tictactoe.ui.theme.TicTacToeTheme

@Composable
fun PlayerCard(
    playerName: String,
    symbol: String,
    isYourTurn: Boolean,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isYourTurn) 1.03f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    val borderColor by animateFloatAsState(
        targetValue = if (isYourTurn) 1f else 0f,
        animationSpec = tween(300),
        label = "borderAlpha"
    )

    Card(
        modifier = modifier
            .fillMaxSize()
            .scale(if (isYourTurn) pulseScale else 1f),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isYourTurn) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surfaceVariant
        ),
        border = if (isYourTurn) BorderStroke(2.dp, MaterialTheme.colorScheme.primary.copy(alpha = borderColor)) else null,
        elevation = CardDefaults.cardElevation(defaultElevation = if (isYourTurn) 8.dp else 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = playerName,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = symbol,
                color = if (symbol == "X") Color.Red else Color.Blue,
                style = MaterialTheme.typography.headlineMedium
            )
            if (isYourTurn) {
                Text(
                    text = "Giliran",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

@Preview(name = "Active", showBackground = true)
@Composable
private fun PlayerCardActivePreview() {
    TicTacToeTheme { PlayerCard(playerName = "Raqhib", symbol = "X", isYourTurn = true) }
}

@Preview(name = "Inactive", showBackground = true)
@Composable
private fun PlayerCardInactivePreview() {
    TicTacToeTheme { PlayerCard(playerName = "Mondo", symbol = "O", isYourTurn = false) }
}
