import java.util.List;
import java.util.Map;

/**
 * This is the main interface which porpuse is to predict a classification to a
 * given traininSet of data and testSet of data.
 * @author DELL
 */
public interface SolvingAlgo {

    /**
     * This function can solve a prediction problem
     * @param trainingSet - list of training examples saved as maps
     * @param testSet - list of tests examples saved as maps
     * @return an array of string with the classificaion results for each
     * example in the testSet and the accuracy value to the all testset.
     */
    public String[] solve(List<Map<String, String>> trainingSet, List<Map<String, String>> testSet);
}
