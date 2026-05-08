Snow Problem

How to compile:

javac -d bin src/game/Main.java src/game/GameBoard.java src/game/Piece.java src/game/HighScores.java src/game/Levels.java

How to run:

java -cp bin Main

Files:

- src/game/Main.java starts the Swing window and handles mouse clicks.
- src/game/GameBoard.java stores the board and game rules.
- src/game/Levels.java loads the level layouts.
- src/game/levels.txt stores the 80 level layouts.
- src/game/HighScores.java saves best scores.
- resources contains the images used by the game.

Controls:

Click a snowball or head, then click where you want it to move or be placed.
Use Previous Level and Next Level to change levels.
Use Reset to restart the current level.
Use High Scores to see saved best scores.
