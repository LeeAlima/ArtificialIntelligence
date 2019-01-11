import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This class is responsible for running the all program using the main
 * function. In this class I created the solving algorithms classes and used
 * them to solve the certain problem and data.
 * @author DELL
 */
public class java_ex2 {

    /**
     * This is the main function of the all program. In this function I created
     * 3 different classes implemening the solvingAlgo class and used them to
     * solve the prediction problem.
     * @param args
     */
    public static void main(String[] args) {
        // Parse the two different text files and save each one in a list of
        // maps.
        // For the training data
        List<Map<String, String>> trainSet = getExamples("train.txt");
        // For the test data
        List<Map<String, String>> testSet = getExamples("test.txt");
        // Create the SolvingAlgoDTL class and use it to solve the certain
        // problem.
        SolvingAlgo DTL = new SolvingAlgoDTL();
        String[] arrDTL = DTL.solve(trainSet, testSet);
        // Create the SolvingAlgoKNN class and use it to solve the certain
        // problem.
        SolvingAlgo KNN = new SolvingAlgoKNN();
        String[] arrKNN = KNN.solve(trainSet, testSet);
        // Create the SolvingAlgoNaiveBase class and use it to solve the certain
        // problem.
        SolvingAlgo NB = new SolvingAlgoNaiveBase();
        String[] arrNB = NB.solve(trainSet, testSet);
        // Write the data to the outputfile
        handleOutputFile(arrDTL, arrKNN, arrNB);
    }

    /**
     * This function gets a name of a text file and parses it into a list of
     * maps saving each line as map in the list.
     * @param fileName - the name of the text file
     * @return a list of maps ( list of the examples in the text files )
     */
    public static List<Map<String, String>> getExamples(String fileName) {
        // creating the list of maps
        List<Map<String, String>> examplesList = new LinkedList<Map<String, String>>();
        String[] attributes = null;
        // Using this flag to know if the readen line is the first one or not
        Boolean flag = true;
        try {
            // Creating a bufferReader obj
            BufferedReader b = new BufferedReader(new FileReader(fileName));
            String line = "";
            // read line by line - parse it and save the data
            while ((line = b.readLine()) != null) {
                if (flag) { // first line
                    flag = false; // change boolean
                    // Get the attributes
                    attributes = splitExample(line);
                } else {
                    // Create a map to save each example in the file
                    Map<String, String> example = new LinkedHashMap<String, String>();
                    // Split the line
                    String[] data = splitExample(line);
                    // for every value in the example save it in the map as
                    // <Attribute, Value>
                    for (int i = 0; i < attributes.length; i++) {
                        example.put(attributes[i], data[i]);
                    }
                    // Save the certain example in the list
                    examplesList.add(example);
                }
            }
            // close the file
            b.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // return the list of examples
        return examplesList;
    }

    /**
     * This function gets a line and splitts it by a tab
     * @param line - a line in the text file
     * @return and array of string
     */
    public static String[] splitExample(String line) {
        String[] tokens = line.split("\\t+");
        return tokens;
    }

    /**
     * This function is responsible of writing the output file.
     * @param arrDTL - the predictions and accuracy using Desicion Tree
     * @param arrKNN - the predictions and accuracy using KNN
     * @param arrNB - the predictions and accuracy using Naive Base
     */
    public static void handleOutputFile(String[] arrDTL, String[] arrKNN, String[] arrNB) {
        BufferedWriter bw = null;
        FileWriter fw = null;
        try {
            // Opening the output file
            fw = new FileWriter("output.txt");
            // Creating a bufferWriter obj
            bw = new BufferedWriter(fw);
            // Write the first line in the pattern showed in the example
            bw.write("Num" + "\t" + "DT" + "\t" + "KNN" + "\t" + "naiveBase" + "\n");
            // Go over the lists and for every example - write the predictions
            // using the 3 algorithms
            for (int i = 0; i < arrDTL.length - 1; i++) {
                bw.write(Integer.toString(i + 1) + "\t" + arrDTL[i] + "\t" + arrKNN[i] + "\t" + arrNB[i] + "\n");
            }
            // Write the accurancy for every algorithm
            bw.write("\t" + arrDTL[arrDTL.length - 1] + "\t" + arrKNN[arrKNN.length - 1] + "\t"
                    + arrNB[arrNB.length - 1]);
            // Close the BufferedWriter
            if (bw != null)
                bw.close();
            // Close the FileWriter
            if (fw != null)
                fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
