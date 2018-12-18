package pkg8queens;

import java.util.Random;

/**
 *
 * @author Logan Willett the Spaghetti Code King
 */
public class Main {
    
    public static void main(String[] args) {
        //Board
        int grid[][] = new int[8][8];
        //Assigns random values
        restart(grid);
   
        int stateCounter = 0;
        int restartCounter = 0;
        int previousScore = totalConflicts(grid);
        int lowestScore = previousScore;
        //Loop until solution found
        do {
            lowestScore = hillClimber(grid);
            //Increment applicable counter
            if (previousScore == lowestScore)
                restartCounter++;
            else
                stateCounter++;
            //Set previous score
            previousScore = lowestScore;
        } while (lowestScore != 0);
        //Print final board and info
        printBoard(grid, totalConflicts(grid));
        System.out.println("Solution Found!");
        System.out.println("State changes: " + stateCounter);
        System.out.println("Restarts: " + restartCounter);
    }
    public static void restart(int[][] board) {
        Random ran = new Random();
        //Resets board to all 0's
        for (int i = 0; i < 8; i++)
            for (int k = 0; k < 8; k++)
                board[i][k] = 0;
        //Populates board
        for (int i = 0; i < 8; i++)
            board[ran.nextInt(8)][i] = 1;
    }
    public static void printBoard(int[][] board, int heur) {
        System.out.println("Current h: " + heur);
        System.out.println("Current board:");
        //Prints board
        for (int i = 0; i < 8; i++) {
            for (int k = 0; k < 8; k++)
                System.out.print(board[i][k] + " ");
            System.out.println();
        }
    }
    public static int conflicts(int x, int y, int[][] board) {
        //Number of conflicts counted on board and a counter for traversal purposes.
        int conflicts = 0;
        int counter = 0;
        //Checks horizontally for conflicts
        for (int i = 0; i < 8; i++)
            if (board[y][i] == 1)
                conflicts++;
        conflicts--;
        //Checks diagonally upwards for conflicts in a stupid way
        for (int i = y - x; i < 8; i++) {
            if (i >= 0)
                if (0 <= counter && counter < 8)
                    if (board[i][counter] == 1)
                        conflicts++;
            counter++;
        }
        conflicts--;
        //Checks diagonally downwards for conflicts in a slightly less stupid way
        counter = x + y;
        for (int i = 0; i < 8; i++) {
            if (0 <= counter && counter < 8)
                if (board[counter][i] == 1)
                    conflicts++;
            counter--;
        }
        conflicts--;
        
        return conflicts;
    }
    public static int totalConflicts(int[][] board) {
        //Conflict counter
        int conflicts = 0;
        //Uses the conflicts method to count the total of all conflicts on the board
        for (int i = 0; i < 8; i++)
            for (int k = 0; k < 8; k++)
                if (board[i][k] == 1)
                    conflicts += conflicts(k, i, board);
        //The conflict checker doesn't account for repeats, so we fix this by halfing the counter
        conflicts /= 2;
        
        return conflicts;
    }
    public static void modifyColumn(int[][] board, int column , int y) {
        //Resets specific column and makes a specific coordinate 1
        for (int i = 0; i < 8; i++)
                board[i][column] = 0;
        board[y][column] = 1;
    }
    public static int hillClimber(int[][] board) {
        //Clone board we can alter
        int boardClone[][] = new int[8][8];
        //Jesus Christ... .clone() is stupid
        for (int q = 0; q < 8; q++)
            for (int w = 0; w < 8; w++)
                boardClone[q][w] = board[q][w];
        //Needed variables
        int neighbor = 0;
        int x = 0;
        int y = 0;
        int oldScore = totalConflicts(board);
        int runningScore;
        int lowestScore = oldScore;
        //Runs through each column
        for (int i = 0; i < 8; i++) {
            //Runs through each row in column
            for (int k = 0; k < 8; k++) {
                //Makes a new test for each coordinate
                modifyColumn(boardClone, i, k);
                runningScore = totalConflicts(boardClone);
                //If coordinate has a better h
                if (runningScore < lowestScore) {
                    lowestScore = runningScore;
                    x = i;
                    y = k;
                }
                //Adds to neighbor counter
                if (runningScore < oldScore)
                    neighbor++;
            }
            //.clone() almost killed me
            for (int q = 0; q < 8; q++)
                for (int w = 0; w < 8; w++)
                    boardClone[q][w] = board[q][w];
        }
        //Prints board and updates to new improved positions
        printBoard(board, oldScore);
        modifyColumn(board, x, y);
        System.out.println("Neighbors found with lower h: " + neighbor);
        //If restart or state change
        if (lowestScore == oldScore) {
            restart(board);
            System.out.println("Restart!");
        }
        else
            System.out.println("Setting new current state...");
        System.out.println();
        
        return lowestScore;
    }
}