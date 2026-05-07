package game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Main {
    private static GameBoard gameBoard = new GameBoard();
    private static int selectedRow = -1;
    private static int selectedCol = -1;
    public static void main(String[] args) {
        JFrame window = new JFrame("Snow Problem");
        JLabel title = new JLabel("Snow Problem", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(gameBoard.getRows(), gameBoard.getCols()));
        ImageIcon holeIcon = resizeIcon("resources/hole.png", 80, 80);
        ImageIcon treeIcon = resizeIcon("resources/tree.png", 80, 80);
        ImageIcon largeIcon = resizeIcon("resources/snowball_large.png", 80, 80);
        ImageIcon smallIcon = resizeIcon("resources/snowball_small.png", 80, 80);
        ImageIcon stackIcon = resizeIcon("resources/snowman_stack.png", 80, 80);
        ImageIcon headRedIcon = resizeIcon("resources/head_red.png", 80, 80);
        ImageIcon snowmanRedIcon = resizeIcon("resources/snowman_red.png", 80, 80);
        ImageIcon snowmanBlueIcon = resizeIcon("resources/snowman_blue.png", 80, 80);
        ImageIcon headBlueIcon = resizeIcon("resources/head_blue.png", 80, 80);

        for (int i = 0; i < gameBoard.getRows() * gameBoard.getCols(); i++) {
            JLabel square = new JLabel();
            square.setOpaque(true);
            square.setBackground(Color.WHITE);
            square.setHorizontalAlignment(SwingConstants.CENTER);
            square.setBorder(javax.swing.BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            int row = i / gameBoard.getCols();
            int col = i % gameBoard.getCols();
            char piece = gameBoard.getPiece(row, col);
            square.setIcon(holeIcon);
           if (piece == 'T') {
    square.setIcon(treeIcon);
} else if (piece == 'L') {
    square.setIcon(largeIcon);
} else if (piece == 's') {
    square.setIcon(smallIcon);
} else if (piece == 'S') {
    square.setIcon(stackIcon);
} else if (piece == 'R') {
    square.setIcon(headRedIcon);
} else if (piece == 'B') {
    square.setIcon(headBlueIcon);
} else if (piece == 'r') {
    square.setIcon(snowmanRedIcon);
} else if (piece == 'b') {
    square.setIcon(snowmanBlueIcon);
}
final int clickedRow = row;
final int clickedCol = col;

square.addMouseListener(new java.awt.event.MouseAdapter() {
    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {
       char clickedPiece = gameBoard.getPiece(clickedRow, clickedCol);

if (selectedRow == -1) {
    if (clickedPiece == 'L' || clickedPiece == 's' || clickedPiece == 'R' || clickedPiece == 'B') {
        selectedRow = clickedRow;
        selectedCol = clickedCol;

        System.out.println("Selected snowball at row " + selectedRow + ", col " + selectedCol);
    } else {
        System.out.println("Select a snowball first.");
    }
} else {
   if (clickedRow == selectedRow && clickedCol > selectedCol) {
    boolean moved = gameBoard.movePiece(selectedRow, selectedCol, "RIGHT");
    if (moved) {
        window.dispose();
        main(null);
    }
char selectedPiece = gameBoard.getPiece(selectedRow, selectedCol);

if (selectedPiece == 'R' || selectedPiece == 'B') {
    boolean completed = gameBoard.placeHead(selectedRow, selectedCol);

    if (completed) {
        window.dispose();
        main(null);
    } else {
        System.out.println("Head is not next to a stack.");
    }
    selectedRow = -1;
    selectedCol = -1;
    return;
}
} else if (clickedRow == selectedRow && clickedCol < selectedCol) {
    boolean moved = gameBoard.movePiece(selectedRow, selectedCol, "LEFT");
    if (moved) {
        window.dispose();
        main(null);
    }
} else if (clickedCol == selectedCol && clickedRow < selectedRow) {
    boolean moved = gameBoard.movePiece(selectedRow, selectedCol, "UP");
    if (moved) {
        window.dispose();
        main(null);
    }
} else if (clickedCol == selectedCol && clickedRow > selectedRow) {
    boolean moved = gameBoard.movePiece(selectedRow, selectedCol, "DOWN");
    if (moved) {
        window.dispose();
        main(null);
    }
} else {
    System.out.println("Invalid direction.");
}

    selectedRow = -1;
    selectedCol = -1;
}
    }
});
  boardPanel.add(square);
        }
        window.setLayout(new BorderLayout());
        window.add(title, BorderLayout.NORTH);
        window.add(boardPanel, BorderLayout.CENTER);
        window.setSize(600, 650);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
    private static ImageIcon resizeIcon(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(path);
        Image image = icon.getImage();
        Image resized = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resized);
    }
}