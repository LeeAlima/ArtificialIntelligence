import java.util.Comparator;
import java.util.PriorityQueue;

public class SolvingProblemAStar extends SolveProblem {

    /**
     * c'tor
     * @param initialState - matrix
     */
    public SolvingProblemAStar(int[][] initialState) {
        super(initialState);
    }

    /**
     * A* implementation
     */
    public void solve() {
        int stateCounter = 0;
        // creation of priority queue
        PriorityQueue<State> openList = new PriorityQueue<State>(stateComparator);
        State initState = new State(this.initialBoard,"");
        initState.setId(stateCounter++);
        initState.setF();
        openList.add(initState);
        State currentState = null;
        State returnedState = null;
        // while the queue is not empty
        while(!openList.isEmpty()){
            currentState = openList.poll();
            this.secParam++;
            // check for goal state
            if(checkForGoalState(currentState.getBoard())){
                break;
            } else {
                // try to create states of the currentState after all
                // possible movements
                for (int i = 0; i < 5; i++) {
                    returnedState = this.changeBoard(currentState, i);
                    if (returnedState != null) {
                        returnedState.setF(); // initialize their F value
                        returnedState.setId(stateCounter++);
                        openList.add(returnedState);
                    }
                }
            }
        }
        this.thirdParam = currentState.fFunction;
        this.handleOutputFile(currentState.path + " " + this.secParam + " " + this.thirdParam);
    }

    /**
     * Use for the priority queue - pushing a State to the queue is being done
     * using this function of compare
     */
    public static Comparator<State> stateComparator = new Comparator<State>() {
        @Override
        public int compare(State o1, State o2) {
            if (o1.getF() == o2.getF()) {
                return o1.getId() - o2.getId();
            } else {
                return o1.getF() - o2.getF();
            }
        }
    };
}
