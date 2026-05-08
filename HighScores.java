import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class HighScores {
    private static final String FILE_NAME = "high_scores.txt";

    public int getBestScore(int level) {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return -1;
        }

        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");

                if (parts.length == 2 && parts[0].equals("level" + level)) {
                    scanner.close();
                    return Integer.parseInt(parts[1]);
                }
            }
            scanner.close();
        } catch (Exception e) {
            return -1;
        }

        return -1;
    }

    public boolean saveScore(int level, int moves) {
        int oldScore = getBestScore(level);

        // Save only if it is a better score.
        if (oldScore != -1 && oldScore <= moves) {
            return false;
        }

        String allScores = "";
        boolean replaced = false;
        File file = new File(FILE_NAME);

        try {
            if (file.exists()) {
                Scanner scanner = new Scanner(file);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (line.startsWith("level" + level + ",")) {
                        allScores = allScores + "level" + level + "," + moves + "\n";
                        replaced = true;
                    } else {
                        allScores = allScores + line + "\n";
                    }
                }
                scanner.close();
            }

            if (!replaced) {
                allScores = allScores + "level" + level + "," + moves + "\n";
            }

            FileWriter writer = new FileWriter(file);
            writer.write(allScores);
            writer.close();
        } catch (IOException e) {
            return false;
        }

        return true;
    }
}
