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
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.co.mondo.tictactoe.data.local.model.Cell
import id.co.mondo.tictactoe.data.local.model.WinningLine
import id.co.mondo.tictactoe.ui.theme.TicTacToeTheme

private val MaxBoardSize = 480.dp

private const val GAP_RATIO = 0.025f
private const val CORNER_RATIO = 0.04f
private const val BORDER_RATIO = 0.02f
private const val GLYPH_RATIO = 0.5f
private const val STROKE_RATIO = 0.03f
private const val GLYPH_MIN_SP = 18f
private const val GLYPH_MAX_SP = 56f

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

    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val side = boardSide(maxWidth, maxHeight)
        val gap = side.scaled(GAP_RATIO)
        val cellSize = (side - gap.scaled(2f)).scaled(1f / 3f)
        val corner = side.scaled(CORNER_RATIO)
        val borderWidth = side.scaled(BORDER_RATIO)
        val glyphSize = (cellSize.value * GLYPH_RATIO).coerceIn(GLYPH_MIN_SP, GLYPH_MAX_SP).sp

        Column(
            modifier = Modifier.size(side),
            verticalArrangement = Arrangement.spacedBy(gap)
        ) {
            for (rowIndex in 0 until 3) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(gap)
                ) {
                    for (colIndex in 0 until 3) {
                        val cellValue = board[rowIndex][colIndex]
                        GameCell(
                            value = cellValue,
                            isWinning = isWinningCell(rowIndex, colIndex, winningLine),
                            isClickable = isMyTurn && cellValue == Cell.EMPTY,
                            cellSize = cellSize,
                            corner = corner,
                            borderWidth = borderWidth,
                            glyphSize = glyphSize,
                            onClick = { onCellClick(rowIndex, colIndex) }
                        )
                    }
                }
            }
        }

        if (winningLine != null && progress > 0f) {
            val stroke = side.scaled(STROKE_RATIO)
            Canvas(modifier = Modifier.size(side)) {
                fun center(col: Int, row: Int): Offset {
                    val half = gap.value + cellSize.value / 2f
                    val stride = cellSize.value + gap.value
                    return Offset(Dp(half + col * stride).toPx(), Dp(half + row * stride).toPx())
                }

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
                    color = Color(0xFFE53935).copy(alpha = 0.3f),
                    start = start,
                    end = currentEnd,
                    strokeWidth = stroke.scaled(1.6f).toPx(),
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = Color.Red,
                    start = start,
                    end = currentEnd,
                    strokeWidth = stroke.toPx(),
                    cap = StrokeCap.Round
                )
            }
        }
    }
}

@Composable
private fun GameCell(
    value: Cell,
    isWinning: Boolean,
    isClickable: Boolean,
    cellSize: Dp,
    corner: Dp,
    borderWidth: Dp,
    glyphSize: TextUnit,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (value != Cell.EMPTY) 1f else 0.85f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "cellScale"
    )

    Box(
        modifier = Modifier
            .size(cellSize)
            .clip(RoundedCornerShape(corner))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(
                borderWidth,
                if (isWinning) MaterialTheme.colorScheme.error else Color.Transparent,
                RoundedCornerShape(corner)
            )
            .border(1.dp, Color.Black.copy(alpha = 0.2f), RoundedCornerShape(corner))
            .clickable(enabled = isClickable, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = when (value) {
                Cell.X -> "X"
                Cell.O -> "O"
                Cell.EMPTY -> ""
            },
            fontSize = glyphSize,
            lineHeight = glyphSize,
            color = when (value) {
                Cell.X -> Color.Red
                Cell.O -> Color.Blue
                Cell.EMPTY -> Color.Black
            },
            modifier = Modifier.scale(scale)
        )
    }
}

private fun boardSide(maxWidth: Dp, maxHeight: Dp): Dp {
    val available = if (maxHeight == Dp.Infinity) maxWidth else minOf(maxWidth, maxHeight)
    return minOf(available, MaxBoardSize)
}

private fun Dp.scaled(factor: Float): Dp = Dp(value * factor)

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
            onCellClick = { _, _ -> },
            modifier = Modifier.size(280.dp)
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
            onCellClick = { _, _ -> },
            modifier = Modifier.size(280.dp)
        )
    }
}

@Preview(name = "Winning Diagonal", showBackground = true)
@Composable
private fun GameBoardWinningDiagonalPreview() {
    TicTacToeTheme {
        GameBoard(
            board = listOf(
                listOf(Cell.O, Cell.X, Cell.EMPTY),
                listOf(Cell.X, Cell.O, Cell.EMPTY),
                listOf(Cell.X, Cell.O, Cell.X)
            ),
            winningLine = WinningLine.DIAG_TL_BR,
            onCellClick = { _, _ -> },
            modifier = Modifier.size(280.dp)
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
            onCellClick = { _, _ -> },
            modifier = Modifier.size(280.dp)
        )
    }
}

@Preview(name = "Tiny - Fits Width", widthDp = 280, heightDp = 400, showBackground = true)
@Composable
private fun GameBoardTinyPreview() {
    TicTacToeTheme {
        GameBoard(
            board = listOf(
                listOf(Cell.X, Cell.EMPTY, Cell.O),
                listOf(Cell.EMPTY, Cell.O, Cell.EMPTY),
                listOf(Cell.O, Cell.EMPTY, Cell.X)
            ),
            winningLine = null,
            onCellClick = { _, _ -> },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(name = "Large - Square Of Bounded Space", widthDp = 700, heightDp = 340, showBackground = true)
@Composable
private fun GameBoardLargePreview() {
    TicTacToeTheme {
        GameBoard(
            board = listOf(
                listOf(Cell.X, Cell.EMPTY, Cell.O),
                listOf(Cell.EMPTY, Cell.O, Cell.EMPTY),
                listOf(Cell.O, Cell.EMPTY, Cell.X)
            ),
            winningLine = WinningLine.COL_1,
            onCellClick = { _, _ -> },
            modifier = Modifier.size(600.dp, 300.dp)
        )
    }
}