import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Levels {
    private static ArrayList<char[][]> levels;

    public static int getLevelCount() {
        loadLevels();
        return levels.size();
    }

    public static char[][] getLevel(int levelNumber) {
        loadLevels();

        if (levelNumber < 1) {
            levelNumber = 1;
        } else if (levelNumber > levels.size()) {
            levelNumber = levels.size();
        }

        return copyBoard(levels.get(levelNumber - 1));
    }

    private static void loadLevels() {
        if (levels != null) {
            return;
        }

        // Load the levels from the text file.
        levels = new ArrayList<char[][]>();
        File file = findLevelsFile();

        if (file != null && file.exists()) {
            readLevelsFromFile(file);
        }

        if (levels.size() == 0) {
            addBackupLevel();
        }
    }

    private static void readLevelsFromFile(File file) {
        try {
            Scanner scanner = new Scanner(file);
            ArrayList<String> rows = new ArrayList<String>();

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (line.startsWith("#") || line.startsWith("Level")) {
                    continue;
                }

                // A blank line means the current level is finished.
                if (line.trim().equals("")) {
                    addLevel(rows);
                    rows.clear();
                } else {
                    rows.add(line);
                }
            }

            addLevel(rows);
            scanner.close();
        } catch (Exception e) {
            System.out.println("Could not load levels.txt.");
        }
    }

    private static File findLevelsFile() {
        // Check a few places for the levels file.
        String[] paths = {
                "src/game/levels.txt",
                "game/levels.txt",
                "levels.txt",
                "resources/levels.txt",
                "../resources/levels.txt",
                "../../resources/levels.txt"
        };

        for (int i = 0; i < paths.length; i++) {
            File file = new File(paths[i]);
            if (file.exists()) {
                return file;
            }
        }

        return null;
    }

    private static void addLevel(ArrayList<String> rows) {
        if (rows.size() != 4) {
            return;
        }

        char[][] board = new char[4][5];

        for (int row = 0; row < 4; row++) {
            String line = rows.get(row);
            if (line.length() < 5) {
                return;
            }

            for (int col = 0; col < 5; col++) {
                char letter = line.charAt(col);
                if (letter == '.') {
                    letter = ' ';
                }
                board[row][col] = letter;
            }
        }

        levels.add(board);
    }

    private static void addBackupLevel() {
        char[][] board = {
                {'R', ' ', 'T', ' ', 's'},
                {'L', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', 's', ' '},
                {'Y', 'L', 'T', ' ', ' '}
        };
        levels.add(board);
    }

    private static char[][] copyBoard(char[][] oldBoard) {
        char[][] result = new char[oldBoard.length][oldBoard[0].length];

        for (int row = 0; row < oldBoard.length; row++) {
            for (int col = 0; col < oldBoard[row].length; col++) {
                result[row][col] = oldBoard[row][col];
            }
        }

        return result;
    }
}
