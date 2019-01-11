import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class uses the Naive base algorithm to solve the prediction problem.
 * This class implements the SolvingAlgo class so it can solve the problem by
 * getting a trainingSet and testSet.
 * @author DELL
 */
public class SolvingAlgoNaiveBase implements SolvingAlgo {

    @Override
    public String[] solve(List<Map<String, String>> trainingSet, List<Map<String, String>> testSet) {
        Map<String, String> trainingExample = trainingSet.get(0);
        // Save the possible attributes
        String[] attributes = trainingExample.keySet().toArray(new String[trainingExample.keySet().size()]);
        // getting the classification attribute
        String classAttr = attributes[attributes.length - 1];
        // Get the possible values of the classification attribute
        Set<String> valuesSet = findPossibleValuesToKey(trainingSet, classAttr).keySet();
        // Save the set value in an array
        String[] values = findPossibleValuesToKey(trainingSet, classAttr).keySet()
                .toArray(new String[valuesSet.size()]);
        Map<String, Double> priors = new HashMap<String, Double>();
        // save the prior of every possible value of the classification
        // attribute
        for(int i = 0; i<values.length; i++){
            priors.put(values[i], getPrior(trainingSet, classAttr, values[i]));
        }
        // get the predictions
        String[] predictedResArr = getPredictions(trainingSet, testSet, values, priors, attributes, classAttr);
        return predictedResArr;
    }

    /**
     * This function predicts the classification
     * @param trainingSet - list of training example
     * @param testSet - list of test example
     * @param values - the possible values of the classificaion attribute
     * @param priors - the first prior result
     * @param attributes - list of attributes
     * @param classAttr - the classification attribute
     * @return an array with the classificaion
     */
    public String[] getPredictions(List<Map<String, String>> trainingSet, List<Map<String, String>> testSet,
            String[] values, Map<String, Double> priors, String[] attributes, String classAttr) {
        String[] predictedResArr = new String[testSet.size() + 1];
        int correctAnsCounter = 0;
        // Go over the test examples
        for (int i = 0; i < testSet.size(); i++) {
            // save the current example
            Map<String, String> example = testSet.get(i);
            String answer = "";
            double max = (double) -1;
            // go over the number of possible values of the classification
            // attribute
            for (int t = 0; t < values.length; t++) {
                // get the prior number of the value
                double calc = priors.get(values[t]);
                // get the classification attribute name
                String cAttr = classAttr;
                // get the value of the classification attribute name
                String cValue = values[t];
                // for every possible attributes
                for (int j = 0; j < attributes.length - 1; j++) {
                    // get the name of the specific attribute
                    String xAttr = attributes[j];
                    // get the value of this attribute
                    String xValue = example.get(xAttr);
                    // multiply of the prior -> P(xAttr=vValue|cAttr=cValue)
                    calc *= getPByGivenValueAndClass(trainingSet, xAttr, xValue, cAttr, cValue);
                }
                // get max
                if (calc > max) {
                    max = calc;
                    answer = values[t];
                }
            }
            predictedResArr[i] = answer;
            // If the predicted result is equal to the real result than add 1 to
            // the counter
            if (predictedResArr[i].compareTo(example.get(classAttr)) == 0) {
                correctAnsCounter++;
            }
        }
        // Calculate the accuracy by dividing the number of correct predictions
        // with the number of examples in the testSet
        double accuracy = (double) correctAnsCounter / testSet.size();
        double roundOff = Math.ceil(accuracy * 100.0) / 100.0;
        predictedResArr[predictedResArr.length - 1] = String.format("%.2f", roundOff);
        // Return the array
        return predictedResArr;
    }

    /**
     * This function returns the possible values of possible attributes and the
     * number of repeats
     * @param examples - list of examples
     * @param key - name of attribute
     * @return possible values of possible attributes and the number of repeats
     */
    public Map<String, Integer> findPossibleValuesToKey(List<Map<String, String>> examples, String key) {
        Map<String, Integer> possibleValues = new HashMap<String, Integer>();
        String val = "";
        // Go over the examples
        for (int i = 0; i < examples.size(); i++) {
            // get the value of the attribute in the certain example
            val = examples.get(i).get(key);
            // if the possibleValues contain the val key - than update its value
            // +1
            if (possibleValues.containsKey(val)) {
                possibleValues.put(val, possibleValues.get(val) + 1);
            } else { // create a record
                possibleValues.put(val, 1);
            }
        }
        // return the map
        return possibleValues;
    }

    /**
     * Get the prior value
     * @param trainingSet - the list of examples
     * @param xAttr - certain attribute
     * @param xValue - certain attribute value
     * @param cAttr - the classificaion attribue
     * @param cValue - certain classification value
     * @return the prior calculation
     */
    public double getPByGivenValueAndClass(List<Map<String, String>> trainingSet, String xAttr,
            String xValue, String cAttr, String cValue) {
        int fitCounter = 0;
        int classificationCounter = 0;
        // Go over the training examples
        for (int i = 0; i < trainingSet.size(); i++) {
            // Save the specific example from the trainingSet
            Map<String, String> example = trainingSet.get(i);
            // if the example example[cAttr] is equal to cValue
            if (example.get(cAttr).compareTo(cValue) == 0) {
                // add one to classificationCounter
                classificationCounter++;
                // if the example[xAttr] is equal to xValue
                if (example.get(xAttr).compareTo(xValue) == 0) {
                    // add one to fitCounter
                    fitCounter++;
                }
            }
        }
        // find possible values to the xAttr value
        int k = findPossibleValuesToKey(trainingSet, xAttr).size();
        // make the slip
        double res = (double) (fitCounter + 1) / (classificationCounter + k);
        // return the result
        return res;
    }

    /**
     * This function returns the proportionate share of the record in the
     * trainingSet
     * @param trainingSet - the list of examples
     * @param attr - The name of the attribute
     * @param value - the value of the attribute in the example test
     * @return double number - prior
     */
    public double getPrior(List<Map<String, String>> trainingSet, String attr, String value) {
        int counter = 0;
        // Go over the trainingSet examples
        for (int i = 0; i < trainingSet.size(); i++) {
            // If the examples has the attribute's value same as the value, add
            // 1 to counter
            if (trainingSet.get(i).get(attr).compareTo(value) == 0) {
                counter++;
            }
        }
        // return the prior
        return (double) counter / trainingSet.size();
    }
}
