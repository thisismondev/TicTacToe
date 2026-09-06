package id.co.mondo.tictactoe.ui.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.co.mondo.tictactoe.data.local.model.Cell
import id.co.mondo.tictactoe.data.local.model.WinningLine
import id.co.mondo.tictactoe.ui.theme.TicTacToeTheme

@Composable
fun GameBoard(
    board: List<List<Cell>>,
    winningLine: WinningLine? = null,
    isMyTurn: Boolean = true,
    onCellClick: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val progress by animateFloatAsState(
        targetValue = if (winningLine != null) 1f else 0f,
        animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing),
        label = "winningLineProgress"
    )

    Box(
        modifier = modifier.size(300.dp),
        contentAlignment = Alignment.Center
    ) {
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(9) { index ->
                val row = index / 3
                val col = index % 3
                val cellValue = board[row][col]

                val scale by animateFloatAsState(
                    targetValue = if (cellValue != Cell.EMPTY) 1f else 0.85f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    ),
                    label = "cellScale$index"
                )

                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .border(
                            2.dp,
                            when {
                                isWinningCell(row, col, winningLine) -> MaterialTheme.colorScheme.error
                                else -> Color.Transparent
                            },
                            RoundedCornerShape(12.dp)
                        )
                        .border(1.dp, Color.Black.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                        .clickable(enabled = isMyTurn && cellValue == Cell.EMPTY) {
                            onCellClick(row, col)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = when (cellValue) {
                            Cell.X -> "X"
                            Cell.O -> "O"
                            Cell.EMPTY -> ""
                        },
                        fontSize = 32.sp,
                        color = when (cellValue) {
                            Cell.X -> Color.Red
                            Cell.O -> Color.Blue
                            Cell.EMPTY -> Color.Black
                        },
                        modifier = Modifier.scale(scale)
                    )
                }
            }
        }

        if (winningLine != null && progress > 0f) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val cellW = size.width / 3f
                val cellH = size.height / 3f
                // gap 8.dp approx - adjust for visual, use center
                fun center(col: Int, row: Int) = Offset(
                    col * cellW + cellW / 2,
                    row * cellH + cellH / 2
                )

                val (start, end) = when (winningLine) {
                    WinningLine.ROW_0 -> center(0, 0) to center(2, 0)
                    WinningLine.ROW_1 -> center(0, 1) to center(2, 1)
                    WinningLine.ROW_2 -> center(0, 2) to center(2, 2)
                    WinningLine.COL_0 -> center(0, 0) to center(0, 2)
                    WinningLine.COL_1 -> center(1, 0) to center(1, 2)
                    WinningLine.COL_2 -> center(2, 0) to center(2, 2)
                    WinningLine.DIAG_TL_BR -> center(0, 0) to center(2, 2)
                    WinningLine.DIAG_TR_BL -> center(2, 0) to center(0, 2)
                }

                val currentEnd = Offset(
                    start.x + (end.x - start.x) * progress,
                    start.y + (end.y - start.y) * progress
                )

                drawLine(
                    color = Color.Red,
                    start = start,
                    end = currentEnd,
                    strokeWidth = 8.dp.toPx(),
                    cap = StrokeCap.Round
                )
                // outer error color with alpha for winning effect
                drawLine(
                    color = Color(0xFFE53935),
                    start = start,
                    end = currentEnd,
                    strokeWidth = 12.dp.toPx(),
                    cap = StrokeCap.Round,
                    alpha = 0.3f
                )
            }
        }
    }
}

private fun isWinningCell(row: Int, col: Int, line: WinningLine?): Boolean {
    if (line == null) return false
    return when (line) {
        WinningLine.ROW_0 -> row == 0
        WinningLine.ROW_1 -> row == 1
        WinningLine.ROW_2 -> row == 2
        WinningLine.COL_0 -> col == 0
        WinningLine.COL_1 -> col == 1
        WinningLine.COL_2 -> col == 2
        WinningLine.DIAG_TL_BR -> row == col
        WinningLine.DIAG_TR_BL -> row + col == 2
    }
}

@Preview(name = "Empty", showBackground = true)
@Composable
private fun GameBoardEmptyPreview() {
    TicTacToeTheme {
        GameBoard(
            board = List(3) { List(3) { Cell.EMPTY } },
            winningLine = null,
            onCellClick = { _, _ -> }
        )
    }
}

@Preview(name = "Winning Row", showBackground = true)
@Composable
private fun GameBoardWinningPreview() {
    TicTacToeTheme {
        GameBoard(
            board = listOf(
                listOf(Cell.X, Cell.X, Cell.X),
                listOf(Cell.O, Cell.O, Cell.EMPTY),
                listOf(Cell.EMPTY, Cell.EMPTY, Cell.EMPTY)
            ),
            winningLine = WinningLine.ROW_0,
            onCellClick = { _, _ -> }
        )
    }
}

@Preview(name = "Draw", showBackground = true)
@Composable
private fun GameBoardDrawPreview() {
    TicTacToeTheme {
        GameBoard(
            board = listOf(
                listOf(Cell.X, Cell.O, Cell.X),
                listOf(Cell.X, Cell.O, Cell.O),
                listOf(Cell.O, Cell.X, Cell.O)
            ),
            onCellClick = { _, _ -> }
        )
    }
}
