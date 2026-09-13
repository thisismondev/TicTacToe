package id.co.mondo.tictactoe.data.local.model

enum class Winner {
    X, O, DRAW
}

enum class Cell {
    EMPTY, X, O
}

enum class WinningLine {
    ROW_0, ROW_1, ROW_2,
    COL_0, COL_1, COL_2,
    DIAG_TL_BR, DIAG_TR_BL
}

data class GameRoom(
    val roomId: String,
    val playerX: String,
    val playerO: String,
    val winAsX: Int = 0,
    val winAsO: Int = 0,
    val drawCount: Int = 0
)


data class GamePlay(
    val roomId: String,
    val board: List<List<Cell>> = List(3) { List(3) { Cell.EMPTY } },
    val turn: Cell = Cell.X,
    val winner: Winner? = null,
    val winningLine: WinningLine? = null
)


//data class TopPlayer(
//    val rank: Int,
//    val name: String,
//    val wins: Int,
//    val losses: Int,
//    val draws: Int
//) {
//    val totalGames: Int get() = wins + losses + draws
//    val winRate: Float get() = if (totalGames == 0) 0f else wins.toFloat() / totalGames
//}




