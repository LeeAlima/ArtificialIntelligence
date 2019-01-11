import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SolvingAlgoDTL implements SolvingAlgo {

    @Override
    public String[] solve(List<Map<String, String>> trainingSet, List<Map<String, String>> testSet) {
        String[] predictedResArr = new String[testSet.size() + 1];
        int counter = 0;
        Map<String, String> trainingExample = trainingSet.get(0);
        // get an array of attributes
        String[] arrtributes = trainingExample.keySet().toArray(new String[trainingExample.keySet().size()]);
        // set the classification attribute
        String classAttr = arrtributes[arrtributes.length - 1];
        // create a copy of the attributes without the classAttr
        arrtributes = copyAttr(arrtributes, classAttr);
        // call DTL in order to create the desicion tree
        Node tree = DTL(trainingSet, trainingSet, arrtributes, mode(trainingSet, classAttr), classAttr);
        // write the tree to the output_tree.txt
        voidOpenWReader(tree);
        // Go over the test set and save the prediction in predictedResArr
        for (int i = 0; i < testSet.size(); i++) {
            predictedResArr[i] = findInTree(tree, testSet.get(i));
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
        return predictedResArr;
    }

    /**
     * This function create a bufferReader fits to the file "output_tree.txt"
     * and calls handleOutputTreeFile to print the tree
     * @param tree - The tree's root
     */
    public void voidOpenWReader(Node tree) {
        BufferedWriter bw = null;
        FileWriter fw = null;
        try {
            // open
            fw = new FileWriter("output_tree.txt");
            bw = new BufferedWriter(fw);
            handleOutputTreeFile(0, tree,bw);
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
     * This function prints the tree
     * @param tabs - number of tabs
     * @param tree - subtree's root
     * @param bw - bufferWrite to the "output_tree.txt" file
     * @throws IOException exception in case of failing
     */
    public void handleOutputTreeFile(int tabs, Node tree, BufferedWriter bw) throws IOException {
        // for leaf in the tree - write the classification
        if (tree.checkIfLeaf()) {
            bw.write(":" + tree.getAttr());
        } else {
            String attr = tree.getAttr();
            String[] keys = tree.getKeys();
            // sort alphabetically the attributes
            java.util.Arrays.sort(keys);
            // handle each attribute's values
            for (int i = 0; i < keys.length; i++) {
                if (i != 0 || tabs != 0) {
                    bw.write("\n");
                }
                // write tabs
                if (tabs != 0) {
                    String tabStr = "";
                    for (int j = 0; j < tabs; j++) {
                        tabStr += "\t";
                    }
                    bw.write(tabStr + "|");
                }
                bw.write(attr + "=" + keys[i]);
                // call recursevly on the subtree
                handleOutputTreeFile(tabs + 1, tree.getChild(keys[i]), bw);
            }
        }
    }

    /**
     * This function find the classification of a given test example using the
     * desicion tree
     * @param tree - the desicion tree
     * @param testExample - the tested example
     * @return the classification
     */
    public String findInTree(Node tree, Map<String, String> testExample) {
        // If the tree is already a leaf than return classification value
        if (tree.checkIfLeaf()) {
            return tree.getAttr();
        }
        // else - save the attr value
        String attr = tree.getAttr();
        // get the attr's value of the tested example
        String value = testExample.get(attr);
        // continue searching for classification on the child associated with
        // the value
        Node leaf = tree.getChild(value);
        return findInTree(leaf, testExample);
    }

    /**
     * Creating the DTL using the algorithm we saw on the "Tirgul"
     * @param examples - list of examples
     * @param attributes - an array of attributes
     * @param defaultClass - the default class (majority from the calling time)
     * @param classAttr - the classification attribute
     * @return Node - represents a tree
     */
    public Node DTL(List<Map<String, String>> allExamples, List<Map<String, String>> examples, String[] attributes,
            String defaultClass, String classAttr) {
        // if no example was left - than return a new node with the defaultClass
        if(examples.isEmpty()){
            return new Node(defaultClass);
            // if examples share the same classification, than return the
            // classification
        } else if (checkForSameClassification(examples, classAttr)) {
            return new Node(examples.get(0).get(classAttr));
            // if there is no more attributes than return the majority
            // classification among the examples
        } else if (attributes.length == 0){
            return new Node(mode(examples, classAttr));
        } else {
            // choode the best attribute
            String best = chooseAttribute(examples, classAttr, attributes);
            // creat a new node with the best attribute
            Node tree = new Node(best);
            // get the possible values of the best attributes - to save as the
            // edges
            Map<String, Integer> valuesAndNum = findPossibleValuesToKey(allExamples, best);
            // save them in an array
            String[] values = valuesAndNum.keySet().toArray(new String[valuesAndNum.size()]);
            // go over the values (edges)
            for (int i = 0; i< values.length; i++){
                // filter the examples
                List<Map<String, String>> specificExamples = getExamples(examples,best,values[i]);          
                // call recursivly to DTL with the lefted attributes and the
                // filtered examples
                Node subtree = DTL(allExamples, specificExamples, copyAttr(attributes, best), mode(examples, classAttr),
                        classAttr);
                // add the subtree as a child to the tree -> saving
                // <edge value (attribute value), subtree>
                tree.addchild(values[i], subtree);
            }
            // return the tree
            return tree;
        }
    }
    
    /**
     * This function creates an array of the attributes omitting the
     * classification attribute
     * @param attributes - an array of the attributes
     * @param attr - the classificaion attribute
     * @return a new array with the filtered attributes
     */
    public String[] copyAttr(String[] attributes, String attr){
        List<String> copyList = new LinkedList<String>();
        // go over the array of attributes and compare tham to the
        // classification attribute. If they are not equal than add the
        // attribute to the copyList
        for(int i = 0; i < attributes.length; i++){
            if(attributes[i].compareTo(attr) != 0){
                copyList.add(attributes[i]);
            }
        }
        // return an array of the list data
        return copyList.toArray(new String[copyList.size()]);
    }
    
    /**
     * This function returns the most majority value of the classification
     * attribute among the examples
     * @param examples - list of examples
     * @param classAttr - the classificaion attribute
     * @return the majority value of the classificaion attribute
     */
    public String mode(List<Map<String, String>> examples, String classAttr){
        // find the possible values of the classificaion attribute
        Map<String, Integer> values = findPossibleValuesToKey(examples, classAttr);
        // save tham in an array
        String[] valuesArr = values.keySet().toArray(new String[values.keySet().size()]);
        int max = -1;
        String mode = "";
        // go over the possible values
        for(int i = 0; i< valuesArr.length; i++){
            // if the counter of the value is bigger than max than update max
            // and mode
            if (values.get(valuesArr[i]) > max) {
                max = values.get(valuesArr[i]);
                mode = valuesArr[i];
            } else if (values.get(valuesArr[i]) == max) {
                mode = returnPositiveClass(values);
            }
        }
        // return the majority value
        return mode;
    }
    
    /**
     * This method is called in case of equality in the mode function
     * @param values - possible classification values
     * @return the positive one
     */
    public String returnPositiveClass(Map<String, Integer> values) {
        if (values.containsKey("yes")) {
            return "yes";
        } else {
            return "true";
        }
    }

    /**
     * This function filters the examples list
     * @param examples - list of examples
     * @param attr - the attribute name
     * @param value - the requested value to filter with
     * @return new list which the attribute value is equal to value
     */
    public List<Map<String, String>> getExamples(List<Map<String, String>> examples, String attr, String value) {
        List<Map<String, String>> specificExamples = new LinkedList<Map<String, String>>();
        // go over the examples and if the example attribute's value is equal to
        // value - than add the example to the list of filtered examples
        for(int i = 0; i<examples.size(); i++){
            if (examples.get(i).get(attr).compareTo(value) == 0) {
                specificExamples.add(examples.get(i));
            }
        }
        // return the list of filtered examples
        return specificExamples;
    }
    
    /**
     * This function checks if a given list of examples has the same
     * classification
     * @param examples - list of examples
     * @param classAttr - the classification attribute
     * @return true if all the examples have the same classification, false
     * otherwise
     */
    public Boolean checkForSameClassification(List<Map<String, String>> examples, String classAttr) {
        Boolean flag = false;
        // Save the possible values of the classification attribute
        Map<String, Integer> possibleValues = findPossibleValuesToKey(examples, classAttr);
        // Save the values in an array
        String[] keys = possibleValues.keySet().toArray(new String[possibleValues.keySet().size()]);
        // Go over the keys array and if there is only one possible key than set
        // flag to true
        for(int i = 0; i<keys.length; i++){
            if(possibleValues.get(keys[i]) == examples.size()){
                flag = true;
            }
        }
        // Return the flag
        return flag;
    }

    /**
     * This finds the attribute with the biggest gain value
     * @param examples - list of examples
     * @param classification - the classification attribute
     * @param attributes - an array with the possible attributes
     * @return the best attribute
     */
    public String chooseAttribute(List<Map<String, String>> examples, String classification, String[] attributes) {
        double max = -1;
        double gain = 0;
        String attr = "";
        // Go over the attributes array and pick the attribute resulting the max
        // gain value
        for (int i = 0; i < attributes.length; i++) {
            // calculate the gain for the specific attribute
            gain = gain(examples, classification, attributes[i]);
            if (gain > max) {
                max = gain;
                attr = attributes[i];
            }
        }
        // return the attribute resulting the max gain value
        return attr;
    }

    /**
     * This function calculates the gain of a certain attribute
     * @param examples - list of examples
     * @param classification - the classification attribute
     * @param attribute - a certain attribute
     * @return the gain of a certain attribute
     */
    public double gain(List<Map<String, String>> examples, String classification, String attribute) {
        // calculate the entropy
        double gain = calculateEntropy(examples, classification);
        // finds the possible values of a certain attribute
        Map<String, Integer> possibleValues = findPossibleValuesToKey(examples, attribute);
        // save the values in an array
        String[] keys = possibleValues.keySet().toArray(new String[possibleValues.keySet().size()]);
        // save the proportions of the attribute's values
        Double[] listOfValues = getProportionList(examples, possibleValues);
        // go over the list of values
        for (int i = 0; i < listOfValues.length; i++) {
            List<Map<String, String>> examplesNew = new LinkedList<Map<String, String>>();
            // go over the example list
            for (int j = 0; j < examples.size(); j++) {
                // if the attribute value of the examples is equal to the
                // keys[i] than add the example to the new list of examples
                if (examples.get(j).get(attribute).compareTo(keys[i]) == 0) {
                    examplesNew.add(examples.get(j));
                }
            }
            // calculate the entropy of the examplesNew
            double e = calculateEntropy(examplesNew, classification);
            // add to gain -P(attribute=keys[i])*Entropy(attribute=keys[i])
            gain += -listOfValues[i] * e;
        }
        // return the gain
        return gain;
    }

    /**
     * This function finds the proportion of every possible key among the
     * examples
     * @param examples - list of examples
     * @param possibleValues - the possible values of the attribute
     * @return an array saving the proportions
     */
    public Double[] getProportionList(List<Map<String, String>> examples, Map<String, Integer> possibleValues) {
        // get the number of examples
        int numberOfExamples = examples.size();
        // get the possible values
        String[] keys = possibleValues.keySet().toArray(new String[possibleValues.keySet().size()]);
        Double[] listOfValues = new Double[keys.length];
        for (int i = 0; i < keys.length; i++) {
            // save the partial number of the certain value records among all of
            // the examples
            listOfValues[i] = (double) possibleValues.get(keys[i]) / numberOfExamples;
        }
        // return the list
        return listOfValues;
    }

    /**
     * This function finds the possible values of certain attribute
     * @param examples - list of examples
     * @param attribute - the attribute name
     * @return Map which maps between the name of the value and the number of
     * appearances
     */
    public Map<String, Integer> findPossibleValuesToKey(List<Map<String, String>> examples, String attribute) {
        // Create a map of possible value - saving <Value Name, Number of
        // appearences>
        Map<String, Integer> possibleValues = new HashMap<String, Integer>();
        String val = "";
        // go over the list of examples
        for(int i= 0; i<examples.size();i++){
            // save the example's attribute value
            val = examples.get(i).get(attribute);
            // if the map contains the key (certain value) than update the
            // counter
            if (possibleValues.containsKey(val)){
                possibleValues.put(val, possibleValues.get(val) + 1);
            } else { // if the map doent contain the key than creat a record and
                     // set it value to 1
                possibleValues.put(val, 1);
            }
        }
        // return the map
        return possibleValues;
    }

    /**
     * This function calculates the entropy of an attribute
     * @param examples - list of examples
     * @param key - an attribute name
     * @return double - the entropy
     */
    public double calculateEntropy(List<Map<String, String>> examples, String key) {
        // gets the possible values of an attribute
        Map<String, Integer> possibleValues = findPossibleValuesToKey(examples, key);
        // save the proportions of the possible values
        Double[] listOfValues = getProportionList(examples, possibleValues);
        // calculate the entropy
        double entropy = informationContent(listOfValues);
        // return the entropy
        return entropy;
    }

    /**
     * This function calculates the entropy
     * @param numbers - list of the proportion
     * @return the entropy
     */
    public double informationContent(Double[] numbers) {
        double sum = 0;
        // go over the proportions array ans add the calculation to sum
        for (int i = 0; i < numbers.length; i++) {
            sum += -numbers[i] * logBase2(numbers[i]);
        }
        // return sum
        return sum;
    }

    /**
     * This function calculate the log base 2 function
     * @param number - to calculate log(number) with base 2
     * @return double number - the result of log(number)
     */
    public double logBase2(double number) {
        return Math.log(number) / Math.log(2);
    }
}
