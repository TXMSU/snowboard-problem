package game;

public abstract class Piece {
    protected int row;
    protected int col;

    public Piece(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setPosition(int r, int c) {
        this.row = r;
        this.col = c;
    }
}