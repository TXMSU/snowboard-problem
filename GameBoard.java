package game;

public class GameBoard {

    private char[][] board = {
            {' ', 's', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' '},
            {'R', 'L', ' ', ' ', ' '}
    };
    public char getPiece(int row, int col) {
        return board[row][col];
    }
    public void setPiece(int row, int col, char piece) {
        board[row][col] = piece;
    }
    public boolean movePiece(int row, int col, String direction) {
        char piece = board[row][col];
        if (piece != 'L' && piece != 's') {
            return false;
        }
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
        while (true) {
            int nextRow = currentRow + rowChange;
            int nextCol = currentCol + colChange;
            if (nextRow < 0 || nextRow >= getRows() || nextCol < 0 || nextCol >= getCols()) {
                System.out.println("Game over! Snowball flew off the board.");
                return false;
            }
            if (board[nextRow][nextCol] != ' ') {
                break;
            }
            currentRow = nextRow;
            currentCol = nextCol;
        }

        board[currentRow][currentCol] = piece;
        board[row][col] = ' ';
        System.out.println("Ended at row " + currentRow + ", col " + currentCol);
        checkForStacking(currentRow, currentCol);
        return true;
    }

    private void checkForStacking(int row, int col) {
        if (board[row][col] != 's') {
            return;
        }
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
                    System.out.println("Stack created at row " + newRow + ", col " + newCol);
                    return;
                    }
                    if (board[newRow][newCol] == 'S') {
    board[newRow][newCol] = 'C';
    board[row][col] = ' ';
    System.out.println("Snowman completed!");
    return;
}
            }
        }
    }
public boolean placeHead(int row, int col) {
    char head = board[row][col];

    if (head != 'R' && head != 'B') {
        return false;
    }

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
            if (board[newRow][newCol] == 'S') {
                board[newRow][newCol] = head == 'R' ? 'r' : 'b';
                board[row][col] = ' ';
                System.out.println("Snowman completed!");
                return true;
            }
        }
    }

    return false;
}
    public int getRows() {
        return board.length;
    }

    public int getCols() {
        return board[0].length;
    }
}