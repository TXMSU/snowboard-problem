import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Main {
    private static final Color SNOW = new Color(244, 249, 252);
    private static final Color ICE = new Color(222, 239, 248);
    private static final Color SELECTED = new Color(181, 216, 242);

    private static GameBoard gameBoard = new GameBoard();
    private static HighScores highScores = new HighScores();

    private static JLabel[][] squares;
    private static JLabel messageLabel;
    private static JLabel moveLabel;
    private static JLabel highScoreLabel;
    private static JLabel levelLabel;

    private static int selectedRow = -1;
    private static int selectedCol = -1;
    private static int level = 1;

    private static ImageIcon holeIcon;
    private static ImageIcon treeIcon;
    private static ImageIcon largeIcon;
    private static ImageIcon smallIcon;
    private static ImageIcon stackIcon;
    private static ImageIcon headRedIcon;
    private static ImageIcon headBlueIcon;
    private static ImageIcon headYellowIcon;
    private static ImageIcon snowmanRedIcon;
    private static ImageIcon snowmanBlueIcon;
    private static ImageIcon snowmanYellowIcon;

    public static void main(String[] args) {
        // Load level 1 when the game starts.
        gameBoard.setStartingBoard(Levels.getLevel(level));

        JFrame window = new JFrame("Snow Problem");

        JLabel title = new JLabel("Snow Problem", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));

        messageLabel = new JLabel("Click a snowball or head to start.", SwingConstants.CENTER);
        moveLabel = new JLabel("Moves: 0");
        highScoreLabel = new JLabel("Best: none");
        levelLabel = new JLabel("Level 1 of " + Levels.getLevelCount());

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> {
            resetCurrentLevel("Level reset.");
        });

        JButton scoresButton = new JButton("High Scores");
        scoresButton.addActionListener(e -> showHighScores());

        JButton previousButton = new JButton("Previous Level");
        previousButton.addActionListener(e -> changeLevel(level - 1));

        JButton nextButton = new JButton("Next Level");
        nextButton.addActionListener(e -> changeLevel(level + 1));

        JPanel topPanel = new JPanel(new GridLayout(4, 1));
        topPanel.setBackground(SNOW);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 8, 10));

        JPanel statusPanel = new JPanel();
        statusPanel.setBackground(SNOW);
        statusPanel.add(levelLabel);
        statusPanel.add(moveLabel);
        statusPanel.add(highScoreLabel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(SNOW);
        buttonPanel.add(previousButton);
        buttonPanel.add(nextButton);
        buttonPanel.add(scoresButton);
        buttonPanel.add(resetButton);

        topPanel.add(title);
        topPanel.add(messageLabel);
        topPanel.add(statusPanel);
        topPanel.add(buttonPanel);

        loadImages();

        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(gameBoard.getRows(), gameBoard.getCols()));
        boardPanel.setBackground(SNOW);
        boardPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        squares = new JLabel[gameBoard.getRows()][gameBoard.getCols()];

        for (int i = 0; i < gameBoard.getRows() * gameBoard.getCols(); i++) {
            // Makes one square for the board.
            JLabel square = new JLabel();
            square.setOpaque(true);
            square.setBackground(ICE);
            square.setHorizontalAlignment(SwingConstants.CENTER);
            square.setPreferredSize(new Dimension(95, 95));
            square.setBorder(BorderFactory.createLineBorder(new Color(175, 205, 220)));

            int row = i / gameBoard.getCols();
            int col = i % gameBoard.getCols();
            final int clickedRow = row;
            final int clickedCol = col;

            square.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    squareClicked(clickedRow, clickedCol);
                }
            });

            squares[row][col] = square;
            boardPanel.add(square);
        }

        window.setLayout(new BorderLayout());
        window.getContentPane().setBackground(SNOW);
        window.add(topPanel, BorderLayout.NORTH);
        window.add(boardPanel, BorderLayout.CENTER);
        updateBoard();

        window.setSize(650, 700);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    private static void squareClicked(int row, int col) {
        if (gameBoard.isGameOver() || gameBoard.isWon()) {
            return;
        }

        char clickedPiece = gameBoard.getPiece(row, col);

        // First click selects the piece.
        if (selectedRow == -1) {
            if (gameBoard.canSelect(row, col)) {
                selectedRow = row;
                selectedCol = col;
                messageLabel.setText("Selected row " + row + ", column " + col + ".");
            } else {
                messageLabel.setText("Select a snowball or head first.");
            }
            updateBoard();
            return;
        }

        if (row == selectedRow && col == selectedCol) {
            selectedRow = -1;
            selectedCol = -1;
            messageLabel.setText("Selection cleared.");
            updateBoard();
            return;
        }

        char selectedPiece = gameBoard.getPiece(selectedRow, selectedCol);
        boolean changed = false;

        // Heads only go on nearby stacks.
        if (selectedPiece == 'R' || selectedPiece == 'B' || selectedPiece == 'Y') {
            changed = gameBoard.placeHead(selectedRow, selectedCol, row, col);
            if (!changed) {
                messageLabel.setText("Heads can only go on an adjacent stack.");
            }
        } else if (selectedPiece == 's' && clickedPiece == 'L') {
            changed = gameBoard.stackSnowballs(selectedRow, selectedCol, row, col);
            if (!changed) {
                messageLabel.setText("Small snowballs only stack on a large snowball next to it.");
            }
        } else if (row == selectedRow || col == selectedCol) {
            String direction = getDirection(row, col);
            changed = gameBoard.movePiece(selectedRow, selectedCol, direction);
            if (!changed) {
                messageLabel.setText("That snowball cannot move that way.");
            }
        } else {
            messageLabel.setText("Click in a straight line from the piece.");
        }

        selectedRow = -1;
        selectedCol = -1;

        if (changed) {
            gameBoard.addMove();
            if (gameBoard.isGameOver()) {
                messageLabel.setText("Game over! A snowball left the board.");
            } else if (gameBoard.isWon()) {
                boolean newBest = highScores.saveScore(level, gameBoard.getMoves());
                if (newBest) {
                    messageLabel.setText("You won in " + gameBoard.getMoves() + " moves. New best score!");
                } else {
                    messageLabel.setText("You won in " + gameBoard.getMoves() + " moves!");
                }
            } else if (clickedPiece == 'S') {
                messageLabel.setText("Head added to the snowman.");
            } else if (selectedPiece == 's' && clickedPiece == 'L') {
                messageLabel.setText("Snowballs stacked.");
            } else {
                messageLabel.setText("Move made.");
            }
        }

        updateBoard();
    }

    private static String getDirection(int row, int col) {
        if (row < selectedRow) {
            return "UP";
        } else if (row > selectedRow) {
            return "DOWN";
        } else if (col < selectedCol) {
            return "LEFT";
        } else {
            return "RIGHT";
        }
    }

    private static void updateBoard() {
        // Refreshes the board after a move.
        for (int row = 0; row < gameBoard.getRows(); row++) {
            for (int col = 0; col < gameBoard.getCols(); col++) {
                JLabel square = squares[row][col];
                square.setIcon(getIcon(gameBoard.getPiece(row, col)));

                if (row == selectedRow && col == selectedCol) {
                    square.setBackground(SELECTED);
                } else {
                    square.setBackground(ICE);
                }
            }
        }

        moveLabel.setText("Moves: " + gameBoard.getMoves());
        levelLabel.setText("Level " + level + " of " + Levels.getLevelCount());
        int bestScore = highScores.getBestScore(level);
        if (bestScore == -1) {
            highScoreLabel.setText("Best: none");
        } else {
            highScoreLabel.setText("Best: " + bestScore);
        }
    }

    private static void resetCurrentLevel(String message) {
        gameBoard.reset();
        selectedRow = -1;
        selectedCol = -1;
        updateBoard();
        messageLabel.setText(message);
    }

    private static void changeLevel(int newLevel) {
        if (newLevel < 1) {
            newLevel = Levels.getLevelCount();
        } else if (newLevel > Levels.getLevelCount()) {
            newLevel = 1;
        }

        level = newLevel;
        gameBoard.setStartingBoard(Levels.getLevel(level));
        resetCurrentLevel("Loaded level " + level + ".");
    }

    private static void showHighScores() {
        String text = "";

        for (int i = 1; i <= Levels.getLevelCount(); i++) {
            int score = highScores.getBestScore(i);
            if (score == -1) {
                text = text + "Level " + i + ": none\n";
            } else {
                text = text + "Level " + i + ": " + score + " moves\n";
            }
        }

        JOptionPane.showMessageDialog(null, text, "High Scores", JOptionPane.INFORMATION_MESSAGE);
    }

    private static ImageIcon getIcon(char piece) {
        if (piece == 'T') {
            return treeIcon;
        } else if (piece == 'L') {
            return largeIcon;
        } else if (piece == 's') {
            return smallIcon;
        } else if (piece == 'S') {
            return stackIcon;
        } else if (piece == 'R') {
            return headRedIcon;
        } else if (piece == 'B') {
            return headBlueIcon;
        } else if (piece == 'Y') {
            return headYellowIcon;
        } else if (piece == 'r') {
            return snowmanRedIcon;
        } else if (piece == 'b') {
            return snowmanBlueIcon;
        } else if (piece == 'y') {
            return snowmanYellowIcon;
        } else {
            return holeIcon;
        }
    }

    private static void loadImages() {
        holeIcon = resizeIcon("hole.png", 80, 80);
        treeIcon = resizeIcon("tree.png", 80, 80);
        largeIcon = resizeIcon("snowball_large.png", 80, 80);
        smallIcon = resizeIcon("snowball_small.png", 80, 80);
        stackIcon = resizeIcon("snowman_stack.png", 80, 80);
        headRedIcon = resizeIcon("head_red.png", 80, 80);
        headBlueIcon = resizeIcon("head_blue.png", 80, 80);
        headYellowIcon = resizeIcon("head_yellow.png", 80, 80);
        snowmanRedIcon = resizeIcon("snowman_red.png", 80, 80);
        snowmanBlueIcon = resizeIcon("snowman_blue.png", 80, 80);
        snowmanYellowIcon = resizeIcon("snowman_yellow.png", 80, 80);
    }

    private static ImageIcon resizeIcon(String fileName, int width, int height) {
        ImageIcon icon = loadIcon(fileName);
        if (icon.getIconWidth() == -1) {
            Image blank = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            return new ImageIcon(blank);
        }

        Image image = icon.getImage();
        Image resized = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resized);
    }

    private static ImageIcon loadIcon(String fileName) {
        File imageFile = findImageFile(fileName);
        if (imageFile != null) {
            return new ImageIcon(imageFile.getAbsolutePath());
        }

        URL resource = Main.class.getResource("/resources/" + fileName);
        if (resource == null) {
            resource = Main.class.getResource("/" + fileName);
        }

        if (resource != null) {
            return new ImageIcon(resource);
        }

        System.out.println("Could not find image: " + fileName);
        return new ImageIcon();
    }

    private static File findImageFile(String fileName) {
        File folder = new File(System.getProperty("user.dir"));

        for (int i = 0; i < 5 && folder != null; i++) {
            File file = new File(folder, "resources/" + fileName);
            if (file.exists()) {
                return file;
            }

            file = new File(folder, fileName);
            if (file.exists()) {
                return file;
            }

            File[] children = folder.listFiles();
            if (children != null) {
                for (int j = 0; j < children.length; j++) {
                    file = new File(children[j], "resources/" + fileName);
                    if (file.exists()) {
                        return file;
                    }
                }
            }

            folder = folder.getParentFile();
        }

        return null;
    }
}