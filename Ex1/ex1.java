import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ex1 {

    /***
     * This is the main method of the project
     * @param args - no use in the args
     */
    public static void main(String[] args) {
        // reading the input file
        List<String> info = readFromFile();
        // parse the list og info
        int solvingAlgo = Integer.parseInt(info.get(0));
        int boardSize = Integer.parseInt(info.get(1));
        String[] numbers = info.get(2).split("-");
        // create an int matrix which saves the initial state of the board
        int[][] board = fillMatrixWithStartState(boardSize, numbers);
        SolveProblem problem = null;
        // IDS algo
        if (solvingAlgo == 1) {
            problem = new SolveProblemIDS(board);
        } else if (solvingAlgo == 2) { // BFS algo
            problem = new SolveProblemBFS(board);
        } else if (solvingAlgo == 3) { // A* algo
            problem = new SolvingProblemAStar(board);
        }
        // Solving the problem
        problem.solve();
    }

    /***
     * This method fills the matrix with the start state
     * @param boardSize - matrix size
     * @param numbers - the numbers to fill the matrix with
     * @return a new matrix with those values
     */
    public static int[][] fillMatrixWithStartState(int boardSize, String[] numbers) {
        // creating a new board
        int[][] board = new int[boardSize][boardSize];
        int index = 0;
        // copying the values from the array
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = Integer.parseInt(numbers[index++]);
            }
        }
        return board;
    }

    /**
     * this method reads the input file
     * @return a list of the lines in the input file
     */
    public static List<String> readFromFile() {
        List<String> linesList = new LinkedList<String>();
        try {

            BufferedReader b = new BufferedReader(new FileReader("input.txt"));
            String line = "";
            // read line by line and add it to the list
            while ((line = b.readLine()) != null) {
                linesList.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return linesList;
    }

}
