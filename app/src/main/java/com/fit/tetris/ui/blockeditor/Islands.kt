package com.fit.tetris.ui.blockeditor

class Islands {
    // A function to check if a given cell (row, col) can
    // be included in DFS
    private fun isSafe(
        M: Array<BooleanArray>, row: Int, col: Int,
        visited: Array<BooleanArray>
    ): Boolean {
        // row number is in range, column number is in range
        // and value is 1 and not yet visited
        return (row in 0 until ROW && col >= 0
                && col < COL
                && M[row][col] && !visited[row][col])
    }

    // A utility function to do DFS for a 2D boolean matrix.
    // It only considers the 8 neighbors as adjacent
    // vertices
    private fun DFS(
        M: Array<BooleanArray>, row: Int, col: Int,
        visited: Array<BooleanArray>
    ) {
        // These arrays are used to get row and column
        // numbers of 8 neighbors of a given cell
        val rowNbr = intArrayOf(-1, -1, -1, 0, 0, 1, 1, 1)
        val colNbr = intArrayOf(-1, 0, 1, -1, 1, -1, 0, 1)

        // Mark this cell as visited
        visited[row][col] = true

        // Recur for all connected neighbours
        for (k in 0..7) if (isSafe(
                M, row + rowNbr[k], col + colNbr[k],
                visited
            )
        ) DFS(
            M, row + rowNbr[k], col + colNbr[k],
            visited
        )
    }

    // The main function that returns count of islands in a
    // given boolean 2D matrix
    fun countIslands(M: Array<BooleanArray>): Int {
        // Make a bool array to mark visited cells.
        // Initially all cells are unvisited
        val visited = Array(ROW) { BooleanArray(COL) }

        // Initialize count as 0 and traverse through the
        // all cells of given matrix
        var count = 0
        for (i in 0 until ROW) for (j in 0 until COL) if (M[i][j]
            && !visited[i][j]
        ) // If a cell with
        { // value 1 is not
            // visited yet, then new island found,
            // Visit all cells in this island and
            // increment island count
            DFS(M, i, j, visited)
            ++count
        }
        return count
    }

    companion object {
        // No of rows and columns
        const val ROW = 4
        const val COL = 4
    }
}