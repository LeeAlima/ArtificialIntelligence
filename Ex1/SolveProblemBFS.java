import java.util.LinkedList;

public class SolveProblemBFS extends SolveProblem {

    /**
     * c'tor
     * @param initialState - matrix
     */
    public SolveProblemBFS(int[][] initialState) {
        super(initialState);
    }

    /**
     * BFS implementation
     */
    public void solve() {
        // creating a queue
        LinkedList<State> queue = new LinkedList<State>();
        queue.add(new State(initialBoard, ""));
        State currentState = null;
        // while the list is not empty
        while (!queue.isEmpty()){
            // get the first elements in the queue
            currentState = queue.remove();
            this.secParam++;
            if (this.checkForGoalState(currentState.getBoard())){
                break;
            } else { // if currentState is not the goal state
                // try to create states of the currentState after all possible
                // movements
                for (int i = 0; i<5;i++){
                    State returnedState = this.changeBoard(currentState, i);
                    if (returnedState != null){
                        queue.add(returnedState);
                    }
                }
            }
        }
        // write to the output file
        this.handleOutputFile(currentState.getPath() + " " + this.secParam + " " + this.thirdParam);
    }

}
