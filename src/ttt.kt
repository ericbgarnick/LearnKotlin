enum class Suit(private val suit: String) {
    Z(" "),
    X("X"),
    O("O"),
    ;

    override fun toString (): String {
        return suit
    }
}


class Board {
    val rowLen = 3
    val numRows = 3
    private var contents: Array<Suit> = Array(rowLen * numRows) { Suit.Z }

    fun isFull(): Boolean {
        return Suit.Z !in contents
    }

    fun setPosition(row: Int, col: Int, suit: Suit) {
        val position = row * rowLen + col
        contents[position] = suit
    }

    fun getPosition(row: Int, col: Int): Suit {
        val position = row * rowLen + col
        return contents[position]
    }

    fun getRow(rowNum: Int): Array<Suit> {
        return Array(rowLen) { i -> contents[i + rowNum * numRows] }
    }

    fun getCol(colNum: Int): Array<Suit> {
        return Array(numRows) { i -> contents[i * rowLen + colNum] }
    }

    fun descDiag(): Array<Suit> {
        return Array(numRows) { i -> contents[i * rowLen + i] }
    }

    fun ascDiag(): Array<Suit> {
        return Array(numRows) { i -> contents[i * rowLen + rowLen - 1 - i] }
    }

    override fun toString(): String {
        val rows: Array<String> = Array(numRows) { "" }
        for (rowNum in 0 until numRows) {
            rows[rowNum] = getRow(rowNum).joinToString( " | ", prefix = " ", postfix = " " )
        }
        return rows.joinToString("\n" + ("-".repeat(rows[0].length)) + "\n")
    }
}


class TicTacToe {
    companion object {
        const val rowKey = "tmb"
        const val colKey = "lmr"
    }
    private val board: Board = Board()
    private var winner: Suit = Suit.Z

    fun run() {
        announceStart()
        var curPlayer = Suit.Z
        while (winner == Suit.Z && !board.isFull()) {
            curPlayer = getPlayer(curPlayer)
            updateBoard(getMove(curPlayer), curPlayer)
            println(board.toString())
            winner = checkWinner()
        }
        announceResult(winner)
    }

    private fun announceStart() {
        println("Let's play tic-tac-toe!")
        println(board.toString())
    }

    private fun getPlayer(curPlayer: Suit): Suit {
        return if (curPlayer == Suit.O) Suit.X else Suit.O
    }

    private fun getMove(curPlayer: Suit): String {
        var userInput: String? = null
        while (invalidInput(userInput)) {
            print("Player ${curPlayer}, enter your move: ")
            userInput = readLine()
        }
        return userInput!!
    }

    private fun invalidInput(userInput: String?): Boolean {
        var result = true
        if (userInput != null &&
                userInput.length == 2 &&
                userInput[0] in rowKey &&
                userInput[1] in colKey) {
            val row = rowKey.indexOf(userInput[0])
            val col = colKey.indexOf(userInput[1])
            result = board.getPosition(row, col) != Suit.Z
        }
        return result
    }

    private fun updateBoard(position: String, curPlayer: Suit) {
        val row = rowKey.indexOf(position[0])
        val col = colKey.indexOf(position[1])
        board.setPosition(row, col, curPlayer)
    }

    private fun checkWinner(): Suit {
        var winner = checkRows()
        if (winner == Suit.Z) {
            winner = checkCols()
        }
        if (winner == Suit.Z) {
            winner = checkDiag()
        }
        return winner
    }

    private fun checkRows(): Suit {
        var winner = Suit.Z
        for (rowNum in 0 until board.numRows) {
            val thisRow = board.getRow(rowNum)
            if (thisRow.distinct().size == 1) {
                winner = thisRow[0]
            }
            if (winner != Suit.Z) break
        }
        return winner
    }

    private fun checkCols(): Suit {
        var winner = Suit.Z
        for (colNum in 0 until board.rowLen) {
            val thisCol = board.getCol(colNum)
            if (thisCol.distinct().size == 1) {
                winner = thisCol[0]
            }
            if (winner != Suit.Z) break
        }
        return winner
    }

    private fun checkDiag(): Suit {
        var winner = Suit.Z
        val descDiag = board.descDiag()
        val ascDiag = board.ascDiag()
        if (descDiag.distinct().size == 1) {
            winner = descDiag[0]
        } else if (ascDiag.distinct().size == 1) {
            winner = ascDiag[0]
        }
        return winner
    }

    private fun announceResult(winner: Suit) {
        if (winner == Suit.Z) {
            println("Tie game!")
        } else {
            println("Winner is $winner!")
        }
    }
}


fun main() {
    val ttt = TicTacToe()
    ttt.run()
}
