public class GameBoard {

    private char[][] startingBoard = {
            {'R', ' ', 'T', ' ', 's'},
            {'L', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', 's', ' '},
            {'Y', 'L', 'T', ' ', ' '}
    };

    private char[][] board;
    private int moves;
    private boolean gameOver;

    public GameBoard() {
        reset();
    }

    public GameBoard(char[][] newBoard) {
        board = copyBoard(newBoard);
        moves = 0;
        gameOver = false;
    }

    public void reset() {
        board = copyBoard(startingBoard);
        moves = 0;
        gameOver = false;
    }

    public void setStartingBoard(char[][] newStartingBoard) {
        startingBoard = copyBoard(newStartingBoard);
        reset();
    }

    private char[][] copyBoard(char[][] boardToCopy) {
        char[][] result = new char[boardToCopy.length][boardToCopy[0].length];
        for (int row = 0; row < boardToCopy.length; row++) {
            for (int col = 0; col < boardToCopy[row].length; col++) {
                result[row][col] = boardToCopy[row][col];
            }
        }
        return result;
    }

    public GameBoard copy() {
        GameBoard copied = new GameBoard(board);
        copied.moves = moves;
        copied.gameOver = gameOver;
        return copied;
    }

    public char[][] getBoardCopy() {
        return copyBoard(board);
    }

    public char getPiece(int row, int col) {
        return board[row][col];
    }

    public void setPiece(int row, int col, char piece) {
        board[row][col] = piece;
    }

    public boolean canSelect(int row, int col) {
        char piece = board[row][col];
        return piece == 'L' || piece == 's' || piece == 'R' || piece == 'B' || piece == 'Y';
    }

    public boolean movePiece(int row, int col, String direction) {
        char piece = board[row][col];
        if (piece != 'L' && piece != 's') {
            return false;
        }

        // Work out which way to move.
        int rowChange = 0;
        int colChange = 0;

        if (direction.equals("UP")) {
            rowChange = -1;
        } else if (direction.equals("DOWN")) {
            rowChange = 1;
        } else if (direction.equals("LEFT")) {
            colChange = -1;
        } else if (direction.equals("RIGHT")) {
            colChange = 1;
        }

        int currentRow = row;
        int currentCol = col;

        // Snowballs keep sliding until they hit something.
        while (true) {
            int nextRow = currentRow + rowChange;
            int nextCol = currentCol + colChange;

            if (nextRow < 0 || nextRow >= getRows() || nextCol < 0 || nextCol >= getCols()) {
                board[row][col] = ' ';
                gameOver = true;
                return true;
            }

            if (board[nextRow][nextCol] != ' ') {
                break;
            }

            currentRow = nextRow;
            currentCol = nextCol;
        }

        boolean moved = currentRow != row || currentCol != col;
        if (moved) {
            board[currentRow][currentCol] = piece;
            board[row][col] = ' ';
        }

        boolean stacked = checkForStacking(currentRow, currentCol);
        return moved || stacked;
    }

    private boolean checkForStacking(int row, int col) {
        if (board[row][col] != 's') {
            return false;
        }

        // Small snowball plus large snowball makes a stack.
        int[][] directions = {
                {-1, 0},
                {1, 0},
                {0, -1},
                {0, 1}
        };

        for (int i = 0; i < directions.length; i++) {
            int newRow = row + directions[i][0];
            int newCol = col + directions[i][1];
            if (newRow >= 0 && newRow < getRows() && newCol >= 0 && newCol < getCols()) {
                if (board[newRow][newCol] == 'L') {
                    board[newRow][newCol] = 'S';
                    board[row][col] = ' ';
                    return true;
                }
            }
        }

        return false;
    }

    public boolean placeHead(int headRow, int headCol, int targetRow, int targetCol) {
        char head = board[headRow][headCol];

        if (head != 'R' && head != 'B' && head != 'Y') {
            return false;
        }

        if (board[targetRow][targetCol] != 'S') {
            return false;
        }

        int rowDistance = Math.abs(headRow - targetRow);
        int colDistance = Math.abs(headCol - targetCol);
        boolean isNextToStack = rowDistance + colDistance == 1;

        if (!isNextToStack) {
            return false;
        }

        if (head == 'R') {
            board[targetRow][targetCol] = 'r';
        } else if (head == 'B') {
            board[targetRow][targetCol] = 'b';
        } else {
            board[targetRow][targetCol] = 'y';
        }

        board[headRow][headCol] = ' ';
        return true;
    }

    public boolean isWon() {
        // If no loose pieces are left, the level is won.
        for (int row = 0; row < getRows(); row++) {
            for (int col = 0; col < getCols(); col++) {
                char piece = board[row][col];
                if (piece == 'L' || piece == 's' || piece == 'S'
                        || piece == 'R' || piece == 'B' || piece == 'Y') {
                    return false;
                }
            }
        }

        return true;
    }

    public void addMove() {
        moves++;
    }

    public void setMoves(int newMoves) {
        moves = newMoves;
    }

    public int getMoves() {
        return moves;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public int getRows() {
        return board.length;
    }

    public int getCols() {
        return board[0].length;
    }

    public String boardKey() {
        String key = "";
        for (int row = 0; row < getRows(); row++) {
            for (int col = 0; col < getCols(); col++) {
                key = key + board[row][col];
            }
        }
        return key;
    }
}
