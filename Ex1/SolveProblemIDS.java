import java.util.Stack;

public class SolveProblemIDS extends SolveProblem {

    /**
     * c'tor
     * @param initialState
     */
    public SolveProblemIDS(int[][] initialState) {
        super(initialState);
    }

    /**
     * IDS implementation
     */
    public void solve() {
        State returnedValue = null;
        while (true) { // runs until finds a solution
            returnedValue = this.runDfs(this.thirdParam);
            if (returnedValue == null) { // solution
                this.thirdParam++;
            } else {
                break;
            }
        }
        // write to the output file
        handleOutputFile(returnedValue.getPath() + " " + this.secParam + " " + this.thirdParam);

    }

    /**
     * This method runs DFS algo with a given depth
     * @param depth
     * @return if found a solution, than returns a state of it. null otherwise
     */
    private State runDfs(int depth) {
        if (this.checkForGoalState(this.initialBoard)) {
            return new State(this.initialBoard, "");
        }
        // create a stack
        Stack openList = new Stack();
        State s = new State(this.initialBoard,"");
        // push initState
        openList.push(s);
        this.secParam = 0;
        State currentState = null;
        // while the openList is not empty
        while(!openList.isEmpty()){
            // take the first element in the stack
            currentState = (State) openList.pop();
            this.secParam++;
            if(checkForGoalState(currentState.getBoard())){
                return currentState;
            } else { // not goal state
                // if last iteration of pushing states to the queue
                if(depth == currentState.getPath().length()){
                    continue;
                } else {
                    // try to create states of the currentState after all
                    // possible movements
                    for(int i = 0; i<5;i++){
                        State returnedState = this.changeBoard(currentState, 5-i);
                        if(returnedState != null){
                            openList.push(returnedState);
                        }
                    }
                }
            }
        }
        return null;
    }
}
