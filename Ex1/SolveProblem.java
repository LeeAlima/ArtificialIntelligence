import java.io.BufferedWriter;
import java.io.FileWriter;

abstract class SolveProblem {
    int[][] initialBoard; // game board
    int secParam; // second parameter to write to the output file
    int thirdParam; // third parameter to write to the output file

    /**
     * c'tor
     * @param initialState - initial matrix
     */
    public SolveProblem(int[][] initialState) {
        this.initialBoard = initialState;
        this.secParam = 0;
        this.thirdParam = 0;
    }

    /**
     * This method checks if the board is the goal state
     * @param board - matrix
     * @return True if the board is the goal state, False otherwise
     */
    public Boolean checkForGoalState(int[][] board) {
        int boardSize = board.length;
        int value = 1;
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] == value) {
                    value++;
                    // last cell
                } else if (i == boardSize - 1 && j == boardSize - 1 && board[i][j] == 0) {
                    return true; // equals
                } else {
                    return false; // not equals
                }
            }
        }
        return false;
    }

    /**
     * This method swaps 2 elements in the matrix
     * @param board - matrix
     * @param raw1 - first cell raw
     * @param col1 - first cell col
     * @param raw2 - second cell raw
     * @param col2 - second cell col
     */
    public void swapeElementsInMatrix(int[][] board, int raw1, int col1, int raw2, int col2) {
        int temp = board[raw1][col1];
        board[raw1][col1] = board[raw2][col2];
        board[raw2][col2] = temp;
    }

    /**
     * This method copies one board matrix into new board matrix
     * @param board - the board to copy
     * @return - the copied board
     */
    public int[][] createAndCopyBoard(int[][] board) {
        // create a new board
        int[][] newBoard = new int[board.length][board.length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                // copy each cell
                newBoard[i][j] = board[i][j];
            }
        }
        return newBoard;
    }

    /**
     * This method updated the current board by moving it Up/Down/Left/Right
     * @param currentState - the current state
     * @param number - which represents in what direction the movement shoud
     * accour
     * @return new state
     */
    public State changeBoard(State currentState, int number) {
        int[][] board = currentState.getBoard();
        int len = board.length;
        // copy the old board
        int[][] newBoard = this.createAndCopyBoard(board);
        String path = currentState.getPath();
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                // finding the empty cell
                if (newBoard[i][j] == 0) {
                    if (number == 1 && i + 1 < len) { // Up
                        this.swapeElementsInMatrix(newBoard, i, j, i + 1, j);
                        return new State(newBoard, path + "U");
                    } else if (number == 2 && i - 1 >= 0) { // Down
                        this.swapeElementsInMatrix(newBoard, i, j, i - 1, j);
                        return new State(newBoard, path + "D");
                    } else if (number == 3 && j + 1 < len) { // Left
                        this.swapeElementsInMatrix(newBoard, i, j, i, j + 1);
                        return new State(newBoard, path + "L");
                    } else if (number == 4 && j - 1 >= 0) { // Right
                        this.swapeElementsInMatrix(newBoard, i, j, i, j - 1);
                        return new State(newBoard, path + "R");
                    }
                }
            }
        }
        return null;
    }

    /**
     * This method gets string and save it to the output file
     * @param s - the string to save
     */
    public void handleOutputFile(String s) {
        BufferedWriter bw = null;
        FileWriter fw = null;
        try {
            // open
            fw = new FileWriter("output.txt");
            bw = new BufferedWriter(fw);
            bw.write(s);
            // close
            if (bw != null)
                bw.close();
            if (fw != null)
                fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method will be implemented in the "sons"
     */
    public abstract void solve();
}
