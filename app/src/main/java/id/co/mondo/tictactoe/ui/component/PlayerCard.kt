package id.co.mondo.tictactoe.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import id.co.mondo.tictactoe.ui.theme.ComponentStyles
import id.co.mondo.tictactoe.ui.theme.TicTacToeTheme
import id.co.mondo.tictactoe.ui.theme.playerOColor
import id.co.mondo.tictactoe.ui.theme.playerXColor

@Composable
fun PlayerCard(
    playerName: String,
    symbol: String,
    isYourTurn: Boolean,
    modifier: Modifier = Modifier
) {
    val containerColor by animateColorAsState(
        targetValue = if (isYourTurn) MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.surfaceVariant,
        animationSpec = tween(300),
        label = "containerColor"
    )

    val elevation by animateDpAsState(
        targetValue = if (isYourTurn) 6.dp else 1.dp,
        animationSpec = tween(300),
        label = "elevation"
    )

    val symbolColor = if (symbol == "X") MaterialTheme.colorScheme.playerXColor
    else MaterialTheme.colorScheme.playerOColor

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = ComponentStyles.buttonShape,
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = if (isYourTurn) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null,
        elevation = CardDefaults.cardElevation(defaultElevation = elevation)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = playerName,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1
            )
            Text(
                text = symbol,
                color = symbolColor,
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold
            )
            if (isYourTurn) {
                Text(
                    text = "Giliran",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold
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
