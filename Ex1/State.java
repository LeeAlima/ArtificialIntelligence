public class State {
    int[][] board;
    String path;
    int fFunction;
    int size;
    int id;
    
    /**
     * c'tor
     * @param board - marix
     * @param path - path
     */
    public State(int[][] board, String path) {
        this.board = board;
        this.path = path;
        this.fFunction = 0;
        this.size = board.length;
        this.id = 0;
    }
    
    /**
     * Get id member
     * @return int - state's Id
     */
    public int getId() {
        return id;
    }

    /**
     * Set thr id member
     * @param id - as the state Id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * get fFunction memeber
     * @return - gFunction
     */
    public int getF() {
        return this.fFunction;
    }

    /**
     * updates the fFunction member
     */
    public void setF() {
        // calculate f = g + h
        this.fFunction = this.path.length(); // cost g
        this.fFunction += getHuristic(); // h is the huristic
    }
    
    /**
     * gets the path member
     * @return - path
     */
    public String getPath() {
        return path;
    }

    /**
     * updates path memeber
     * @param path - string
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * gets the matrix
     * @return - matrix
     */
    public int[][] getBoard() {
        return this.board;
    }

    /**
     * updates the board matrix
     * @param board - matrix
     */
    public void setBoard(int[][] board) {
        this.board = board;
    }

    /**
     * This method calculated the manhattan distance of a state
     * @return int - manhattan distance
     */
    public int getHuristic(){
        int huristic = 0;
        for(int i = 0; i<size;i++){
            for(int j = 0;j<size;j++){
                int value = board[i][j];
                if (value != 0){
                    // find the right raw
                    int rightRaw = (int)(Math.ceil((float)value/this.size)) - 1;
                    // find the right col
                    int rightCol = ((value % this.size) + this.size - 1) % this.size;
                    // add the distance to the huristic
                    huristic += Math.abs(i - rightRaw) + Math.abs(j - rightCol);
                }
            }
        }
        return huristic;
    }

}
