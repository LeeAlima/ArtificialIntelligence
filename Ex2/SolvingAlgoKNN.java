import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This class uses the KNN algorithm to solve the prediction problem. This class
 * implements the SolvingAlgo class so it can solve the problem by getting a
 * trainingSet and testSet.
 * @author DELL
 */
public class SolvingAlgoKNN implements SolvingAlgo {

    @Override
    public String[] solve(List<Map<String, String>> trainingSet, List<Map<String, String>> testSet) {
        // Set the k value to 5 as was requested in the "Targil"
        int k = 5;
        int counter = 0;
        // Get the first example in the testSet
        Map<String, String> testExample = testSet.get(0);
        // Get the attributes names
        String[] attributes = testExample.keySet().toArray(new String[testExample.keySet().size()]);
        // Get the last attribute
        String classAttr = attributes[attributes.length - 1];
        // Save the prediction results in the predictedResArr array
        String[] predictedResArr = new String[testSet.size() + 1];
        for (int i = 0; i < testSet.size(); i++) {
            // Save in the predictedResArr array the resulting of running the
            // KNN function with all the training data and the specific example
            // in the testSet
            predictedResArr[i] = KNN(trainingSet, testSet.get(i), k, classAttr);
            // If the predicted result is equal to the real result than add 1 to
            // the counter
            if (predictedResArr[i].compareTo(testSet.get(i).get(classAttr)) == 0) {
                counter++;
            }
        }
        // Calculate the accuracy by dividing the number of correct predictions
        // with the number of examples in the testSet
        double accuracy = (double) counter / testSet.size();
        double roundOff = Math.ceil(accuracy * 100.0) / 100.0;
        predictedResArr[predictedResArr.length - 1] = String.format("%.2f", roundOff);
        // Return the array
        return predictedResArr;
    }

    /**
     * This function runs the KNN algorithm
     * @param trainingSet - list of the training data
     * @param testExample - a specific test example
     * @param k - number
     * @param res - the name of the classificaion attribute
     * @return the predicted classification
     */
    public String KNN(List<Map<String, String>> trainingSet, Map<String, String> testExample, int k, String classAttr) {
        // Calculate the hamming distance for every example in the training set
        List<Map<String, Integer>> hammingDList = calculateAllHammingDistances(trainingSet, testExample);
        // Sort the hammingDList by the distance values
        Collections.sort(hammingDList, new Comparator<Map<String, Integer>>() {
            public int compare(Map<String, Integer> o1, Map<String, Integer> o2) {
                return (o1.get("distance")).compareTo(o2.get("distance"));
            }
        });
        // Get the top k examples
        List<Map<String, String>> topKExamples = getTopKExamples(hammingDList, k, trainingSet);
        // Get the most frequent value of the classification attribute
        // (classAttr)
        String classMax = getTheMostFreqVal(topKExamples, k, classAttr);
        return classMax;
    }

    /**
     * This function returns the most frequent value in the top K list
     * @param topKExamples - a list of examples
     * @param k - number
     * @param res - the name of the classificaion attribute
     * @return the most frequent value of the classification attribute
     */
    public String getTheMostFreqVal(List<Map<String, String>> topKExamples, int k, String classAttr) {
        // Creating a map - to map between value and the number of appearances
        // in the topK
        Map<String, Integer> getMostFreq = new HashMap<String, Integer>();
        String s = "";
        // Save the most frequent value in classMax
        String classMax = "";
        int max = -1;
        // Go over the topKExamples
        for (int j = 0; j < k; j++) {
            // get the example classification
            s = topKExamples.get(j).get(classAttr);
            // if the classification appears in the map, than add 1 to its value
            if (getMostFreq.containsKey(s)) {
                getMostFreq.put(s, getMostFreq.get(s) + 1);
            } else { // if the calssification doesn't appear in the map - create
                     // a record of it
                getMostFreq.put(s, 1);
            }
            // check for max and update it if needed
            if (getMostFreq.get(s) > max) {
                max = getMostFreq.get(s);
                classMax = s;
            }
        }
        // return the most frequent value
        return classMax;
    }

    /**
     * This function return the top k examples
     * @param hammingDList - a list with the hamming distance calculated for
     * every example in the trainingSet with the test example
     * @param k - number
     * @param trainingSet - the training set
     * @return a list with the top k
     */
    public List<Map<String, String>> getTopKExamples(List<Map<String, Integer>> hammingDList, int k,
            List<Map<String, String>> trainingSet) {
        // Creating a list of maps
        List<Map<String, String>> topKExamples = new LinkedList<Map<String, String>>();
        int index;
        // Go over the top k examples in the list
        for (int j = 0; j < k; j++) {
            // get the j's example in the hammingDList list -> than get the
            // example index
            index = hammingDList.get(j).get("index");
            // add the list the example with the same index
            topKExamples.add(trainingSet.get(index));
        }
        // return the list
        return topKExamples;
    }

    /**
     * This function go over the trainingSet and for every example in it
     * calculates the hamming distance
     * @return a list of maps. Each map represent an example identified by ID
     * (index) and the hammingDistance (distance)
     */
    public List<Map<String, Integer>> calculateAllHammingDistances(List<Map<String, String>> trainingSet,
            Map<String, String> testExample) {
        int distance;
        List<Map<String, Integer>> listOfHammingD = new LinkedList<Map<String, Integer>>();
        // Go over the examples in the training set and calculate the hamming
        // distance between them and the testExample
        for (int i = 0; i < trainingSet.size(); i++) {
            // calculate the hamming distance
            distance = hammingDistance(trainingSet.get(i), testExample);
            // In every map save the index value of the example and the hamming
            // distance value
            Map<String, Integer> newMap = new HashMap<>();
            newMap.put("index", i);
            newMap.put("distance", distance);
            // Add the map to the list
            listOfHammingD.add(newMap);
        }
        // Return the list
        return listOfHammingD;
    }

    /**
     * This function calculates the hamming distance of 2 maps
     * @param trainingExample - an example from the trainingSet
     * @param testExample - an example from the testSet
     * @return number - the result of calculating the hamming distance
     */
    public int hammingDistance(Map<String, String> trainingExample,Map<String, String> testExample){
        // Initilize the distance to 0
        int distance = 0;
        // Save all of the possible attributes in an array
        String[] attributes = trainingExample.keySet().toArray(new String[trainingExample.keySet().size()]);
        // Go over the attributes ( pass the last one - the classification )
        for (int i = 0; i < attributes.length - 1; i++) {
            // Save the value of the attribute in the trainingExample
            String trainingAttValue = trainingExample.get(attributes[i]);
            // Save the value of the attribute in the testExample
            String testAttrValue = testExample.get(attributes[i]);
            // If the values are different than add 1 to the hamming distance
            // variable
            if (trainingAttValue.compareTo(testAttrValue) != 0) {
                distance++;
            }
        }
        // return the hamming distance
        return distance;
    }
}
